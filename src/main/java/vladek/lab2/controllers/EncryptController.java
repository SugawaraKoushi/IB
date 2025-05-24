package vladek.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab2.dto.Request;
import vladek.lab2.services.IEncryptService;

@RestController("encryptController2")
@RequestMapping("api/lab2/encrypt")
public class EncryptController {
    @Autowired
    private IEncryptService encryptService;

    @PostMapping("/rail-fence")
    public ResponseEntity<String> encryptByRailFenceCipher(@RequestBody Request request) {
        String result = encryptService.encryptByRailFenceCipher(request.getText());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/with-key")
    public ResponseEntity<String> ecnryptByChangeCipherWithKey(@RequestBody Request request) {
        String result = encryptService.encryptByChangeCipherWithKey(request.getText(), request.getKey());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
