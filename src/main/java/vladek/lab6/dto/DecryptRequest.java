package vladek.lab6.dto;

import lombok.Data;

@Data
public class DecryptRequest {
    private String text;
    private RSAPrivateKey key;
}
