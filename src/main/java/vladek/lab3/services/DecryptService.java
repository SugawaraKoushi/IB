package vladek.lab3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static vladek.lab3.helpers.AES128Helper.*;

@Service
public class DecryptService implements IDecryptService {
    @Autowired
    private KeyService keyService;

    @Override
    public String decryptFromAES128(String text, String key, int keyType) {
        int[] textWords = base64StringToIntArray(text);
        int[] decodedTextWords = new int[textWords.length];
        int[] keyWords = keyService.normalizeKey(key, keyType);
        int[][] keys = expandKeys(keyWords);

        for (int i = 0; i < textWords.length; i += 4) {
            int[] state = new int[4];
            System.arraycopy(textWords, i, state, 0, 4);

            state = addRoundKey(state, keys[10]);
            state = invShiftRows(state);
            state = invSubBytes(state);

            for (int j = 9; j >= 1; j--) {
                // - Преобразование AddRoundKey (State xor i-тый_ключ)
                state = addRoundKey(state, keys[j]);

                // - MixColumns перемешиваем строки
                state = invMixColumns(state);

                // - ShiftRows сдвиг строк
                state = invShiftRows(state);

                // - SubBytes заменяем по SBOXE
                state = invSubBytes(state);
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
        int[] result = new int[bytes.length / 4];

        for (int i = 0; i < result.length; i++) {
            byte[] temp = new byte[4];
            System.arraycopy(bytes, 4 * i, temp, 0, 4);
            result[i] = bytesToInt(temp);
        }

        return result;
    }

    /**
     * Преобразует последовательность из 4-х байтов в слово
     *
     * @param bytes - 4 байта
     * @return слово
     */
    public static int bytesToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |  // сдвигаем на первый байт
                ((bytes[1] & 0xFF) << 16) | // сдвигаем на второй байт
                ((bytes[2] & 0xFF) << 8) |  // сдвигаем на третий байт
                (bytes[3] & 0xFF);          // оставляем на четвертом байте и все соединяем между собой
    }

    /**
     * Преобразует массив 4-байтовых слов в текст в кодировке UTF-8
     *
     * @param intArray массив 4-байтовых слов
     * @return текст
     */
    private String intArrayToUTF8String(int[] intArray) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        for (int value : intArray) {
            byteStream.write(intToBytes(value), 0, 4);
        }

        byte[] bytes = byteStream.toByteArray();

        // обрезаем нули в конце
        int length = bytes.length;

        while (length > 0 && bytes[length - 1] == 0) {
            length--;
        }

        return new String(bytes, 0, length, StandardCharsets.UTF_8);
    }
}
