package vladek.lab4.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vladek.lab4.dto.PrimeTestResult;
import vladek.lab4.services.PrimeNumbersService;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/lab4/prime-numbers")
@RequiredArgsConstructor
public class PrimeNumbersController {
    private final PrimeNumbersService primeNumbersService;

    @GetMapping("/fermat-test")
    public ResponseEntity<PrimeTestResult> fermatTest(@RequestParam long n) {
        PrimeTestResult result = primeNumbersService.fermatTest(n);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/miller-rabin-test")
    public ResponseEntity<PrimeTestResult> millerRabinTest(@RequestParam long n, @RequestParam int k) {
        PrimeTestResult result = primeNumbersService.millerRabinTest(new BigInteger(String.valueOf(n)), k);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/division-test")
    public ResponseEntity<PrimeTestResult> divisionTest(@RequestParam long n) {
        PrimeTestResult result = primeNumbersService.divisionTest(n);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/generate-prime-number")
    public ResponseEntity<String> generatePrimeNumber(@RequestParam int bits, @RequestParam int rounds) {
        BigInteger n = primeNumbersService.getRandomPrimeNumber(bits, rounds);
        String result = n.toString().concat("n");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/generate-hex-prime-number")
    public ResponseEntity<String> generateHexPrimeNumber(@RequestParam int bits, @RequestParam int rounds) {
        BigInteger n = primeNumbersService.getRandomPrimeNumber(bits, rounds);
        String result = n.toString(16);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
