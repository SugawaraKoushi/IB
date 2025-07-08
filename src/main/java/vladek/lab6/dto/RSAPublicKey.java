package vladek.lab6.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class RSAPublicKey {
    BigInteger e;
    BigInteger n;
}
