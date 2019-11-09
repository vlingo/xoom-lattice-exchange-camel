// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel;

import io.vlingo.lattice.exchange.Covey;
import io.vlingo.lattice.exchange.ExchangeReceiver;
import io.vlingo.lattice.exchange.ExchangeSender;
import io.vlingo.lattice.exchange.camel.adapter.AbstractCamelExchangeAdapter;
import org.apache.camel.Exchange;

/**
 * A factory that produces Exchange {@code io.vlingo.lattice.exchange.Covey} instances to be used with {@code io.vlingo.lattice.exchange.camel.CamelExchange}.
 */
public final class CoveyFactory {
  private CoveyFactory() {
    //prevent instantiation
  }

  /**
   * Build an exchange {@code io.vlingo.lattice.exchange.Covey} to be used with {@code io.vlingo.lattice.exchange.camel.CamelExchange}.
   *
   * @param <L> the local object type
   * @param <E> the external object type
   */
  public static <L, E> Covey<L, E, Exchange> build(final ExchangeSender<Exchange> sender, final ExchangeReceiver<L> receiver,
                                                   final AbstractCamelExchangeAdapter<L, E> adapter, final Class<L> localClass, final Class<E> externalClass) {

    return new Covey<>(sender, receiver, adapter, localClass, externalClass, Exchange.class);
  }
}
