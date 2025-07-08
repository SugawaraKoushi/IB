package vladek.lab6.dto;

import lombok.Data;

@Data
public class RSAKeyPair {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
}
