package vladek.lab2.dto;

import lombok.Data;

@Data
public class Request {
    private String text;
    private int shift;
    private String key;
}
