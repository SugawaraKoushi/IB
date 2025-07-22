package vladek.lab7.dto;

import lombok.Data;

@Data
public class ElGamalSignResponse {
    private String a;
    private String b;
    private String hashCode;
}
