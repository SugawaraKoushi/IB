package vladek.lab8.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vladek.lab7.services.SHA256Service;
import vladek.lab8.dto.*;
import vladek.lab8.services.ECDSAService;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/lab8/ecdsa")
@RequiredArgsConstructor
public class ECDSAController {
    private final ECDSAService ecdsaService;
    private final SHA256Service sha256Service;

    @GetMapping("/secret-key")
    public ResponseEntity<String> generateSecretKey(@RequestParam String n) {
        BigInteger nBigInt = new BigInteger(n);
        BigInteger x = ecdsaService.generateSecretKey(nBigInt);
        return new ResponseEntity<>(x.toString(16), HttpStatus.OK);
    }

    @PostMapping("/open-key")
    public ResponseEntity<ECDSAPointDto> generateOpenKey(@RequestBody OpenKeyGenerateRequest request) {
        ECDSAPoint q = new ECDSAPoint(
                new BigInteger(request.getQx(), 16),
                new BigInteger(request.getQy(), 16)
        );
        BigInteger x = new BigInteger(request.getX(), 16);
        BigInteger a = new BigInteger(request.getA());
        BigInteger pMod = new BigInteger(request.getP());
        ECDSAPoint p = ecdsaService.generateOpenKey(q, x, a, pMod);
        ECDSAPointDto result = new ECDSAPointDto();
        result.setX(p.getX().toString(16));
        result.setY(p.getY().toString(16));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/sign")
    public ResponseEntity<ECDSASign> signMessage(@RequestBody ECDSASignMessageRequest request) {
        byte[][] blocks = sha256Service.prepareMessageBlocks(request.getMessage());
        byte[] hashCodeBytes = sha256Service.getHashCode(blocks);
        BigInteger hashCode = new BigInteger(1, hashCodeBytes);

        ECDSAPoint q = new ECDSAPoint(
                new BigInteger(request.getQx(), 16),
                new BigInteger(request.getQy(), 16)
        );

        BigInteger x = new BigInteger(request.getX(), 16);
        BigInteger a = new BigInteger(request.getA());
        BigInteger pMod = new BigInteger(request.getP());
        BigInteger n = new BigInteger(request.getN());

        ECDSASign sign = ecdsaService.sign(hashCode, x, q, a, pMod, n);
        return new ResponseEntity<>(sign, HttpStatus.OK);
    }

    @PostMapping("/check-sign")
    public ResponseEntity<ECDSACheckSign> checkSign(@RequestBody ECDSACheckSignRequest request) {
        ECDSAPoint p = new ECDSAPoint(
                new BigInteger(request.getPx(), 16),
                new BigInteger(request.getPy(), 16)
        );
        ECDSAPoint q = new ECDSAPoint(
                new BigInteger(request.getQx(), 16),
                new BigInteger(request.getQy(), 16)
        );

        ECDSASign sign = new ECDSASign();
        sign.setR(request.getR());
        sign.setS(request.getS());

        byte[][] blocks = sha256Service.prepareMessageBlocks(request.getMessage());
        byte[] hashCodeBytes = sha256Service.getHashCode(blocks);
        BigInteger hashCode = new BigInteger(1, hashCodeBytes);

        BigInteger n = new BigInteger(request.getN());
        BigInteger a = new BigInteger(request.getA());
        BigInteger pMod = new BigInteger(request.getP());

        ECDSACheckSign check = ecdsaService.checkSign(p, q, sign, hashCode, n, a, pMod);
        return new ResponseEntity<>(check, HttpStatus.OK);
    }
}
