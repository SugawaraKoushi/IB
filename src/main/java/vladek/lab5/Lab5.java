package vladek.lab5;

import vladek.lab4.services.PrimeNumbersService;
import vladek.lab5.services.PrimitiveRootsService;

import java.math.BigInteger;
import java.util.Set;

public class Lab5 {

    public static void main(String[] args) {
        PrimeNumbersService pns = new PrimeNumbersService();
        PrimitiveRootsService prs = new PrimitiveRootsService(pns);
        BigInteger n = BigInteger.valueOf(1000000000);
        Set<BigInteger> factors = prs.getPrimeFactors(n);
        factors.forEach(System.out::println);
    }
}
