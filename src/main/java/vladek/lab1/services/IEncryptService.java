package vladek.lab1.services;

public interface IEncryptService {
    String encryptByAdditiveCipherWithShift(String text, int shift);
    String encryptByMultiplicativeCipherWithShift(String text, int shift);
    String encryptByPlayfairCipher(String text);
}
