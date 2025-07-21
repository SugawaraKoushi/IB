package vladek.lab7.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vladek.lab7.dto.ElGamalKeys;
import vladek.lab7.dto.ElGamalSign;
import vladek.lab7.dto.ElGamalSignRequest;
import vladek.lab7.services.ElGamalService;
import vladek.lab7.services.SHA256Service;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/lab7/el-gamal")
@RequiredArgsConstructor
public class ElGamalController {
    private final ElGamalService elGamalService;
    private final SHA256Service sha256Service;

    @GetMapping("/get-keys")
    public ResponseEntity<ElGamalKeys> getElGamalKeys(@RequestParam String p, @RequestParam String a) {
        BigInteger pBigInt = new BigInteger(p, 16);
        BigInteger aBigInt = new BigInteger(a, 16);
        ElGamalKeys keys = elGamalService.keyGeneration(pBigInt, aBigInt);
        return new ResponseEntity<>(keys, HttpStatus.OK);
    }

    @GetMapping("/get-k")
    public ResponseEntity<String> getK(@RequestParam String p) {
        BigInteger pBigInt = new BigInteger(p, 16);
        BigInteger k = elGamalService.generateK(pBigInt);
        return new ResponseEntity<>(k.toString(16), HttpStatus.OK);
    }

    @PostMapping("/sign")
    public ResponseEntity<ElGamalSign> sign(@RequestBody ElGamalSignRequest request) {
        byte[][] blocks = sha256Service.prepareMessageBlocks(request.getMessage());
        byte[] hashCodeBytes = sha256Service.getHashCode(blocks);
        String hashCode = Hex.encodeHexString(hashCodeBytes);

        BigInteger gBigInt = new BigInteger(request.getG(), 16);
        BigInteger kBigInt = new BigInteger(request.getK(), 16);
        BigInteger pBigInt = new BigInteger(request.getP(), 16);
        BigInteger xBigInt = new BigInteger(request.getX(), 16);
        BigInteger hashCodeBigInt = new BigInteger(hashCode, 16);

        ElGamalSign sign = elGamalService.getSign(gBigInt, kBigInt, pBigInt, xBigInt, hashCodeBigInt);
        return new ResponseEntity<>(sign, HttpStatus.OK);
    }
}
