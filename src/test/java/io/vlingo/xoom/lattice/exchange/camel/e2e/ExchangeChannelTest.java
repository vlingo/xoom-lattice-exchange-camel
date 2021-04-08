// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.lattice.exchange.camel.e2e;

import io.vlingo.xoom.lattice.exchange.camel.CamelTest;
import io.vlingo.xoom.lattice.exchange.camel.consumer.ExchangeConsumers;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExchangeChannelTest extends CamelTest {
  private final String ENDPOINT = "seda:" + UUID.randomUUID()
                                                .toString();

  @Test
  void shouldConsumeAMessageThatIsWaitingInTheChannel() throws Exception {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    AtomicReference<String> receivedMessage = new AtomicReference<>();

    ExchangeConsumers.receivingFrom(ENDPOINT, context(), exchange -> {
      receivedMessage.set(exchange.getMessage().getBody(String.class));
      countDownLatch.countDown();
    });

    String[] messages = producedMessagesAre(UUID.randomUUID().toString());

    countDownLatch.await(1, TimeUnit.SECONDS);

    assertNotNull(receivedMessage.get());
    assertEquals(messages[0], receivedMessage.get());
  }

  private String[] producedMessagesAre(String... messages) {
    Stream.of(messages)
          .forEach(message -> producer().sendBody(ENDPOINT, message));
    return messages;
  }
}
