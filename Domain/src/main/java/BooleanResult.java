import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.Future;

@AllArgsConstructor
@Data
public class BooleanResult implements Serializable {
    public Future<Boolean> result;
}
