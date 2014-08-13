/*=========================================================================
 * Copyright (c) 2002-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */

package com.gemstone.gemfire.stream.internal;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.stream.Stream;
import com.gemstone.gemfire.stream.StreamExistsException;

public class StreamManager {
  
  private static StreamManager instance;
  
  private Map<String, Stream> streams = new ConcurrentHashMap<String, Stream>();
  
  public static StreamManager getInstance() {
    if (instance == null) {
      instance = new StreamManager();
    }
    return instance;
  }

  public Cache getCache() {
    Cache cache = CacheFactory.getAnyInstance();
    if (cache == null) {
      cache = new CacheFactory().create();
    }
    return cache;
  }

  public Stream createStream(String streamName) throws StreamExistsException {
    // Check if stream with given name already exists.
    if (streams.containsKey(streamName)) {
      throw new StreamExistsException("Stream with given name already exists");
    }
    
    Stream s = new StreamImpl(streamName);
    streams.put(streamName, s);
    return s;
  }
  
  public Stream getStream(String streamName) {
    return streams.get(streamName);
  }

  public Collection<Stream> getAllStreams() {
    return streams.values();
  }
  
}