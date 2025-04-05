package vladek.lab1.services;

import org.springframework.stereotype.Service;

@Service
public class DecodeService implements IDecodeService {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъьыэюя_,.";

    @Override
    public String decodeFromAdditiveCipherWithShift(String text, int shift) {
        String str = text.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int newSymbolIndex = getPositionWithoutShift(ALPHABET.indexOf(text.charAt(i)), shift);
            sb.append(ALPHABET.charAt(newSymbolIndex));
        }

        return sb.toString();
    }

    private int getPositionWithoutShift(int symbolPos, int shift) {
        int absolutePosition = symbolPos - shift;
        return absolutePosition % ALPHABET.length();
    }
}
