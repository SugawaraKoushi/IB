package vladek.lab2.services;

import org.springframework.stereotype.Service;

@Service("decryptService2")
public class DecryptService implements IDecryptService {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъьыэюя_,.АБВГДЦЕЁЖЗИЙКЛМНОПРСТУФХЦШЩЭЮЯ";

    @Override
    public String decryptFromRailFenceCipher(String text) {
        String part1 = text.substring(0, text.length() / 2);
        String part2 = text.substring(text.length() / 2);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < part1.length(); i++) {
            sb.append(part1.charAt(i));

            if (i < part2.length()) {
                sb.append(part2.charAt(i));
            }
        }

        return sb.toString();
    }
}
