package vladek.lab8.services;

import org.springframework.stereotype.Service;
import vladek.lab8.dto.ECDSACheckSign;
import vladek.lab8.dto.ECDSAPoint;
import vladek.lab8.dto.ECDSASign;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
public class ECDSAService {
    // уравнение кривой y^2 = x^3 - 3x + b (mod p), где a = -3 (NIST P-256)

    private static final BigInteger P = new BigInteger(
            "115792089210356248762697446949407573530086143415290314195533631308867097853951"
    );
    private static final BigInteger N = new BigInteger(
            "115792089210356248762697446949407573529996955224135760342422259061068512044369"
    );
    private static final BigInteger A = BigInteger.valueOf(-3);
    private static final BigInteger B = new BigInteger(
                "5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b", 16
    );
    private static final ECDSAPoint Q = new ECDSAPoint(
            new BigInteger(
                    "6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296",
                    16),
            new BigInteger(
                    "4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5",
                    16)
    );


    /**
     * Генерирует секретный ключ в диапазоне 1 < x < n - 1
     */
    public BigInteger generateSecretKey(BigInteger n) {
        SecureRandom random = new SecureRandom();
        BigInteger x;

        do {
            x = new BigInteger(n.bitLength(), random);
        } while (x.compareTo(BigInteger.ONE) <= 0 || x.compareTo(n.subtract(BigInteger.ONE)) > 0);

        return x;
    }

    /**
     * Генерирует открытый ключ
     *
     * @param x секретный ключ
     */
    public ECDSAPoint generateOpenKey(ECDSAPoint Q, BigInteger x, BigInteger a, BigInteger p) {
        return Q.multiply(x, a, p);
    }

    /**
     * Генерирует число k в диапазоне 1 <= k <= n
     */
    private BigInteger generateK(BigInteger n) {
        SecureRandom random = new SecureRandom();
        BigInteger k;

        do {
            k = new BigInteger(n.bitLength(), random);
        } while (k.compareTo(BigInteger.ONE) < 0 || n.compareTo(N) > 0);

        return k;
    }

    /**
     * Получение подписи ECDSA
     *
     * @param hashCode хэшированное сообщение через SHA-256
     */
    public ECDSASign sign(BigInteger hashCode, BigInteger x, ECDSAPoint q, BigInteger a, BigInteger p, BigInteger n) {
        BigInteger k = generateK(n);
        ECDSAPoint r = q.multiply(k, a, p);

        BigInteger kInv = k.modInverse(n);
        BigInteger s = hashCode.add(x.multiply(r.getX())).multiply(kInv).mod(n);

        ECDSASign sign = new ECDSASign();
        sign.setR(r.getX().toString(16));
        sign.setS(s.toString(16));
        return sign;
    }

    /**
     * Проверка подписи ECDSA
     */
    public ECDSACheckSign checkSign(ECDSAPoint p, ECDSAPoint q, ECDSASign sign, BigInteger hashCode, BigInteger n, BigInteger a, BigInteger pMod) {
        BigInteger s = new BigInteger(sign.getS(), 16);
        BigInteger w = s.modInverse(n);

        BigInteger u1 = hashCode.multiply(w).mod(n);
        BigInteger r = new BigInteger(sign.getR(), 16);
        BigInteger u2 = r.multiply(w).mod(n);

        ECDSAPoint v = q.multiply(u1, a, pMod).add(p.multiply(u2, a, pMod), a, pMod);

        ECDSACheckSign checkSign = new ECDSACheckSign();
        checkSign.setR(r.toString(16));
        checkSign.setV(v.getX().toString(16));
        return checkSign;
    }
}
