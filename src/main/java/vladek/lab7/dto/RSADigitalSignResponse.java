package vladek.lab7.dto;

import lombok.Data;

@Data
public class RSADigitalSignResponse {
    private String hashCode;
    private String sign;
}
