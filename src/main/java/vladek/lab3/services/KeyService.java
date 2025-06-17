package vladek.lab3.services;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Service
public class KeyService {
    /**
     * Нормализует ключ, добавляя в него лишние 0 или обрезая длину, чтобы тот был равен четырем 4-байтовым словам
     *
     * @param key текстовый ключ
     * @return битовый ключ
     */
    public int[] normalizeKey(String key, int keyType) {
        byte[] bytes = getKeyWords(key, keyType);

        if (bytes.length != 16) bytes = Arrays.copyOf(bytes, 16);
        int[] result = new int[4];

        for (int i = 0; i < result.length; i++) {
            byte[] temp = new byte[4];
            System.arraycopy(bytes, 4 * i, temp, 0, 4);
            result[i] = bytesToInt(temp);
        }

        return result;
    }

    /**
     * Преобразует ключ в байты в зависимости от типа ключа.
     *
     * @param key     текстовый ключ
     * @param keyType тип ключа: 1 - текст, 2 - 16-ричное число
     * @return массив байтов ключа
     */
    private byte[] getKeyWords(String key, int keyType) {
        byte[] bytes;

        if (keyType == 1) {
            bytes = getKeyBytesFromText(key);
        } else {
            bytes = getKeyBytesFromHexNumber(key);
        }

        return bytes;
    }

    /**
     * Преобразует ключ в массив байтов
     *
     * @param key текстовый ключ
     * @return массив байтов ключа
     */
    private byte[] getKeyBytesFromText(String key) {
        return key.getBytes(StandardCharsets.UTF_8);
    }


    /**
     * Преобразует ключ, представленный в виде 16-числа, в массив байтов
     * @param key 16-ричный ключ
     * @return массив байтов ключа
     */
    private byte[] getKeyBytesFromHexNumber(String key) {
        byte[] bytes = new byte[key.length()];

        for (int i = 0; i < key.length(); i++) {
            String ch = String.valueOf(key.charAt(i));
            bytes[i] = (byte) Integer.parseInt(ch, 16);
        }

        return bytes;
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
}
