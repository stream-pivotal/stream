/*=========================================================================
 * Copyright (c) 2002-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */

package com.gemstone.gemfire.stream;

import java.util.Collection;

import com.gemstone.gemfire.stream.internal.StreamManager;


/*
 *
 * @since 8.0
 */
public class StreamService {

  public static Stream createStream(String streamName) throws StreamExistsException, StreamInitException {
    return StreamManager.getInstance().createStream(streamName);
  }
  
  public static Stream getStream(String streamName) {
    return StreamManager.getInstance().getStream(streamName);
  }

  public static Collection<Stream> getAllStreams(String streamName) {
    return StreamManager.getInstance().getAllStreams();
  }

}