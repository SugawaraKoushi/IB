package vladek.lab4.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vladek.lab4.services.PrimeNumbersService;

@RestController
@RequestMapping("/api/lab4/prime-numbers")
@RequiredArgsConstructor
public class PrimeNumbersController {
    private final PrimeNumbersService primeNumbersService;

    @GetMapping("/euler-test/{n}")
    public ResponseEntity<String> eulerTest(@PathVariable long n) {
        String result = primeNumbersService.eulerTest(n);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
