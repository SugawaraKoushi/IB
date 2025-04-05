package vladek.lab1.services;

import org.springframework.stereotype.Service;

@Service
public class EncodeService implements IEncodeService{
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъьыэюя_,.";

    @Override
    public String encodeByAdditiveCipherWithShift(String text, int shift) {
        String str = text.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int newSymbolIndex = getPositionAfterShift(ALPHABET.indexOf(text.charAt(i)), shift);
            sb.append(ALPHABET.charAt(newSymbolIndex));
        }

        return sb.toString();
    }

    private int getPositionAfterShift(int symbolPos, int shift) {
        int absolutePosition = symbolPos + shift;
        return absolutePosition % ALPHABET.length();
    }
}
