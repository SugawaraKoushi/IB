package vladek.lab1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab1.dto.Request;
import vladek.lab1.services.IDecryptService;

@RestController
@RequestMapping("api/lab1/decrypt")
public class DecryptController {
    @Autowired
    private IDecryptService decryptService;

    @PostMapping("/additive")
    public ResponseEntity<String> decryptFromAdditiveCipher(@RequestBody Request request) {
        String result = decryptService.decryptFromAdditiveCipherWithShift(request.getText(), request.getShift());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/multiplicative")
    public ResponseEntity<String> decryptFromMultiplicativeCipher(@RequestBody Request request) {
        String result = decryptService.decryptFromMultiplicativeCipherWithShift(request.getText(), request.getShift());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
