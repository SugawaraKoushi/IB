package vladek.lab5.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab5.dto.DiffieHellmanKeys;
import vladek.lab5.services.KeyExchangeService;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/lab5/key-exchange")
@RequiredArgsConstructor
public class KeyExchangeController {
    private final KeyExchangeService keyExchangeService;

    @GetMapping("/diffie-hellman")
    public ResponseEntity<DiffieHellmanKeys> getDiffieHellmanKeys(
            @RequestParam String x1, @RequestParam String x2, @RequestParam String g, @RequestParam String n
    ) {
        BigInteger bigX1 = new BigInteger(x1);
        BigInteger bigX2 = new BigInteger(x2);
        BigInteger bigG = new BigInteger(g);
        BigInteger bigN = new BigInteger(n);
        DiffieHellmanKeys keys = keyExchangeService.getDiffieHellmanKeys(bigX1, bigX2, bigG, bigN);
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }
}
