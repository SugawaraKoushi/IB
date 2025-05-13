package vladek.lab1.services;

public interface IDecryptService {
    String decryptFromAdditiveCipherWithShift(String text, int shift);
    String decryptFromMultiplicativeCipherWithShift(String text, int shift);
    String decryptFromPlayfairCipher(String text, String key);
}
