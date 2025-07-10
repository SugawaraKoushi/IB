package vladek.lab6.dto;

import lombok.Data;

@Data
public class EncryptRequest {
    private String text;
    private RSAPublicKey rsaPublicKey;
    private String aesKey;
}
