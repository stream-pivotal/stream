import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.cache.Scope;
import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.AttributesFactory;
import com.gemstone.gemfire.cache.EvictionAttributes;
import com.gemstone.gemfire.cache.EvictionAction;
import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.CacheWriterException;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.query.Query;
import com.gemstone.gemfire.cache.query.QueryService;
import com.gemstone.gemfire.cache.query.SelectResults;
import com.gemstone.gemfire.cache.query.IndexType;
import com.gemstone.gemfire.cache.query.Struct;
import com.gemstone.gemfire.cache.server.CacheServer;
import com.gemstone.gemfire.distributed.DistributedSystem;
import com.gemstone.gemfire.internal.NanoTimer;
import com.gemstone.gemfire.stream.*;

/**
 * In this example the server listens on a port for client requests. 
 * 
 */
public class Server {
  
  private static final BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));
	
  public static void main(String [] args) throws Exception {
    System.out.println("\nThis program is a server, listening on port " + 40304 + " for client requests.");

    // Connect to the GemFire distributed system
    Properties props = new Properties();
    props.setProperty("name", "Server");
    props.setProperty("log-level", "fine");
    props.setProperty("log-file", "server.log");
    System.out.println("\nConnecting to the distributed system and creating the cache.");
    DistributedSystem ds = DistributedSystem.connect(props);
    Cache cache = CacheFactory.create(ds);

    Server myServer = new Server();
    
    myServer.demoStream();
    
    // Close the cache and disconnect from GemFire distributed system
    
    System.out.println("Enter to Close the cache.");
    stdinReader.readLine();
    myServer.executeQuery(cache);
    cache.close();
    ds.disconnect();
  }

  public void demoStream() {
    Stream turbineStream = null;
    try {
      turbineStream = StreamService.createStream("TurbineDataAnalyzer");
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    turbineStream.link("TurbineData", new TurbineAnalyzer());
    for (int i=0; i < 10; i++) {
      TurbineData tData = new TurbineData();
      System.out.println(tData.toString());
      turbineStream.streamData(tData);
    }
    
  }
    
  public void executeQuery(Cache cache) {
    String[] queryStr = new String[] {
        "SELECT e.key, e.value FROM /TurbineData.entries e",
        "SELECT e.key, e.value FROM /AvgPowerCalculator.entries e",
    };

    QueryService cacheQS = cache.getQueryService();
    for (int i=0; i< queryStr.length; i++) {
      try {
        Query query = cacheQS.newQuery(queryStr[i]);
        System.out.println("\nExecuting Query on Stream Region. Query :" + queryStr[i]);
        SelectResults results = (SelectResults)query.execute();
        for (Object o : results.asList()) {
          System.out.println(o);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  
}

