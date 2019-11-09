package io.vlingo.lattice.exchange.camel.e2e.model;

import io.vlingo.lattice.exchange.camel.adapter.JsonCamelExchangeAdapter;
import org.apache.camel.CamelContext;

import java.util.Objects;

public class MessageTypeA {
  private final String id;
  private final String message;

  public MessageTypeA(final String id, final String message) {
    this.id = id;
    this.message = message;
  }

  public String getId() {
    return id;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    final MessageTypeA that = (MessageTypeA) o;
    return id.equals(that.id) && message.equals(that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, message);
  }

  public static class Adapter extends JsonCamelExchangeAdapter<MessageTypeA, MessageTypeA> {
    public Adapter(final CamelContext camelContext) {
      super(camelContext, MessageTypeA.class);
    }
  }
}
