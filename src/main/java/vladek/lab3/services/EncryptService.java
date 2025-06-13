package vladek.lab3.services;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncryptService implements IEncryptService {
    private static final int[][] SBOXE = {
            {0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76},
            {0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0},
            {0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15},
            {0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75},
            {0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84},
            {0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF},
            {0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8},
            {0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2},
            {0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73},
            {0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB},
            {0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79},
            {0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08},
            {0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A},
            {0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E},
            {0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF},
            {0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16}
    };
    private static final int[] RCON = {0x01000000, 0x02000000, 0x04000000, 0x08000000,0x10000000, 0x20000000,
            0x40000000, 0x80000000, 0x1B000000, 0x36000000};

    @Override
    public String encryptByAES128(String text, String key) {
        // State - изначальный текст, представленный в виде 16 байт

        // 1. Нормализация текста, чтобы было кратно 16 байтам. Возможно стоит заложить длину текста к закодированную штуку
        int[] textWords = normalizeText(text);

        // 2. Нормализовать ключ, приведя его к 128 битам (16 байтам)
        int[] keyWords = normalizeKey(key);

        // 3. Сформировать ключи для раундов, их на 1 больше, чем самих раундов. Всего раундов 10
        // 4. Начальное преобразование AddRoundKey State xor первый_ключ
        // 5. Начало 10 основных раундов
        // - SubBytes заменяем по S-box
        // - ShiftRows сдвиг строк
        // - MixColumns перемешиваем строки
        // - преобразование AddRoundKey State xor i-тый_ключ
        // 6. SubBytes -> ShiftRows -> AddRoundKey
        // 7. Преобразовать текст из байтов в текст
        return "";
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

        int[] result = new int[bytes.length / 16];

        for (int i = 0; i < result.length; i++) {
            byte[] temp = new byte[4];
            System.arraycopy(bytes, 4 * i, temp, 0, 4);
            result[i] = bytesToInt(temp);
        }

        return result;
    }

    /**
     * Нормализует ключ, добавляя в него лишние 0 или обрезая длину, чтобы тот был равен 128 битам
     *
     * @param key текстовый ключ
     * @return 128 битовый ключ
     */
    private int[] normalizeKey(String key) {
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
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
     * Преобразует последовательность из 4-х байтов в слово
     *
     * @param bytes - 4 байта
     * @return слово
     */
    private int bytesToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |  // сдвигаем на первый байт
                ((bytes[1] & 0xFF) << 16) | // сдвигаем на второй байт
                ((bytes[2] & 0xFF) << 8) |  // сдвигаем на третий байт
                (bytes[3] & 0xFF);          // оставляем на четвертом байте и все соединяем между собой
    }

    /**
     * Порождает 11 16-байтовых ключей
     *
     * @param key исходный 16-байтовый ключ
     * @return 11 16-байтовых ключей для раундов шифрования
     */
    private int[] expandKeys(int[] key) {
        int[] keysWords = new int[44];
        System.arraycopy(key, 0, keysWords, 0, 4);

        for (int i = 4; i < keysWords.length; i++) {
            if (i % 4 == 0) {
                keysWords[i] = keysWords[i - 1] ^ keysWords[i - 4];
            } else {
                int t = subWord(rotWord(keysWords[i - 1])) ^ RCON[i/4];
                keysWords[i] = t ^ keysWords[i - 4];
            }
        }

        return keysWords;
    }

    /**
     * Циклический сдвиг слова влево на 1 байт
     *
     * @param word слово
     * @return преобразованное слово
     */
    private int rotWord(int word) {
        return ((word << 8) | (word & 0xFF000000 >> 24));
    }

    /**
     * Преобразует слово по таблице SBOXE
     *
     * @param word слово
     * @return преобразованное слово
     */
    private int subWord(int word) {
        int b0 = (word >> 28) & 0xF;
        int b1 = (word >> 24) & 0xF;

        int b2 = (word >> 20) & 0xF;
        int b3 = (word >> 16) & 0xF;

        int b4 = (word >> 12) & 0xF;
        int b5 = (word >> 8) & 0xF;

        int b6 = (word >> 4) & 0xF;
        int b7 = word & 0xF;

        int sb0 = SBOXE[b0][b1];
        int sb1 = SBOXE[b2][b3];
        int sb2 = SBOXE[b4][b5];
        int sb3 = SBOXE[b6][b7];

        return ((sb0 << 24) | (sb1 << 16) | (sb2 << 8) | sb3);
    }
}
