// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.e2e;

import io.vlingo.lattice.exchange.camel.CamelExchange;
import io.vlingo.lattice.exchange.camel.CamelTest;
import io.vlingo.lattice.exchange.camel.channel.ExchangeChannel;
import io.vlingo.lattice.exchange.Exchange;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CamelExchangeTest extends CamelTest {
    private final String NAME = UUID.randomUUID().toString();
    private final String ENDPOINT = "seda:" + UUID.randomUUID().toString();
    private final String CONTENT = UUID.randomUUID().toString();

    @Test
    void shouldBeAbleToConsumeAProducerMessageFromAExchange() throws Exception {
        Exchange exchange = new CamelExchange<>(context(), NAME, String.class, ENDPOINT);

        ExchangeChannel<String> channel = exchange.channel();

        exchange.send(CONTENT);

        Optional<String> receivedBody = channel.receive(DEFAULT_TIMEOUT);
        assertTrue(receivedBody.isPresent());
        assertEquals(CONTENT, receivedBody.get());
    }
}
