import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.Future;

@AllArgsConstructor
@Data
public class BooleanResult implements Serializable {
    public Boolean result;
    public Boolean stopping;

}
