package vladek.lab1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab1.dto.Request;
import vladek.lab1.services.IEncryptService;

@RestController("encryptController1")
@RequestMapping("api/lab1/encrypt")
public class EncryptController {
    @Autowired
    private IEncryptService encryptService;

    @PostMapping("/additive")
    public ResponseEntity<String> encryptByAdditiveCipher(@RequestBody Request request) {
        String result = encryptService.encryptByAdditiveCipherWithShift(request.getText(), request.getShift());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/multiplicative")
    public ResponseEntity<String> encryptByMultiplicativeCipher(@RequestBody Request request) {
        String result = encryptService.encryptByMultiplicativeCipherWithShift(request.getText(), request.getShift());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/playfair")
    public ResponseEntity<String> encryptByPlayfairCipher(@RequestBody Request request) {
        String result = encryptService.encryptByPlayfairCipher(request.getText(), request.getKey());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
