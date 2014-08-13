import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Random;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

public class TurbineData implements DataSerializable {
  
  private static Random randomTurbineData = new Random();
  
  public String id;

  public int rpm;

  public int watt;

  public TurbineData () {
    this.id = "" + randomTurbineData.nextInt(5);
    this.watt = randomTurbineData.nextInt(10);
    this.rpm = randomTurbineData.nextInt(10);
  }

  @Override
  public String toString() {
    return "TurbineData [id=" + id + ", rpm=" + rpm + ", watt=" + watt + "]";
  }

  @Override
  public void fromData(DataInput in) throws IOException,
      ClassNotFoundException {
    this.id = DataSerializer.readString(in);
    this.rpm = in.readInt();
    this.watt = in.readInt();
  }

  @Override
  public void toData(DataOutput out) throws IOException {
    DataSerializer.writeString(this.id, out);
    out.writeInt(this.rpm);
    out.writeInt(this.watt);
  }
  
}