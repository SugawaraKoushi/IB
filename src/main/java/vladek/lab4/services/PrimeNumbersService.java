package vladek.lab4.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import vladek.lab4.dto.PrimeTestResult;

import java.math.BigInteger;
import java.util.*;

@Service
public class PrimeNumbersService {

    /**
     * Тест Ферма простоты числа
     *
     * @param n нечетное число >= 5
     * @return Число меньше 0, если число составное.
     * Число равное 0, если число является числом Кармайкла
     * Число больше 0, если число вероятно простое.
     * А так же затраченное время на определение простоты.
     */
    public PrimeTestResult fermatTest(long n) {
        Random random = new Random();
        boolean isPrimeNumber = false;
        boolean isCarmichaelNumber;
        int iterations = 20;
        StopWatch sw = new StopWatch();
        PrimeTestResult result = new PrimeTestResult();

        sw.start();
        for (int i = 0; i < iterations; i++) {
            long a = random.nextLong(2, n - 1);
            long r = modPow(a, n - 1, n);

            if (r == 1) {
                isPrimeNumber = true;
            } else {
                if (isPrimeNumber) {
                    // Проверим, является ли оно числом Кармайкла
                    List<Long> primeFactors = getPrimeFactors(n);
                    isCarmichaelNumber = isSquareFree(primeFactors) && primeFactors.size() > 2;

                    sw.stop();
                    result.setSeconds(sw.getTotalTimeSeconds());
                    if (isCarmichaelNumber) {
                        result.setNumberType(0);
                    } else {
                        result.setNumberType(-1);
                    }

                    return result;
                }
            }
        }

        // Если число составное
        if (!isPrimeNumber) {
            // Проверим, является ли оно числом Кармайкла
            List<Long> primeFactors = getPrimeFactors(n);
            isCarmichaelNumber = isSquareFree(primeFactors) && primeFactors.size() > 2;


            sw.stop();
            result.setSeconds(sw.getTotalTimeSeconds());
            if (isCarmichaelNumber) {
                result.setNumberType(0);
            } else {
                result.setNumberType(-1);
            }

            return result;
        }

        sw.stop();
        result.setNumberType(1);
        result.setSeconds(sw.getTotalTimeSeconds());
        return result;
    }

    /**
     * Поиск простых множителей числа с учётом кратности
     *
     * @param n число, для которого ищутся простые множители
     * @return список простых множителей
     */
    private List<Long> getPrimeFactors(long n) {
        List<Long> factors = new ArrayList<>();

        // Пока число делится на 2, будем делить его на 2
        while (n % 2 == 0) {
            factors.add(2L);
            n /= 2;
        }

        // После деления на 2 число осталось нечетным, делим его на нечетные числа
        // Точно так же как с делением на 2, пока делится
        // при этом уменьшаем границу поиска числа
        for (long i = 3; i < Math.sqrt(n); i += 2) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }

        // Если осталось число больше 1 - оно простое
        if (n > 1) {
            factors.add(n);
        }

        return factors;
    }

    /**
     * Проверка числа, что оно свободно от квадратов (нет повторяющихся множителей)
     *
     * @param factors список простых множителей числа
     * @return истина, если число свободно от квадратов. Иначе - ложь
     */
    private static boolean isSquareFree(List<Long> factors) {
        for (int i = 0; i < factors.size() - 1; i++) {
            if (factors.get(i).equals(factors.get(i + 1))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Эффективное возведение числа в степень по модулю
     *
     * @param a        число возводимое в степень
     * @param exponent степень
     * @param mod      модуль
     */
    private long modPow(long a, long exponent, long mod) {
        long result = 1;

        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * a) % mod;
            }

            a = (a * a) % mod;
            exponent = exponent >> 1;
        }

        return result;
    }

    /**
     * Тест Рабина - Миллера простоты числа
     *
     * @param n очень большое нечетное число
     * @param k количество раундов
     * @return Число меньше 0, если число составное.
     * Число больше 0, если число вероятно простое
     */
    public PrimeTestResult millerRabinTest(BigInteger n, int k) {
        PrimeTestResult result = new PrimeTestResult();
        StopWatch sw = new StopWatch();
        sw.start();

        if (n.equals(BigInteger.TWO) || n.equals(new BigInteger("3"))) {
            sw.stop();
            result.setNumberType(1);
            result.setSeconds(sw.getTotalTimeSeconds());
            return result;
        }

        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            sw.stop();
            result.setNumberType(-1);
            result.setSeconds(sw.getTotalTimeSeconds());
            return result;
        }

        BigInteger t = n.subtract(BigInteger.ONE);
        int s = 0;

        while (t.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            t = t.divide(BigInteger.TWO);
            s++;
        }

        Random random = new Random();

        for (int i = 0; i < k; i++) {
            BigInteger a;

            do {
                a = new BigInteger(n.bitLength(), random);
            } while (a.compareTo(BigInteger.TWO) < 0 || a.compareTo(n.subtract(BigInteger.TWO)) > 0);

            BigInteger x = a.modPow(t, n);

            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) continue;

            for (int j = 0; j < s - 1; j++) {
                x = x.pow(2).mod(n);

                if (x.equals(BigInteger.ONE)) {
                    sw.stop();
                    result.setNumberType(-1);
                    result.setSeconds(sw.getTotalTimeSeconds());
                    return result;
                }

                if (x.equals(n.subtract(BigInteger.ONE))) {
                    break;
                }
            }

            if (!x.equals(n.subtract(BigInteger.ONE))) {
                sw.stop();
                result.setNumberType(-1);
                result.setSeconds(sw.getTotalTimeSeconds());
                return result;
            }
        }

        sw.stop();
        result.setNumberType(1);
        result.setSeconds(sw.getTotalTimeSeconds());
        return result;
    }


    /**
     * Тест простоты через делимость
     *
     * @param n число
     * @return Отрицательное число, если n составное
     * Положительное число, если n простое
     */
    public PrimeTestResult divisionTest(long n) {
        PrimeTestResult result = new PrimeTestResult();
        StopWatch sw = new StopWatch();
        sw.start();
        long border = (long) Math.sqrt((double) n);

        for (int i = 2; i <= border; i++) {
            if (n % i == 0) {
                sw.stop();
                result.setNumberType(-1);
                result.setSeconds(sw.getTotalTimeSeconds());
                return result;
            }
        }

        sw.stop();
        result.setNumberType(1);
        result.setSeconds(sw.getTotalTimeSeconds());
        return result;
    }

    /**
     * Алгоритм генерации простого числа
     *
     * @param bits   длина числа в битах
     * @param rounds количество проверок тестом Рабина-Миллера
     * @return Число меньше 0, если число не получилось сгенерировать простое число. Иначе возвращает простое число
     */
    private BigInteger generatePrimeNumber(int bits, int rounds) {
        Random random = new Random();
        BigInteger p = new BigInteger(bits, random);
        p = p.setBit(0);
        p = p.setBit(bits - 1);
        int[] primes = {
                2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71,
                73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173,
                179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281,
                283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409,
                419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499
        };

        for (int prime : primes) {
            BigInteger n = BigInteger.valueOf(prime);

            if (p.equals(n)) {
                return p;
            }

            if (p.mod(n).equals(BigInteger.ZERO)) {
                return new BigInteger("-1");
            }
        }

        for (int i = 0; i < rounds; i++) {
            if (millerRabinTest(p, 50).getNumberType() < 0) {
                return new BigInteger("-1");
            }
        }

        return p;
    }

    public BigInteger getRandomPrimeNumber(int bits, int rounds) {
        BigInteger bigInt;

        do {
            bigInt = generatePrimeNumber(bits, rounds);
        } while (bigInt.equals(new BigInteger("-1")));

        return bigInt;
    }

}
