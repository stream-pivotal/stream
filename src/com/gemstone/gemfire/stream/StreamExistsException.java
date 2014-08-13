/*=========================================================================
 * Copyright (c) 2002-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * more patents listed at http://www.pivotal.io/patents.
 *========================================================================
 */

package com.gemstone.gemfire.stream;

public class StreamExistsException extends Exception {
  public StreamExistsException() {
    
  }

  public StreamExistsException(String msg) {
    super(msg);
  }
}
