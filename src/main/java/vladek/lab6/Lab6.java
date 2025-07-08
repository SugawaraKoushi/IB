package vladek.lab6;

import vladek.lab4.services.PrimeNumbersService;
import vladek.lab6.dto.RSAKeyPair;
import vladek.lab6.services.RSAService;

public class Lab6 {
    public static void main(String[] args) {
        PrimeNumbersService pns = new PrimeNumbersService();
        RSAService rs = new RSAService(pns);
        RSAKeyPair keyPair = rs.keyGeneration();
        String text = "Привет как дела как погода азазаза";
        byte[] encryptedBytes = rs.encrypt(text, keyPair.getPublicKey());
        String decryptedText = rs.decrypt(encryptedBytes, keyPair.getPrivateKey());
        System.out.println(text);
        System.out.println(decryptedText);
    }
}
