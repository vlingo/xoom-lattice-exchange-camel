package io.vlingo.lattice.exchange.camel.e2e.model;

import io.vlingo.lattice.exchange.camel.adapter.JsonCamelExchangeAdapter;
import org.apache.camel.CamelContext;

import java.util.Objects;

public class MessageTypeB {
  private final String id;
  private final Long value;

  public MessageTypeB(final String id, final Long value) {
    this.id = id;
    this.value = value;
  }

  public String getId() {
    return id;
  }

  public Long getValue() {
    return value;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    final MessageTypeB that = (MessageTypeB) o;
    return id.equals(that.id) && value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, value);
  }

  public static class Adapter extends JsonCamelExchangeAdapter<MessageTypeB, MessageTypeB> {

    public Adapter(final CamelContext camelContext) {
      super(camelContext, MessageTypeB.class);
    }

  }
}
