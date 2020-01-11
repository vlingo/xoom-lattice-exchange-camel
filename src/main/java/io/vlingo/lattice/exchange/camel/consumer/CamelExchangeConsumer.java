// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.consumer;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.engine.DefaultConsumerTemplate;

import java.util.function.Consumer;

/**
 * Class that allows an actor to read from an exchange.
 * Instances of this class are created through the CamelExchange#channel method.
 */
public class CamelExchangeConsumer implements AutoCloseable {
  private final ConsumerTemplate consumerTemplate;

  public CamelExchangeConsumer(final String endpoint, final CamelContext camelContext, final Consumer<Exchange> consumer) throws Exception {
    this.consumerTemplate = new DefaultConsumerTemplate(camelContext);

    camelContext.addRoutes(new RouteBuilder() {
      @Override
      public void configure() {
        from(endpoint).routeId(String.format("ExchangeConsumerRoute[%s]", endpoint))
                      .log(LoggingLevel.DEBUG, "Message received: ${body}")
                      .process(consumer::accept)
                      .onException(Throwable.class)
                      .continued(true);
      }
    });

    this.consumerTemplate.start();
  }

  @Override
  public void close() {
    consumerTemplate.stop();
  }
}
