package vladek.lab1.services;

import org.springframework.stereotype.Service;

@Service
public class EncryptService implements IEncryptService {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъьыэюя_,.";

    @Override
    public String encryptByAdditiveCipherWithShift(String text, int shift) {
        String str = text.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int newSymbolIndex = (ALPHABET.indexOf(str.charAt(i)) + shift) % ALPHABET.length();
            sb.append(ALPHABET.charAt(newSymbolIndex));
        }

        return sb.toString();
    }

    @Override
    public String encryptByMultiplicativeCipherWithShift(String text, int shift) {
        String str = text.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int newSymbolIndex = ((ALPHABET.indexOf(str.charAt(i))) * shift);
            newSymbolIndex = newSymbolIndex % ALPHABET.length();
            newSymbolIndex = newSymbolIndex == 0 ? ALPHABET.length() : newSymbolIndex;
            sb.append(ALPHABET.charAt(newSymbolIndex ));
        }

        return sb.toString();
    }

    @Override
    public String encryptByPlayfairCipher(String text) {
        return "";
    }
}
