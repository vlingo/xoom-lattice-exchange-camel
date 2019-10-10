package io.vlingo.lattice.exchange.camel.channel;

import org.apache.camel.CamelContext;

public final class ExchangeChannels {
    private ExchangeChannels() {}


    public static <T> ExchangeChannel<T> receivingFrom(String endpoint, CamelContext context, Class<T> messageClass) {
        return new ExchangeChannel<>(endpoint, context, messageClass);
    }
}
