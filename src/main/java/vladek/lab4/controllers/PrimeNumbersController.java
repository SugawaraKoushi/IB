package vladek.lab4.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vladek.lab4.services.PrimeNumbersService;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/lab4/prime-numbers")
@RequiredArgsConstructor
public class PrimeNumbersController {
    private final PrimeNumbersService primeNumbersService;

    @GetMapping("/fermat-test")
    public ResponseEntity<String> fermatTest(@RequestParam long n) {
        String result = primeNumbersService.fermatTest(n);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/miller-rabin-test")
    public ResponseEntity<String> millerRabinTest(@RequestParam long n, @RequestParam int k) {
        String result = primeNumbersService.millerRabinTest(n, k);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/division-test")
    public ResponseEntity<String> divisionTest(@RequestParam long n) {
        String result = primeNumbersService.divisionTest(n);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/generate-prime-number")
    public ResponseEntity<String> generatePrimeNumber(@RequestParam int bits, @RequestParam int rounds) {
        BigInteger bigInt;

        do {
            bigInt = primeNumbersService.generatePrimeNumber(bits, rounds);
        } while (bigInt.equals(new BigInteger("-1")));

        String result = bigInt.toString().concat("n");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
