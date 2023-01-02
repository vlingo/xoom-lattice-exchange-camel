// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.lattice.exchange.camel.sender;

import org.apache.camel.CamelContext;

public final class ExchangeSenders {
    private ExchangeSenders() {
    }

    public static <T> CamelExchangeSender<T> sendingTo(String endpoint, CamelContext context) throws Exception {
        return new CamelExchangeSender<>(endpoint, context);
    }
}
