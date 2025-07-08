package vladek.lab6.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class RSAPrivateKey {
    private BigInteger d;
    private BigInteger n;
}
