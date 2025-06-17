package vladek.lab3.services;

public interface IDecryptService {
    String decryptFromAES128(String text, String key);
}
