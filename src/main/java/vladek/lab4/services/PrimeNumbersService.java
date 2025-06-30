package vladek.lab4.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.math.BigInteger;
import java.util.Random;

@Service
public class PrimeNumbersService {

    /**
     * Тест Ферма простоты числа
     *
     * @param n нечетное число >= 5
     * @return простота числа
     */
    public String fermatTest(long n) {
        Random random = new Random();
        boolean isPrimeNumber = false;
        boolean isCarlmichaelNumber = true;
        int iterations = 1000000;
        StopWatch sw = new StopWatch();

        sw.start();
        for (int i = 0; i < iterations; i++) {
            long a = random.nextLong(2, n - 1);
            long r = (long) Math.pow((double) a, (double) (n - 1)) % n;

            if (r == 1) {
                isPrimeNumber = true;
            } else {
                isCarlmichaelNumber = false;

                if (isPrimeNumber) {
                    sw.stop();
                    return String.format("Число %d вероятно простое.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
                }
            }
        }

        if (!isPrimeNumber) {
            sw.stop();
            return String.format("Число %d составное.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
        }

        if (!isCarlmichaelNumber) {
            return String.format("Число %d вероятно простое.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
        }

        sw.stop();
        return String.format("Число %d является числом Кармайкла.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
    }

    /**
     * Тест Рабина - Миллера простоты числа
     *
     * @param n нечетное число
     * @param k количество раундов
     * @return простота числа
     */
    public String millerRabinTest(long n, int k) {
        StopWatch sw = new StopWatch();
        sw.start();

        if (n == 2 || n == 3) {
            sw.stop();
            return String.format("Число %d вероятно простое.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
        }

        if (n % 2 == 0) {
            sw.stop();
            return String.format("Число %d составное.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
        }

        long t = n - 1;
        int s = 0;

        while (t % 2 == 0) {
            t /= 2;
            s++;
        }
        ;

        Random random = new Random();

        for (int i = 0; i < k; i++) {
            long a = random.nextLong(2, n - 1);
            long x = (long) Math.pow((double) a, (double) t) % n;

            if (x == 1 || x == n - 1) continue;

            for (int j = 0; j < s - 1; j++) {
                x = x * x % 2;

                if (x == 1) {
                    sw.stop();
                    return String.format("Число %d составное.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
                }

                if (x == n - 1) {
                    break;
                }
            }

            if (x != n - 1) {
                sw.stop();
                return String.format("Число %d составное.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
            }
        }

        sw.stop();
        return String.format("Число %d вероятно простое.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
    }


    /**
     * Тест Рабина - Миллера простоты числа для очень больших чисел
     *
     * @param n очень большое нечетное число
     * @param k количество раундов
     * @return число меньше 0, если число составное. Число равное 0, если число вероятно простое
     */
    private int millerRabanTestForBigInteger(BigInteger n, int k) {
        StopWatch sw = new StopWatch();
        sw.start();

        if (n.equals(BigInteger.TWO) || n.equals(new BigInteger("3"))) {
            sw.stop();
            return 0;
        }

        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            sw.stop();
            return -1;
        }

        BigInteger t = n.subtract(BigInteger.ONE);
        int s = 0;

        while (t.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            t = t.divide(BigInteger.TWO);
            s++;
        }

        Random random = new Random();

        for (int i = 0; i < k; i++) {
            BigInteger a = new BigInteger(n.subtract(BigInteger.ONE).bitCount(), random);
            BigInteger x = a.modPow(t, n);

            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) continue;

            for (int j = 0; j < s - 1; j++) {
                x = x.pow(2).mod(n);

                if (x.equals(BigInteger.ONE)) {
                    sw.stop();
                    return -1;
                }

                if (x.equals(n.subtract(BigInteger.ONE))) {
                    break;
                }
            }

            if (!x.equals(n.subtract(BigInteger.ONE))) {
                sw.stop();
                return -1;
            }
        }

        sw.stop();
        return 0;
    }


    /**
     * Тест простоты через делимость
     *
     * @param n число
     * @return простота числа
     */
    public String divisionTest(long n) {
        StopWatch sw = new StopWatch();
        sw.start();
        long border = (long) Math.sqrt((double) n);

        for (int i = 2; i <= border; i++) {
            if (n % i == 0) {
                sw.stop();
                return String.format("Число %d составное.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
            }
        }

        return String.format("Число %d простое.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
    }

    /**
     * Алгоритм генерации простого числа
     *
     * @param bits   длина числа в битах
     * @param rounds количество проверок тестом Рабина-Миллера
     * @return Число меньше 0, если число не получилось сгенерировать простое число. Иначе возвращает простое число
     */
    public BigInteger generatePrimeNumber(int bits, int rounds) {
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
            BigInteger n = new BigInteger(String.valueOf((prime)));

            if (p.mod(n).equals(BigInteger.ZERO)) {
                return new BigInteger("-1");
            }
        }

        for (int i = 0; i < rounds; i++) {
            if (millerRabanTestForBigInteger(p, rounds) < 0) {
                return new BigInteger("-1");
            }
        }

        return p;
    }
}
