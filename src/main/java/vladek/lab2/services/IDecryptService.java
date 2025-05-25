package vladek.lab2.services;

public interface IDecryptService {
    String decryptFromRailFenceCipher(String text);
    String decryptFromChangeCipherWithKey(String text, String key);
    String decryptFromCombineChangeCipherWithKey(String text, String key);
}
