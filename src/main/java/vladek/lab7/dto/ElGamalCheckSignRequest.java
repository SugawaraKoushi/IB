package vladek.lab7.dto;

import lombok.Data;

@Data
public class ElGamalCheckSignRequest {
    private String g;
    private String hashCode;
    private String y;
    private String a;
    private String b;
    private String p;
}
