package vladek.lab5.services;

import org.springframework.stereotype.Service;
import vladek.lab5.dto.DiffieHellmanKeys;

import java.math.BigInteger;

@Service
public class KeyExchangeService {
    public DiffieHellmanKeys getDiffieHellmanKeys(BigInteger x1, BigInteger x2, BigInteger n, BigInteger g) {
        BigInteger y1 = g.modPow(x1, n);
        BigInteger y2 = g.modPow(x2, n);

        BigInteger k1 = y2.modPow(x1, n);
        BigInteger k2 = y1.modPow(x2, n);

        DiffieHellmanKeys keys = new DiffieHellmanKeys();
        keys.setKeyAlice(k1);
        keys.setKeyBob(k2);

        return keys;
    }
}
