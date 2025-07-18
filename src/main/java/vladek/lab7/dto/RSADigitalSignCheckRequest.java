package vladek.lab7.dto;

import lombok.Data;
import vladek.lab6.dto.RSAPrivateKey;

@Data
public class RSADigitalSignCheckRequest {
    private String sign;
    private RSAPrivateKey privateKey;
}
