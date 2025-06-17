package vladek.lab3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab3.dto.Request;
import vladek.lab3.services.IDecryptService;

@RestController
@RequestMapping("/api/lab3/decrypt")
public class DecryptController {
    @Autowired
    private IDecryptService decryptService;

    @PostMapping("/aes-128")
    public ResponseEntity<String> decryptFromAES128(@RequestBody Request request) {
        String result = decryptService.decryptFromAES128(request.getText(), request.getKey(), request.getKeyType());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
