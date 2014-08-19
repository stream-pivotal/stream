/*=========================================================================
 * Copyright (c) 2002-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */

package com.gemstone.gemfire.stream.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionShortcut;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventListener;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventQueueFactory;
import com.gemstone.gemfire.cache.asyncqueue.internal.AsyncEventQueueImpl;
import com.gemstone.gemfire.stream.Stream;
import com.gemstone.gemfire.stream.StreamFn;
import com.gemstone.gemfire.stream.*;


public class StreamImpl implements Stream {
  
  private String streamName;
  
  private Region streamRegion;
  
  private Map<String, StreamFn> streamFns = new ConcurrentHashMap<String, StreamFn>();
  
  public StreamImpl(String streamName) {
    this.streamName = streamName; 
  }
  
  public void initStream() throws StreamInitException {
    
    // Get Cache handle to create stream's root region.
    try {
    Cache cache = StreamManager.getInstance().getCache();
        
    // Create async queue.
    AsyncEventQueueFactory factory = cache.createAsyncEventQueueFactory();
    // Enable persistence based on user confg.
    factory.setPersistent(false);
    //factory.setDiskStoreName("exampleStore");
    factory.setParallel(false);
    AsyncEventListener listener = new StreamListener(this);
    AsyncEventQueueImpl asyncQueue = (AsyncEventQueueImpl)factory.create(streamName + "_AQ", listener);
    
    // Create stream root region with async queue.
    this.streamRegion = cache.createRegionFactory(RegionShortcut.REPLICATE_PROXY).addAsyncEventQueueId(asyncQueue.getId()).create(streamName);    
    } catch (Exception ex) {
      throw new StreamInitException("Failed to initialize the stream. " + ex.getMessage(),  ex);
    }
  }
  
  public void streamData(Object data) {
    this.streamRegion.put(this.streamName, data);
  }
  
  public void link(String fnName, StreamFn streamFn) {
    streamFns.put(fnName, streamFn);
  }

  public void unlink(String fnName) {
    streamFns.remove(fnName);
  }

  public StreamFn[] getStreamFns() {
    return (StreamFn[])streamFns.values().toArray(new StreamFn[streamFns.size()]);
  }

}