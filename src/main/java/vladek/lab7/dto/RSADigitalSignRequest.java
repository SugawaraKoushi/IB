package vladek.lab7.dto;

import lombok.Data;
import vladek.lab6.dto.RSAPublicKey;

@Data
public class RSADigitalSignRequest {
    private RSAPublicKey publicKey;
    private String message;
}
