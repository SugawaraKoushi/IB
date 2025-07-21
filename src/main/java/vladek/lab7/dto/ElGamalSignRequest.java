package vladek.lab7.dto;

import lombok.Data;

@Data
public class ElGamalSignRequest {
    private String g;
    private String k;
    private String p;
    private String x;
    private String message;
}
