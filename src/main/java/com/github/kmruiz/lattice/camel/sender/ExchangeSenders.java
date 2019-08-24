package com.github.kmruiz.lattice.camel.sender;

import org.apache.camel.CamelContext;

public final class ExchangeSenders {
    private ExchangeSenders() {
    }

    public static <T> CamelExchangeSender<T> sendingTo(String endpoint, CamelContext context) throws Exception {
        return new CamelExchangeSender<>(endpoint, context);
    }
}
