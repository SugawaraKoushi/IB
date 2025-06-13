package vladek.lab3.services;

public interface IEncryptService {
    String encryptByAES128(String text, String key);
}
