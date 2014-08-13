import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionShortcut;
import com.gemstone.gemfire.stream.StreamFn;

public class TurbineAnalyzer implements StreamFn {

  private String fnName = "TurbineData";
  
  private StreamFn[] streamFn = new StreamFn[] {new AvgPowerCalculator()};
  
  public StreamFn[] getStreamFns() {
    return streamFn;   
  }

  public Object execute(Object streamedData) {
    if (!(streamedData instanceof TurbineData)) {
      return null;  
    }
    TurbineData tData = (TurbineData)streamedData;
    Region resultRegion = getResultRegion();
    Long old = (Long)resultRegion.get(tData.id);
    Long total;
    //System.out.println("Watts produced :" + old);
    if (old == null) {
      total = new Long(tData.watt);
      resultRegion.put(tData.id, total);
    } else {
      // Add new value.
      total = new Long(old.longValue() + tData.watt);
      resultRegion.put(tData.id, total);
    }
    AvgTurbinePower atp = new AvgTurbinePower(tData.id, total);
    //System.out.println("AvgTurbinePower" + atp);
    return atp;
  }

  private Region getResultRegion() {
    Cache cache = CacheFactory.getAnyInstance();
    if (cache == null) {
      cache = new CacheFactory().create();
    }
    
    // Create stream root region with async queue.
    Region resultRegion = cache.getRegion(fnName); 
    if ( resultRegion == null) {
      resultRegion = cache.createRegionFactory(RegionShortcut.REPLICATE).create(fnName);
    }
    return resultRegion;
  }
  
}