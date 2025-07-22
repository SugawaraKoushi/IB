package vladek.lab7.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vladek.lab4.services.PrimeNumbersService;
import vladek.lab7.dto.ElGamalCheckSignResponse;
import vladek.lab7.dto.ElGamalKeys;
import vladek.lab7.dto.ElGamalSignResponse;

import java.math.BigInteger;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ElGamalService {
    private final PrimeNumbersService primeNumbersService;

    public ElGamalKeys keyGeneration(BigInteger p, BigInteger a) {
        Random random = new Random();
        BigInteger x = new BigInteger(p.clearBit(0).bitLength(), random);
        BigInteger y = a.modPow(x, p);
        ElGamalKeys keys = new ElGamalKeys();
        keys.setP(p.toString(16));
        keys.setX(x.toString(16));
        keys.setY(y.toString(16));
        return keys;
    }

    public BigInteger generateK(BigInteger p) {
        BigInteger k;
        BigInteger gcd;

        do {
            k = primeNumbersService.getRandomPrimeNumber(p.bitLength() - 1, 20);
            gcd = k.gcd(p.subtract(BigInteger.ONE));
        } while (!gcd.equals(BigInteger.ONE));

        return k;
    }

    public ElGamalSignResponse getSign(BigInteger g, BigInteger k, BigInteger p, BigInteger x, BigInteger hashCode) {
        BigInteger a = g.modPow(k, p);
        BigInteger kInv = k.modInverse(p.subtract(BigInteger.ONE));
        BigInteger b = hashCode.subtract(x.multiply(a)).multiply(kInv).mod(p.subtract(BigInteger.ONE));
        ElGamalSignResponse sign = new ElGamalSignResponse();
        sign.setA(a.toString(16));
        sign.setB(b.toString(16));
        sign.setHashCode(hashCode.toString(16));
        return sign;
    }

    public ElGamalCheckSignResponse checkSign(
            BigInteger g, BigInteger hashCode, BigInteger y, BigInteger a, BigInteger b, BigInteger p) {
        BigInteger left = g.modPow(hashCode, p);
        BigInteger right = y.modPow(a, p).multiply(a.modPow(b, p)).mod(p);
        ElGamalCheckSignResponse check = new ElGamalCheckSignResponse();
        check.setLeft(left.toString(16));
        check.setRight(right.toString(16));
        return check;
    }
}
