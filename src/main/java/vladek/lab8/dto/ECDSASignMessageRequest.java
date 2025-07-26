package vladek.lab8.dto;

import lombok.Data;

@Data
public class ECDSASignMessageRequest {
    String message;
    String x;
    String qx;
    String qy;
    String a;
    String p;
    String n;
}
