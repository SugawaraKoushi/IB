package vladek.lab6.dto;

import lombok.Data;

@Data
public class EncryptResponse {
    private String encryptedKey;
    private String encryptedText;
    private String zip;
}
