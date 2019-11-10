package io.vlingo.lattice.exchange;

import io.vlingo.lattice.exchange.camel.adapter.AbstractCamelExchangeAdapter;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;

public class TextMessageAdapter extends AbstractCamelExchangeAdapter<String, String> {
  public TextMessageAdapter(final CamelContext camelContext) {super(camelContext, String.class);}

  @Override
  public String fromExchange(final Exchange exchangeMessage) {
    return exchangeMessage.getMessage().getBody(String.class);
  }

  @Override
  protected ExchangeBuilder buildExchange(final ExchangeBuilder builder, final String localMessage) {
    return builder.withBody(localMessage);
  }
}
