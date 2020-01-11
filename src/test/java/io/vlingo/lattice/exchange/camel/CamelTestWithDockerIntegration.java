// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel;

import java.util.Queue;
import java.util.UUID;

import org.apache.camel.CamelContext;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import io.vlingo.lattice.exchange.Covey;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.lattice.exchange.ExchangeSender;
import io.vlingo.lattice.exchange.TextMessageAdapter;
import io.vlingo.lattice.exchange.camel.e2e.MockMessageReceiver;
import io.vlingo.lattice.exchange.camel.sender.ExchangeSenders;

@SuppressWarnings("rawtypes")
public abstract class CamelTestWithDockerIntegration<T extends GenericContainer> extends CamelTest {
  private T container;

  protected abstract T testContainer();

  protected abstract String exchangeUri(T forContainer);

  @BeforeEach
  public void initializeContainer() {
    container = testContainer();
    container.start();
  }

  @AfterEach
  public void tearDownContainer() {
    container.stop();
  }

  @Test
  public void shouldConsumeAndReadTheSameMessageFromTheSameExchange() throws Exception {
    String exchangeUri = exchangeUri(container);
    final CamelContext camelContext = context();
    Exchange exchange = new CamelExchange(context(), exchangeUri, exchangeUri);

    try {
      final MockMessageReceiver<String> messageReceiver = new MockMessageReceiver<>(2);

      final ExchangeSender<org.apache.camel.Exchange> sender = ExchangeSenders.sendingTo(exchangeUri, camelContext);

      final Covey<String, String, org.apache.camel.Exchange> covey = CoveyFactory.build(sender,
                                                                                        messageReceiver,
                                                                                        new TextMessageAdapter(camelContext),
                                                                                        String.class,
                                                                                        String.class);
      exchange.register(covey);

      final String msg1 = UUID.randomUUID().toString();
      final String msg2 = UUID.randomUUID().toString();
      exchange.send(msg1);
      exchange.send(msg2);

      final Queue<String> results = messageReceiver.getResults();
      Assert.assertEquals(2, results.size());
      Assert.assertEquals(msg1, results.poll());
      Assert.assertEquals(msg2, results.poll());
    } finally {
      exchange.close();
    }
  }
}
