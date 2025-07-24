package vladek.lab8.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ECDSASign {
    private String r;
    private String s;
}
