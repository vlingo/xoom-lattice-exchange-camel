// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.consumer;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;

import java.util.function.Consumer;

public final class ExchangeConsumers {
    private ExchangeConsumers() {}


    public static CamelExchangeConsumer receivingFrom(final String endpoint, final CamelContext context, final Consumer<Exchange> consumer) throws Exception {
        return new CamelExchangeConsumer(endpoint, context, consumer);
    }
}
