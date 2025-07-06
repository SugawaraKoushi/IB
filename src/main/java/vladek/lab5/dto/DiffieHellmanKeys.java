package vladek.lab5.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class DiffieHellmanKeys {
    BigInteger keyAlice;
    BigInteger keyBob;
}
