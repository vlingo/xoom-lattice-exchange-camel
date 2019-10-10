// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel;

import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.lattice.exchange.camel.channel.ExchangeChannel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class CamelTestWithDockerIntegration<T extends GenericContainer> extends CamelTest {
    private T container;
    private String content;

    protected abstract T testContainer();
    protected abstract String exchangeUri(T forContainer);

    @BeforeEach
    public void initializeContainer() {
        container = testContainer();
        container.start();

        content = UUID.randomUUID().toString();
    }

    @AfterEach
    public void tearDownContainer() {
        container.stop();
    }

    @Test
    public void shouldConsumeAndReadTheSameMessageFromTheSameExchange() throws Exception {
        String exchangeUri = exchangeUri(container);
        Exchange exchange = new CamelExchange<>(context(), exchangeUri, String.class, exchangeUri);

        ExchangeChannel<String> channel = exchange.channel();

        exchange.send(content);

        Optional<String> receivedBody = channel.receive(DEFAULT_TIMEOUT);
        assertTrue(receivedBody.isPresent());
        assertEquals(content, receivedBody.get());
    }
}
