/*=========================================================================
 * Copyright (c) 2002-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */

package com.gemstone.gemfire.stream.internal;

import java.util.List;

import com.gemstone.gemfire.CopyHelper;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEvent;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventListener;
import com.gemstone.gemfire.stream.StreamFn;

public class StreamListener implements AsyncEventListener {
	
  private StreamImpl stream;
  
  StreamListener(StreamImpl stream) {
    this.stream = stream;
  }
  
  @Override
  public void close() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean processEvents(List<AsyncEvent> events) {
    // Process each AsyncEvent
    for(AsyncEvent event: events) {
      // Add support to see if functions can handle serialized values.
      // Add support to check if data needs to be copied
      Object streamData = event.getDeserializedValue();
      

      // Execute StreamFns on the stream. Vertical.
      StreamFn[] streamFns = this.stream.getStreamFns();
      if (streamFns != null) {
        Object streamDataCopy;
        for (StreamFn streamFn : streamFns) {
          streamDataCopy = CopyHelper.deepCopy(streamData);
          executeStreamFns(streamFn, streamDataCopy);
        }
      }
    }

    return true;
  }

  /**
   * Executes stream function. Iterates over stream functions linked with 
   * stream function (horizontal) and executes them.
   */
  private void executeStreamFns (StreamFn streamFn, Object data) {

    try {
      Object result = streamFn.execute(data);

      // Execute StreamFns chained with StreamFn. Horizontal.
      StreamFn[] streamFns = streamFn.getStreamFns();
      if (streamFns != null) {
        Object resultCopy;
        for (StreamFn streamFnsStreamFn : streamFns) {
          resultCopy = CopyHelper.deepCopy(result);
          executeStreamFns(streamFnsStreamFn, resultCopy);
        } 
      }
    } catch (Exception ex) {
      // Log exception.
    }
  }
  
}
