package vladek.lab3.services;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static vladek.lab3.helpers.AES128Helper.*;

@Service
public class DecryptService implements IDecryptService {
    @Override
    public String decryptFromAES128(String text, String key) {
        int[] textWords = base64StringToIntArray(text);
        int[] decodedTextWords = new int[textWords.length];
        int[] keyWords = normalizeKey(key);
        int[][] keys = expandKeys(keyWords);

        for (int i = 0; i < textWords.length; i += 4) {
            int[] state = new int[4];
            System.arraycopy(textWords, i, state, 0, 4);

            state = addRoundKey(state, keys[10]);
            state = invShiftRows(state);
            state = invSubBytes(state);

            for (int j = 9; j >= 1; j--) {
                // - MixColumns перемешиваем строки
                state = invMixColumns(state);

                // - ShiftRows сдвиг строк
                state = invShiftRows(state);

                // - SubBytes заменяем по SBOXE
                state = invSubBytes(state);

                // - Преобразование AddRoundKey (State xor i-тый_ключ)
                state = addRoundKey(state, keys[i]);
            }

            state = addRoundKey(state, keys[0]);

            System.arraycopy(state, 0, decodedTextWords, i, 4);
        }

        return intArrayToUTF8String(decodedTextWords);
    }

    /**
     * Преобразует base64-текст в массив 4-байтовых слов
     *
     * @param text исходный текст
     * @return массив 4-байтовых слов
     */
    private int[] base64StringToIntArray(String text) {
        byte[] bytes = Base64.getDecoder().decode(text);
        int[] result = new int[bytes.length / 8];

        for (int i = 0; i < result.length; i++) {
            byte[] temp = new byte[4];
            System.arraycopy(bytes, 4 * i, temp, 0, 4);
            result[i] = bytesToInt(temp);
        }

        return result;
    }

    /**
     * Преобразует массив 4-байтовых слов в текст в кодировке UTF-8
     * @param intArray массив 4-байтовых слов
     * @return текст
     */
    private String intArrayToUTF8String(int[] intArray) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        for (int value : intArray) {
            byte[] bytes = intToBytes(value);
            byteStream.write(bytes, 0, bytes.length);
        }

        return byteStream.toString(StandardCharsets.UTF_8);
    }
}
