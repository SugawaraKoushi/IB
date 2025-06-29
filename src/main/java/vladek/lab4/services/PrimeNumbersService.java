package vladek.lab4.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Random;

@Service
public class PrimeNumbersService {
    public String eulerTest(long n) {
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
                    return String.format("Число %d вероятно простое.%nЗатрачено времени: %.6f сек",n, sw.getTotalTimeSeconds());
                }
            }
        }

        if (!isPrimeNumber) {
            sw.stop();
            return String.format("Число %d составное.%nЗатрачено времени: %.6f сек", n, sw.getTotalTimeSeconds());
        }

        if (!isCarlmichaelNumber) {
            return String.format("Число %d вероятно простое.%nЗатрачено времени: %.6f сек",n, sw.getTotalTimeSeconds());
        }

        sw.stop();
        return String.format("Число %d является числом Кармайкла.%nЗатрачено времени: %.6f сек",n, sw.getTotalTimeSeconds());
    }
}
