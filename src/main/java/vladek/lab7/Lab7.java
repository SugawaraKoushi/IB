package vladek.lab7;

import vladek.lab7.services.SHA256Service;

import java.math.BigInteger;

public class Lab7 {
    public static void main(String[] args) {
        SHA256Service sha256Service = new SHA256Service();
        BigInteger a = sha256Service.prepareSourceMessage("Привет как дела?Привет как дела?Привет как дела?Привет как дела?Привет как делаПривет как дела?Привет как дела?Привет как дела?");
        System.out.println(a.bitLength());
        System.out.println(sha256Service.rotShiftRight(4, 1));
    }
}
