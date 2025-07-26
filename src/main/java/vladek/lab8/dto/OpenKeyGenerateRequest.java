package vladek.lab8.dto;

import lombok.Data;

@Data
public class OpenKeyGenerateRequest {
    private String qx;
    private String qy;
    private String x;
    private String a;
    private String p;
}
