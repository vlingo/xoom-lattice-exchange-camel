// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.sender;

import io.vlingo.lattice.exchange.ExchangeSender;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.engine.DefaultProducerTemplate;

import java.util.UUID;

/**
 * Class that allows an actor to send a message through a CamelExchange.
 * This class is mostly used in the CamelExchange class.
 *
 * @param <T>
 */
class CamelExchangeSender<T> implements ExchangeSender<Exchange>, AutoCloseable {
  private final String entrypoint;
  private final ProducerTemplate producerTemplate;

  public CamelExchangeSender(String endpoint, CamelContext context) throws Exception {
    // all messages will be sent to this seda queue. SEDA queues are asynchronous and ordered.
    this.entrypoint = "seda:" + UUID.randomUUID()
                                    .toString();

    context.addRoutes(new RouteBuilder() {
      @Override
      public void configure() {
        // This route will read all messages from the entrypoint seda queue
        // and then sent to the final endpoint (SQS, ActiveMQ, Rabbit, whatever).
        // This allows us to buffer, aggregate or process messages before sending them to the exchange
        // implementation.

        from(entrypoint).to(endpoint)
                        .routeId(String.format("ExchangeProducerRoute[%s]", endpoint))
                        .log(LoggingLevel.DEBUG, "Message sent: ${body}");
      }
    });

    this.producerTemplate = new DefaultProducerTemplate(context);
    this.producerTemplate.start();
  }

  public void send(Exchange message) {
    this.producerTemplate.send(entrypoint, message);
  }

  @Override
  public void close() {
    this.producerTemplate.stop();
  }
}
