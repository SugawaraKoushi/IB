package vladek.lab7.services;

import org.springframework.stereotype.Service;
import vladek.lab7.dto.ElGamalKeys;

import java.math.BigInteger;
import java.util.Random;

@Service
public class ElGamalService {
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
}
