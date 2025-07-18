package vladek.lab5.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import vladek.lab4.services.PrimeNumbersService;
import vladek.lab5.dto.PrimitiveRootsResponse;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class PrimitiveRootsService {
    private final PrimeNumbersService primeNumbersService;

    /**
     * Нахождение первообразных корней числа n
     */
    public PrimitiveRootsResponse findPrimitiveRoots(BigInteger n) {
        List<BigInteger> roots = new ArrayList<>();
        PrimitiveRootsResponse result = new PrimitiveRootsResponse();
        StopWatch sw = new StopWatch();
        sw.start();

        if (!hasPrimitiveRoots(n)) {
            sw.stop();
            result.setRoots(roots);
            result.setSeconds(sw.getTotalTimeSeconds());
            return result;
        }

        BigInteger phi = eulerFunc(n);
        BigInteger rootsCount = eulerFunc(phi);
        Set<BigInteger> primeFactors = getPrimeFactors(phi);
        int bound = Math.min(rootsCount.intValue(), 100);

        for (int i = 1; i < n.intValue() && roots.size() < bound; i++) {
            BigInteger g = BigInteger.valueOf(i);

            // Число должно быть взаимно простым с n, чтобы быть его корнем
            if (!gcd(g, n).equals(BigInteger.ONE)) continue;

            // Проверяем является ли число индексом по модулю n
            boolean isRoot = true;
            for (BigInteger factor : primeFactors) {
                if (g.modPow(phi.divide(factor), n).equals(BigInteger.ONE)) {
                    isRoot = false;
                    break;
                }
            }

            if (isRoot) roots.add(g);
        }

        sw.stop();
        result.setRoots(roots);
        result.setSeconds(sw.getTotalTimeSeconds());
        return result;
    }

    /**
     * Функция Эйлера поиска взаимно простых чисел
     *
     * @return Взаимно простые числа с n
     */
    private BigInteger eulerFunc(BigInteger n) {
        if (primeNumbersService.millerRabinTest(n, 20).getNumberType() > 0) {
            return n.subtract(BigInteger.ONE);
        }

        Set<BigInteger> primeFactors = getPrimeFactors(n);

        BigInteger result = n;
        for (BigInteger p : primeFactors) {
            result = result.divide(p).multiply(p.subtract(BigInteger.ONE));
        }

        return result;
    }

    /**
     * Функция поиска наибольшего общего делителя
     *
     * @return Наибольший общий делитель
     */
    private BigInteger gcd(BigInteger a, BigInteger b) {
        return b.equals(BigInteger.ZERO) ? a : gcd(b, a.mod(b));
    }

    /**
     * Функция нахождения множества простых множителей числа
     *
     * @return Множество простых множителей
     */
    private Set<BigInteger> getPrimeFactors(BigInteger n) {
        Set<BigInteger> factors = new HashSet<>();

        // Пока число делится на 2, будем делить его на 2
        while (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            factors.add(BigInteger.TWO);
            n = n.divide(BigInteger.TWO);
        }

        // После деления на 2 число осталось нечетным, делим его на нечетные числа
        // Точно так же как с делением на 2, пока делится
        // при этом уменьшаем границу поиска числа
        BigInteger delimiter = BigInteger.valueOf(3);
        BigInteger border = n.sqrt();
        while (delimiter.compareTo(border) <= 0) {
            while (n.mod(delimiter).equals(BigInteger.ZERO)) {
                factors.add(delimiter);
                n = n.divide(delimiter);
                border = n.sqrt();
            }

            delimiter = delimiter.add(BigInteger.TWO);
        }

        // Если осталось число больше 1 - оно простое
        if (n.compareTo(BigInteger.ONE) > 0) {
            factors.add(n);
        }

        return factors;
    }

    /**
     * Проверяет наличие первообразных корней
     */
    private boolean hasPrimitiveRoots(BigInteger n) {
        if (n.equals(BigInteger.TWO) || n.equals(BigInteger.valueOf(4))) {
            return true;
        }

        // Проверяем на простоту
        if (primeNumbersService.millerRabinTest(n, 20).getNumberType() > 0) return true;

        // Проверяем p^k
        // Числа, где больше 1го простого множителя не подойдут
        // Поэтому достаточно находить первый простой множитель и проверять
        // Можно ли его возвести в степень и получить исследуемое число
        BigInteger p = findFirstPrimeFactor(n);
        if (p.compareTo(BigInteger.ZERO) > 0 && isPowerOfPrime(n, p)) return true;

        // Проверяем 2*p^k
        // Для этого число должно быть четным, исходя из формулы
        // А дальше точно так же как с p^k
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            BigInteger oddPart = n.divide(BigInteger.TWO);
            p = findFirstPrimeFactor(oddPart);
            return p.compareTo(BigInteger.ZERO) > 0 && isPowerOfPrime(oddPart, p);
        }

        return false;
    }

    /**
     * Находит первый простой множитель числа
     *
     * @return простой множитель или 0, если не нашло его
     */
    private BigInteger findFirstPrimeFactor(BigInteger n) {
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) return BigInteger.TWO;

        for (BigInteger delimeter = BigInteger.valueOf(3); delimeter.compareTo(n) <= 0; delimeter = delimeter.add(BigInteger.TWO)) {
            if (n.mod(delimeter).equals(BigInteger.ZERO)) {
                return delimeter;
            }
        }

        return BigInteger.ZERO;
    }

    /**
     * Проверяется, является ли число p степенью по модулю числа n
     *
     * @param n число
     * @param p степень
     */
    private boolean isPowerOfPrime(BigInteger n, BigInteger p) {
        while (n.compareTo(BigInteger.ONE) > 0) {
            if (!n.mod(p).equals(BigInteger.ZERO)) return false;
            n = n.divide(p);
        }

        return true;
    }

    /**
     * Находит все уникальные простые множители числа
     *
     * @param n число, для которого ищутся простые множители
     */
    public Set<BigInteger> getAllFactors(BigInteger n) {
        Set<BigInteger> factors = new HashSet<>();

        if (n.compareTo(BigInteger.ONE) <= 0) {
            return factors;
        }

        while (!n.equals(BigInteger.ONE)) {
            BigInteger p = pollardRhoFactorization(n);

            // Если число простое
            if (p.equals(n) || p.equals(BigInteger.ONE)) {
                factors.add(n);
                return factors;
            }

            factors.add(p);
            n = n.divide(p);
        }

        return factors;
    }

    /**
     * Метод нахождения простого множителя методом Полларда "Ро"
     */
    public BigInteger pollardRhoFactorization(BigInteger n) {
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return BigInteger.TWO;
        }

        BigInteger x = BigInteger.TWO;
        BigInteger y = BigInteger.TWO;
        BigInteger p = BigInteger.ONE;

        while (p.equals(BigInteger.ONE)) {
            x = f(x);
            y = f(f(y).mod(n)).mod(n);
            p = gcd(x.subtract(y), n);
        }

        return p;
    }

    private BigInteger f(BigInteger x) {
        return x.pow(2).add(BigInteger.ONE);
    }
}
