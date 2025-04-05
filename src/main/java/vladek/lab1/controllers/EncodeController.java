package vladek.lab1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab1.dto.Request;
import vladek.lab1.services.IEncodeService;

@RestController
@RequestMapping("/api/lab1/encode")
public class EncodeController {
    @Autowired
    private IEncodeService encodeService;

    @PostMapping("/additive")
    public ResponseEntity<String> encodeByAdditiveCipher(@RequestBody Request request) {
        String result = encodeService.encodeByAdditiveCipherWithShift(request.getText(), request.getShift());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
