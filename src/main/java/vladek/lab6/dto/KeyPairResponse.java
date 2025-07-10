package vladek.lab6.dto;

import lombok.Data;

@Data
public class KeyPairResponse {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private String zipFile;
}
