package vladek.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab2.dto.Request;
import vladek.lab2.services.IDecryptService;

@RestController("decryptController2")
@RequestMapping("/api/lab2/decrypt")
public class DecryptController {
    @Autowired
    private IDecryptService decryptService;

    @PostMapping("/rail-fence")
    public ResponseEntity<String> decryptFromRailFenceCipher(@RequestBody Request request) {
        String result = decryptService.decryptFromRailFenceCipher(request.getText());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
