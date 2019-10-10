package io.vlingo.lattice.exchange.camel;

import io.vlingo.lattice.exchange.Covey;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.lattice.exchange.Forwarder;
import io.vlingo.lattice.exchange.camel.channel.ExchangeChannel;
import io.vlingo.lattice.exchange.camel.channel.ExchangeChannels;
import io.vlingo.lattice.exchange.camel.sender.CamelExchangeSender;
import io.vlingo.lattice.exchange.camel.sender.ExchangeSenders;
import org.apache.camel.CamelContext;

public class CamelExchange<ExchangeType> implements Exchange {
    private final CamelContext camelContext;
    private final String name;
    private final ExchangeChannel<ExchangeType> channel;
    private final CamelExchangeSender<ExchangeType> sender;

    private final Forwarder forwarder;

    public CamelExchange(CamelContext camelContext, String name, Class<ExchangeType> exchangeType, String endpoint) throws Exception {
        this.camelContext = camelContext;
        this.name = name;
        this.channel = ExchangeChannels.receivingFrom(endpoint, camelContext, exchangeType);
        this.sender = ExchangeSenders.sendingTo(endpoint, camelContext);
        this.forwarder = new Forwarder();
    }

    @Override
    public void close() {
        channel.close();

        try {
            sender.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        camelContext.stop();
    }

    @Override
    public <T> T channel() {
        return (T) channel;
    }

    @Override
    public <T> T connection() {
        return (T) camelContext;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public <L, E, EX> Exchange register(Covey<L, E, EX> covey) {
        // ? forwarder.register(covey);
        return this;
    }

    @Override
    public <L> void send(L l) {
        sender.send((ExchangeType) l);
        // ? forwarder.forwardToSender(l);
    }
}
