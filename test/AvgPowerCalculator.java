import java.util.Collections;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionShortcut;
import com.gemstone.gemfire.stream.StreamFn;

public class AvgPowerCalculator implements StreamFn {

  private String fnName = "AvgPowerCalculator";
    
  public StreamFn[] getStreamFns() {
    return (StreamFn[])Collections.EMPTY_LIST.toArray();
  }

  public Object execute(Object streamedData) {
    //System.out.println("Executing AvgPowerCalculaor Fn...");
    if (!(streamedData instanceof AvgTurbinePower)) {
      return null;  
    }

    AvgTurbinePower sData = (AvgTurbinePower)streamedData;
    //System.out.println("Executing AvgPowerCalculaor Fn..." + sData);
    Region resultRegion = getResultRegion();
    AvgTurbinePower avgData = (AvgTurbinePower)resultRegion.get(sData.tId);
    try {
      //System.out.println("Watts produced :" + old);
      if (avgData == null) {
        avgData = new AvgTurbinePower(sData.tId, sData.total);
      } else {
        avgData.total = sData.total;
      }
      avgData.num++;

      avgData.AvgPower = ((avgData.total).longValue() / (avgData.num));
      resultRegion.put(sData.tId, avgData);
    } catch (Exception ex) {
      System.out.println("Error in AvgPowerCalculator :" + ex.getMessage());
    }
    return avgData;
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