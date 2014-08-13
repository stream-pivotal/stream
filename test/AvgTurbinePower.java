import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Random;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

public class AvgTurbinePower implements DataSerializable {
  
  public int num;

  public Long total;
  
  public long AvgPower;

  public String tId;
  
  public AvgTurbinePower () {
  }

  public AvgTurbinePower (String tId, Long total) {
    this.tId = tId;
    this.total = total;
  }
  

  @Override
  public String toString() {
    return "AvgTurbinePower [num=" + num + ", total=" + total + ", AvgPower="
        + AvgPower + ", tId=" + tId + "]";
  }

  @Override
  public void fromData(DataInput in) throws IOException,
      ClassNotFoundException {
    this.tId = DataSerializer.readString(in);
    this.AvgPower = in.readLong();
    this.total = in.readLong();
    this.num = in.readInt();
  }

  @Override
  public void toData(DataOutput out) throws IOException {
    DataSerializer.writeString(this.tId, out);
    out.writeLong(this.AvgPower);
    out.writeLong(this.total);
    out.writeInt(this.num);
  }
  
}