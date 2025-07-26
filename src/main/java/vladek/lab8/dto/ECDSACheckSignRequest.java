package vladek.lab8.dto;

import lombok.Data;

@Data
public class ECDSACheckSignRequest {
    String px;
    String py;
    String qx;
    String qy;
    String r;
    String s;
    String message;
    String n;
    String a;
    String p;
}
