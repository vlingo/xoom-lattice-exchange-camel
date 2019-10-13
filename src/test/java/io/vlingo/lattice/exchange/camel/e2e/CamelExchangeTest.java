// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.e2e;

import io.vlingo.lattice.exchange.Covey;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.lattice.exchange.TextMessageAdapter;
import io.vlingo.lattice.exchange.camel.CamelExchange;
import io.vlingo.lattice.exchange.camel.CamelTest;
import io.vlingo.lattice.exchange.camel.CoveyFactory;
import io.vlingo.lattice.exchange.camel.TextMessageReceiver;
import org.apache.camel.CamelContext;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Queue;
import java.util.UUID;

public class CamelExchangeTest extends CamelTest {
    private final String NAME = UUID.randomUUID().toString();
    private final String ENDPOINT = "seda:" + UUID.randomUUID().toString();

    @Test
    void shouldBeAbleToConsumeAProducerMessageFromAExchange() throws Exception {
        final CamelContext camelContext = context();
        Exchange exchange = new CamelExchange(camelContext, NAME, ENDPOINT);

        try {
            final TextMessageReceiver messageReceiver = new TextMessageReceiver(2);

            final Covey<String, String, org.apache.camel.Exchange> covey = CoveyFactory.build(camelContext, ENDPOINT,
                                                                           messageReceiver,
                                                                           new TextMessageAdapter(camelContext),
                                                                           String.class, String.class);
            exchange.register(covey);

            final String msg1 = UUID.randomUUID().toString();
            final String msg2 = UUID.randomUUID().toString();
            exchange.send(msg1);
            exchange.send(msg2);

            final Queue<Object> results = messageReceiver.getResults();

            Assert.assertEquals(2, results.size());
            Assert.assertEquals(msg1, results.poll());
            Assert.assertEquals(msg2, results.poll());

        } finally {
            exchange.close();
        }
    }
}
