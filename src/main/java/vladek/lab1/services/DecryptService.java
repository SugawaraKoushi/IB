package vladek.lab1.services;

import org.springframework.stereotype.Service;

@Service
public class DecryptService implements IDecryptService {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъьыэюя_,.";

    @Override
    public String decryptFromAdditiveCipherWithShift(String text, int shift) {
        String str = text.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int newSymbolIndex = getSymbolPosition(ALPHABET.indexOf(str.charAt(i)), shift);
            sb.append(ALPHABET.charAt(newSymbolIndex));
        }

        return sb.toString();
    }

    @Override
    public String decryptFromMultiplicativeCipherWithShift(String text, int shift) {
        String str = text.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int newSymbolIndex = ((ALPHABET.indexOf(str.charAt(i))) * shift) % ALPHABET.length();
            sb.append(ALPHABET.charAt(newSymbolIndex));
        }

        return sb.toString();
    }

    private int getSymbolPosition(int pos, int shift) {
        int absolutePos = pos - shift;

        if (absolutePos < 0) {
            absolutePos += ALPHABET.length();
        }

        return absolutePos % ALPHABET.length();
    }
}
