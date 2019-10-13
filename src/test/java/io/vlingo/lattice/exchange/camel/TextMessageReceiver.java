// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.exchange.camel;

import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.lattice.exchange.ExchangeReceiver;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TextMessageReceiver implements ExchangeReceiver<String> {
  private final AccessSafely accessSafely;
  private final Queue<Object> results = new ConcurrentLinkedQueue<>();

  public TextMessageReceiver(final int expectedMessages) {
    accessSafely = AccessSafely.afterCompleting(expectedMessages);
    accessSafely.writingWith("results", results::add);
    accessSafely.readingWith("results", () -> results);
  }

  @Override
  public void receive(final String message) {
    accessSafely.writeUsing("results", message);
  }


  public Queue<Object> getResults(){
    return accessSafely.readFrom("results");
  }
}
