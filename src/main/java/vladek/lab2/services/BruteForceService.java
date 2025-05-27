package vladek.lab2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vladek.lab2.dto.BruteForceResponse;

import java.util.HashSet;

@Service
public class BruteForceService implements IBruteForceService {
    @Autowired
    private IDecryptService decryptService;

    @Override
    public BruteForceResponse bruteForceCombineCipher(String text, String lastKey) {
        String key = generateKeyForCombineCipher(lastKey);
        String decryptedText = decryptService.decryptFromCombineChangeCipherWithKey(text, key);

        BruteForceResponse result = new BruteForceResponse();
        result.setText(decryptedText);
        result.setKey(key);

        return result;
    }

    private String generateKeyForCombineCipher(String key) {
        int keyNum = Integer.parseInt(key);
        boolean isValid;

        do {
            isValid = validateKey(++keyNum);
        } while (!isValid);

        return String.valueOf(keyNum);
    }

    private boolean validateKey(int key) {
        String keyStr = String.valueOf(key);
        HashSet<Character> digits = new HashSet<>();

        // Проверка на 0 и уникальность
        for (int i = 0; i < keyStr.length(); i++) {
            char c = keyStr.charAt(i);
            if (c == '0' || !digits.add(c)) {
                return false;
            }
        }

        int min = 9, max = 0;
        for (char c : digits) {
            int digit = Character.getNumericValue(c);
            min = Math.min(min, digit);
            max = Math.max(max, digit);
        }

        // Минимальная цифра должна быть 1, и диапазон должен быть непрерывным
        return (min == 1) && (max - min + 1 == keyStr.length());
    }
}
