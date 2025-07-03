package vladek.lab5.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class PrimitiveRootsResponse {
    private List<BigInteger> roots;
    private double seconds;
}
