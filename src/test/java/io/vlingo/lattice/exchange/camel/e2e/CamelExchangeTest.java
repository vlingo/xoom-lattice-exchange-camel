// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.e2e;

import io.vlingo.lattice.exchange.Covey;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.lattice.exchange.ExchangeSender;
import io.vlingo.lattice.exchange.TextMessageAdapter;
import io.vlingo.lattice.exchange.camel.CamelExchange;
import io.vlingo.lattice.exchange.camel.CamelTest;
import io.vlingo.lattice.exchange.camel.CoveyFactory;
import io.vlingo.lattice.exchange.camel.e2e.model.MessageTypeA;
import io.vlingo.lattice.exchange.camel.e2e.model.MessageTypeB;
import io.vlingo.lattice.exchange.camel.e2e.model.MessageTypeC;
import io.vlingo.lattice.exchange.camel.sender.ExchangeSenders;
import org.apache.camel.CamelContext;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Queue;
import java.util.Random;
import java.util.UUID;

public class CamelExchangeTest extends CamelTest {

  @Test
  void shouldBeAbleToConsumeAProducerMessageFromAExchange() throws Exception {
    final CamelContext camelContext = context();
    final String endpoint = "seda:" + getRandomString();
    Exchange exchange = new CamelExchange(camelContext, getRandomString(), endpoint);

    try {
      final MockMessageReceiver<String> messageReceiver = new MockMessageReceiver<>(2);

      final ExchangeSender<org.apache.camel.Exchange> sender = ExchangeSenders.sendingTo(endpoint, camelContext);

      final Covey<String, String, org.apache.camel.Exchange> covey = CoveyFactory.build(sender,
                                                                                        messageReceiver,
                                                                                        new TextMessageAdapter(camelContext),
                                                                                        String.class,
                                                                                        String.class);
      exchange.register(covey);

      final String msg1 = getRandomString();
      final String msg2 = getRandomString();
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

  @Test
  void shouldSupportMultipleCoveys() throws Exception {
    final CamelContext camelContext = context();
    final String endpoint = "seda:" + "multiple_covey_test";
    Exchange exchange = new CamelExchange(camelContext, getRandomString(), endpoint);

    try {
      final ExchangeSender<org.apache.camel.Exchange> sender = ExchangeSenders.sendingTo(endpoint, camelContext);

      final MockMessageReceiver<MessageTypeA> typeAReceiver = new MockMessageReceiver<>(1);
      exchange.register(CoveyFactory.build(sender, typeAReceiver, new MessageTypeA.Adapter(camelContext), MessageTypeA.class, MessageTypeA.class));

      final MockMessageReceiver<MessageTypeB> typeBReceiver = new MockMessageReceiver<>(1);
      exchange.register(CoveyFactory.build(sender, typeBReceiver, new MessageTypeB.Adapter(camelContext), MessageTypeB.class, MessageTypeB.class));

      final MockMessageReceiver<MessageTypeC> typeCReceiver = new MockMessageReceiver<>(1);
      exchange.register(CoveyFactory.build(sender, typeCReceiver, new MessageTypeC.Adapter(camelContext), MessageTypeC.class, MessageTypeC.class));

      final Random random = new Random();

      final MessageTypeA msgA = new MessageTypeA(getRandomString(), getRandomString());
      final MessageTypeB msgB = new MessageTypeB(getRandomString(), random.nextLong());
      final MessageTypeC msgC = new MessageTypeC(getRandomString(), new MessageTypeC.NestedMessage(getRandomString(), random.nextInt()));

      exchange.send(msgA);
      exchange.send(msgB);
      exchange.send(msgC);

      final Queue<MessageTypeA> resultsTypeA = typeAReceiver.getResults();
      final Queue<MessageTypeB> resultsTypeB = typeBReceiver.getResults();
      final Queue<MessageTypeC> resultsTypeC = typeCReceiver.getResults();

      Assert.assertEquals(1, resultsTypeA.size());
      Assert.assertEquals(1, resultsTypeB.size());
      Assert.assertEquals(1, resultsTypeC.size());

      Assert.assertEquals(msgA, resultsTypeA.poll());
      Assert.assertEquals(msgB, resultsTypeB.poll());
      Assert.assertEquals(msgC, resultsTypeC.poll());
    } finally {
      exchange.close();
    }

  }

  private String getRandomString() {
    return UUID.randomUUID().toString();
  }
}
