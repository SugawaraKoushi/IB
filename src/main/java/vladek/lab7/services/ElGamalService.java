package vladek.lab7.services;

import com.fasterxml.jackson.databind.node.BigIntegerNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vladek.lab4.services.PrimeNumbersService;
import vladek.lab7.dto.ElGamalKeys;
import vladek.lab7.dto.ElGamalSign;

import java.math.BigInteger;
import java.util.HashSet;
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

    public ElGamalSign getSign(BigInteger g, BigInteger k, BigInteger p, BigInteger x, BigInteger hashCode) {
        BigInteger a = g.modPow(k, p);
        BigInteger b = hashCode.subtract(x.multiply(a)).divide(k).mod(p.subtract(BigInteger.ONE));
        ElGamalSign sign = new ElGamalSign();
        sign.setA(a.toString(16));
        sign.setB(b.toString(16));
        sign.setHashCode(hashCode.toString(16));
        return sign;
    }
}
