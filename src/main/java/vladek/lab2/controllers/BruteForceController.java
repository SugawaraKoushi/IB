package vladek.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab2.dto.BruteForceResponse;
import vladek.lab2.dto.Request;
import vladek.lab2.services.IBruteForceService;

@RestController
@RequestMapping("/api/lab2/brute-force")
public class BruteForceController {
    @Autowired
    private IBruteForceService bruteForceService;

    @PostMapping("/combine")
    public ResponseEntity<BruteForceResponse> bruteForceCombineCipher(@RequestBody Request request) {
        BruteForceResponse response = bruteForceService.bruteForceCombineCipher(request.getText(), request.getKeys()[0]);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
