package vladek.lab5.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab5.dto.PrimitiveRootsResponse;
import vladek.lab5.services.PrimitiveRootsService;

import java.math.BigInteger;
import java.util.Random;

@RestController
@RequestMapping("/api/lab5/primitive-roots")
@RequiredArgsConstructor
public class PrimitiveRootsController {
    private final PrimitiveRootsService primitiveRootsService;

    @GetMapping("/get-roots")
    public ResponseEntity<PrimitiveRootsResponse> getPrimitiveRoots(@RequestParam int type, @RequestParam String value) {
        BigInteger n = new BigInteger(value, type);
        PrimitiveRootsResponse response = primitiveRootsService.findPrimitiveRoots(n, 100);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-random-root")
    public ResponseEntity<String> getRandomPrimitiveRoot(@RequestParam String value) {
        BigInteger n = new BigInteger(value, 16);
        PrimitiveRootsResponse response = primitiveRootsService.findPrimitiveRoots(n, 100000);
        Random random = new Random();
        BigInteger root = response.getRoots().get(random.nextInt(response.getRoots().size()));
        return new ResponseEntity<>(root.toString(16), HttpStatus.OK);
    }
}
