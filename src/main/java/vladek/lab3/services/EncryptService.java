package vladek.lab3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static vladek.lab3.helpers.AES128Helper.*;

@Service
public class EncryptService implements IEncryptService {
    @Autowired
    private KeyService keyService;

    @Override
    public String encryptByAES128(String text, String key, int keyType) {
        // State - изначальный текст, представленный в виде 16 байт

        // 1. Нормализация текста, чтобы было кратно 16 байтам. Возможно стоит заложить длину текста к закодированную штуку
        int[] textWords = normalizeText(text);
        int[] encodedTextWords = new int[textWords.length];

        // 2. Нормализовать ключ, приведя его к 128 битам (16 байтам)
        int[] keyWords = keyService.normalizeKey(key, keyType);

        // 3. Сформировать ключи для раундов, их на 1 больше, чем самих раундов. Всего раундов 10
        int[][] keys = expandKeys(keyWords);

        // Шифруем каждые 16 байт текста
        for (int i = 0; i < textWords.length; i += 4) {
            int[] state = new int[4];
            System.arraycopy(textWords, i, state, 0, 4);

            // Не будем преобразовывать шифруемые 16 байт в массив 4 на 4, т.к. в каждом элементе state
            // хранится по 4 байта (4 пары 16-ричных цифр), и их можно считывать последовательно.
            // По сути своей каждый элемент state это столбец матрицы состояний.
            // Для вычисления значения в "ячейке", можно использовать побитовые сдвиги и умножение

            // 4. Начальное преобразование AddRoundKey (state xor первый_ключ)
            state = addRoundKey(state, keys[0]);

            // 5. Начало 9 основных раундов
            for (int j = 1; j <= 9; j++) {
                // - SubBytes заменяем по SBOXE
                state = subBytes(state);

                // - ShiftRows сдвиг строк
                state = shiftRows(state);

                // - MixColumns перемешиваем строки
                state = mixColumns(state);

                // - Преобразование AddRoundKey (State xor i-тый_ключ)
                state = addRoundKey(state, keys[j]);
            }

            // 6. Финальный раунд SubBytes -> ShiftRows -> AddRoundKey
            state = subBytes(state);
            state = shiftRows(state);
            state = addRoundKey(state, keys[10]);

            // 7. добавить зашифрованный текст в буфер
            System.arraycopy(state, 0, encodedTextWords, i, 4);
        }

        // 8. Преобразовать текст из интов в текст
        return intArrayToBase64String(encodedTextWords);
    }

    /**
     * Нормализует текст, добавляя в него лишние 0, чтобы тот был кратен 16 байтам
     *
     * @param text текст
     * @return текст в байтах
     */
    private int[] normalizeText(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        int zerosCount = 16 - (bytes.length % 16);

        if (zerosCount != 16) {
            byte[] withZeros = new byte[bytes.length + zerosCount];
            System.arraycopy(bytes, 0, withZeros, 0, bytes.length);
            bytes = withZeros;
        }

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
     * Преобразует массив int[] в строку
     *
     * @param intArray массив int'ов
     * @return строка
     */
    private String intArrayToBase64String(int[] intArray) {
        ByteBuffer buffer = ByteBuffer.allocate(intArray.length * 4);

        for (int value : intArray) {
            buffer.putInt(value);
        }

        return Base64.getEncoder().encodeToString(buffer.array());
    }
}
