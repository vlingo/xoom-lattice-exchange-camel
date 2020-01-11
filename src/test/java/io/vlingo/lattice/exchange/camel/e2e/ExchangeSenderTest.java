// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.e2e;

import io.vlingo.lattice.exchange.camel.CamelTest;
import io.vlingo.lattice.exchange.camel.sender.ExchangeSenders;
import io.vlingo.lattice.exchange.ExchangeSender;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangeSenderTest extends CamelTest {
    private final String ENDPOINT = "seda:" + UUID.randomUUID().toString();
    private final String CONTENT = UUID.randomUUID().toString();

    @Test
    void shouldSendAConsumableExchangeToTheSelectedEndpoint() throws Exception {
        ExchangeSender<Exchange> sender = ExchangeSenders.sendingTo(ENDPOINT, context());
        sender.send(ExchangeBuilder.anExchange(context()).withBody(CONTENT).build());

        String receivedBody = consumer().receiveBody(ENDPOINT, DEFAULT_TIMEOUT).toString();
        assertEquals(CONTENT, receivedBody);
    }
}
