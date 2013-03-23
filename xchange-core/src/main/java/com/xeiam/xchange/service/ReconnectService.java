/**
 * Copyright (C)  2013 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author alexnugent
 */
public class ReconnectService {

  private final Logger log = LoggerFactory.getLogger(ReconnectService.class);

  // TODO make this configurable from the oiutside
  private final int maxConnectionAttempts = 10;

  private final StreamingExchangeService streamingExchangeService;

  private int numConnectionAttempts = 0;

  /**
   * Constructor
   * 
   * @param streamingExchangeService
   */
  public ReconnectService(StreamingExchangeService streamingExchangeService) {

    this.streamingExchangeService = streamingExchangeService;
  }

  public void intercept(ExchangeEvent exchangeEvent) {

    if (exchangeEvent.getEventType() == ExchangeEventType.ERROR || exchangeEvent.getEventType() == ExchangeEventType.DISCONNECT) {
      reconnect();
    } else if (exchangeEvent.getEventType() == ExchangeEventType.CONNECT) {
      numConnectionAttempts = 0;
    }

  }

  private void reconnect() {

    log.debug("ExchangeType Error. Attempting reconnect " + numConnectionAttempts + " of " + maxConnectionAttempts);

    if (numConnectionAttempts > maxConnectionAttempts) {
      log.debug("Terminating reconnection attempts.");
      return;
    }

    streamingExchangeService.connect();
    numConnectionAttempts++;

  }

}
