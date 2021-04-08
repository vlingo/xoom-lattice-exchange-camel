package io.vlingo.xoom.lattice.exchange.camel.e2e.model;

import io.vlingo.xoom.lattice.exchange.camel.adapter.JsonCamelExchangeAdapter;
import org.apache.camel.CamelContext;

import java.util.Objects;

public class MessageTypeC {
  private final String id;
  private final NestedMessage nestedMsg;

  public MessageTypeC(final String id, final NestedMessage nestedMsg) {
    this.id = id;
    this.nestedMsg = nestedMsg;
  }

  public String getId() {
    return id;
  }

  public NestedMessage getNestedMsg() {
    return nestedMsg;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    final MessageTypeC that = (MessageTypeC) o;
    return id.equals(that.id) && nestedMsg.equals(that.nestedMsg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nestedMsg);
  }

  public static class NestedMessage {
    private final String value;
    private final Integer id;

    public NestedMessage(final String value, final Integer id) {
      this.value = value;
      this.id = id;
    }

    public String getValue() {
      return value;
    }

    public Integer getId() {
      return id;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      final NestedMessage that = (NestedMessage) o;
      return value.equals(that.value) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value, id);
    }
  }

  public static class Adapter extends JsonCamelExchangeAdapter<MessageTypeC, MessageTypeC> {

    public Adapter(final CamelContext camelContext) {
      super(camelContext, MessageTypeC.class);
    }

  }
}
