package vladek.lab5;

import vladek.lab4.services.PrimeNumbersService;
import vladek.lab5.services.KeyExchangeService;

import java.math.BigInteger;
import java.util.List;

public class Lab5 {
    public static void main(String[] args) {
        PrimeNumbersService primeNumbersService = new PrimeNumbersService();
        KeyExchangeService keyExchangeService = new KeyExchangeService(primeNumbersService);
        BigInteger n = BigInteger.valueOf(101);
        List<BigInteger> roots = keyExchangeService.findPrimitiveRoots(n);
        System.out.printf("%s - %d%n", n, roots.size());

        n = BigInteger.valueOf(107);
        roots = keyExchangeService.findPrimitiveRoots(n);
        System.out.printf("%s - %d%n", n, roots.size());

        n = BigInteger.valueOf(113);
        roots = keyExchangeService.findPrimitiveRoots(n);
        System.out.printf("%s - %d%n", n, roots.size());

        n = BigInteger.valueOf(383);
        roots = keyExchangeService.findPrimitiveRoots(n);
        System.out.printf("%s - %d%n", n, roots.size());

        n = BigInteger.valueOf(500);
        roots = keyExchangeService.findPrimitiveRoots(n);
        System.out.printf("%s - %d%n", n, roots.size());
    }
}
