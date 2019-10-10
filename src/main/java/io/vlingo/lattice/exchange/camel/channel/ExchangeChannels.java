// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel.channel;

import org.apache.camel.CamelContext;

public final class ExchangeChannels {
    private ExchangeChannels() {}


    public static <T> ExchangeChannel<T> receivingFrom(String endpoint, CamelContext context, Class<T> messageClass) {
        return new ExchangeChannel<>(endpoint, context, messageClass);
    }
}
