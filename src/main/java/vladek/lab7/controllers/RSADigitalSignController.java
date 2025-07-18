package vladek.lab7.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vladek.lab6.dto.RSAKeyPair;
import vladek.lab6.services.RSAService;
import vladek.lab7.dto.RSADigitalSignCheckRequest;
import vladek.lab7.dto.RSADigitalSignRequest;
import vladek.lab7.dto.RSADigitalSignResponse;
import vladek.lab7.services.RSADigitalSignService;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/lab7/rsa")
@RequiredArgsConstructor
public class RSADigitalSignController {
    private final RSAService rsaService;
    private final RSADigitalSignService rsaDigitalSignService;

    @GetMapping("/get-keys")
    public ResponseEntity<RSAKeyPair> getRSAKeys(@RequestParam String p, @RequestParam String q) {
        BigInteger pBig = new BigInteger(p, 16);
        BigInteger qBig = new BigInteger(q, 16);
        RSAKeyPair keyPair = rsaService.keyGeneration(pBig, qBig);
        return new ResponseEntity<>(keyPair, HttpStatus.OK);
    }

    @PostMapping("/sign")
    public ResponseEntity<RSADigitalSignResponse> signMessageWithRSA(@RequestBody RSADigitalSignRequest request) {
        RSADigitalSignResponse response = rsaDigitalSignService.getSign(request.getMessage(), request.getPublicKey());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/check-sign")
    public ResponseEntity<String> checkSign(@RequestBody RSADigitalSignCheckRequest request) {
        String hashCode = rsaDigitalSignService.checkSign(request.getSign(), request.getPrivateKey());
        return new ResponseEntity<>(hashCode, HttpStatus.OK);
    }
}
