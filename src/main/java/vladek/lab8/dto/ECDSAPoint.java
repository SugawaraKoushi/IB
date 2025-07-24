package vladek.lab8.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Data
@RequiredArgsConstructor
public class ECDSAPoint {
    private final BigInteger x;
    private final BigInteger y;

    /**
     * Сложение двух точек на эллиптической кривой.
     *
     * @param p    точка для сложения
     * @param a    коэффициент кривой
     * @param pMod модуль
     */
    public ECDSAPoint add(ECDSAPoint p, BigInteger a, BigInteger pMod) {
        if (p == null) {
            return this;  // P + O = P
        }

        if (this.isInfinity()) {
            return p;  // O + Q = Q
        }

        // Если точки одинаковые → удвоение
        if (x.equals(p.getX())) {
            if (y.equals(p.getY())) {
                return doublePoint(a, pMod);
            } else {
                return null;  // P + (-P) = O (бесконечность)
            }
        }

        // Сложение разных точек
        BigInteger lambda = p.getY().subtract(y)
                .multiply(p.getX().subtract(x).modInverse(pMod))
                .mod(pMod);

        BigInteger newX = lambda.pow(2).subtract(x).subtract(p.getX()).mod(pMod);
        BigInteger newY = lambda.multiply(x.subtract(newX)).subtract(y).mod(pMod);

        return new ECDSAPoint(newX, newY);
    }

    /**
     * Удвоение точки (P + P).
     *
     * @param a    коэффициент кривой
     * @param pMod модуль
     * @return новая точка или null
     */
    public ECDSAPoint doublePoint(BigInteger a, BigInteger pMod) {
        if (isInfinity()) {
            return null;
        }

        BigInteger numerator = BigInteger.valueOf(3).multiply(x.pow(2)).add(a);
        BigInteger denominator = y.multiply(BigInteger.TWO);
        BigInteger lambda = numerator.multiply(denominator.modInverse(pMod)).mod(pMod);

        BigInteger newX = lambda.pow(2).subtract(x).subtract(x).mod(pMod);
        BigInteger newY = lambda.multiply(x.subtract(newX)).subtract(y).mod(pMod);

        return new ECDSAPoint(newX, newY);
    }

    /**
     * Проверка, является ли точка нейтральным элементом
     *
     * @return true, если точка на бесконечности
     */
    public boolean isInfinity() {
        return x == null || y == null;
    }

    /**
     * Умножение точки на число
     *
     * @param n    число
     * @param a    коэффициент кривой
     * @param pMod модуль
     * @return результат умножения или null
     */
    public ECDSAPoint multiply(BigInteger n, BigInteger a, BigInteger pMod) {
        if (n.equals(BigInteger.ZERO) || isInfinity()) {
            return null;  // 0 * P = O
        }

        ECDSAPoint result = null;
        ECDSAPoint current = this;

        // Проходим по битам n
        for (int i = 0; i < n.bitLength(); i++) {
            if (n.testBit(i)) {
                result = (result == null) ? current : result.add(current, a, pMod);
            }

            current = current.doublePoint(a, pMod);
        }

        return result;
    }
}
