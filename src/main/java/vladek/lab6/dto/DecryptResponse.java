package vladek.lab6.dto;

import lombok.Data;

@Data
public class DecryptResponse {
    private String decryptedText;
    private String zip;
}
