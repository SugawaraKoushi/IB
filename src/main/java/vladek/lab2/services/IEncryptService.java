package vladek.lab2.services;

public interface IEncryptService {
    String encryptByRailFenceCipher(String text);
    String encryptByChangeCipherWithKey(String text, String key);
    String encryptByCombineChangeCipherWithKey(String text, String key);
}
