package vladek.lab3.helpers;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AES128Helper {
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
    private static final int[][] SBOXD = {
            {0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB},
            {0x7C, 0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4, 0xDE, 0xE9, 0xCB},
            {0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D, 0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E},
            {0x08, 0x2E, 0xA1, 0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B, 0xD1, 0x25},
            {0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4, 0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92},
            {0x6C, 0x70, 0x48, 0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D, 0x84},
            {0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06},
            {0xD0, 0x2C, 0x1E, 0x8F, 0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B},
            {0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73},
            {0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E},
            {0x47, 0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E, 0xAA, 0x18, 0xBE, 0x1B},
            {0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79, 0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4},
            {0x1F, 0xDD, 0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xEC, 0x5F},
            {0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF},
            {0xA0, 0xE0, 0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53, 0x99, 0x61},
            {0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D}
    };
    private static final int[] RCON = {0x01000000, 0x02000000, 0x04000000, 0x08000000, 0x10000000, 0x20000000,
            0x40000000, 0x80000000, 0x1B000000, 0x36000000};

    /**
     * Выполняет операцию XOR над 4-байтовым словом и 4-байтовым ключом
     *
     * @param word 4-байтовое слово
     * @param key  4-байтовый ключ
     * @return результат XOR
     */
    public static int[] addRoundKey(int[] word, int[] key) {
        int[] result = new int[4];

        for (int i = 0; i < 4; i++) {
            result[i] = key[i] ^ word[i];
        }

        return result;
    }

    /**
     * Порождает 11 16-байтовых ключей
     *
     * @param key исходный 16-байтовый ключ
     * @return 11 16-байтовых ключей для раундов шифрования
     */
    public static int[][] expandKeys(int[] key) {
        int[] keysWords = new int[44];
        System.arraycopy(key, 0, keysWords, 0, 4);

        for (int i = 4; i < keysWords.length; i++) {
            if (i % 4 == 0) {
                keysWords[i] = keysWords[i - 1] ^ keysWords[i - 4];
            } else {
                int t = subWord(rotWord(keysWords[i - 1])) ^ RCON[i / 4 - 1];
                keysWords[i] = t ^ keysWords[i - 4];
            }
        }

        int[][] keys = new int[11][4];

        for (int i = 0; i < keys.length; i++) {
            for (int j = 0; j < keys[i].length; j++) {
                keys[i][j] = keysWords[i * keys[i].length + j];
            }
        }

        return keys;
    }

    /**
     * Циклический сдвиг 4-байтового слова влево на 1 байт
     *
     * @param word слово
     * @return преобразованное слово
     */
    private static int rotWord(int word) {
        return ((word << 8) | (word & 0xFF000000 >> 24));
    }

    /**
     * Преобразует 4-байтовое слово по таблице SBOXE
     *
     * @param word слово
     * @return преобразованное слово
     */
    public static int subWord(int word) {
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

    /**
     * Преобразует слово по таблице SBOXD
     *
     * @param word слово
     * @return преобразованное слово
     */
    public static int invSubWord(int word) {
        int b0 = (word >> 28) & 0xF;
        int b1 = (word >> 24) & 0xF;

        int b2 = (word >> 20) & 0xF;
        int b3 = (word >> 16) & 0xF;

        int b4 = (word >> 12) & 0xF;
        int b5 = (word >> 8) & 0xF;

        int b6 = (word >> 4) & 0xF;
        int b7 = word & 0xF;

        int sb0 = SBOXD[b0][b1];
        int sb1 = SBOXD[b2][b3];
        int sb2 = SBOXD[b4][b5];
        int sb3 = SBOXD[b6][b7];

        return ((sb0 << 24) | (sb1 << 16) | (sb2 << 8) | sb3);
    }

    /**
     * Преобразует матрицу состояний по SBOXE
     *
     * @param state матрица состояний
     * @return преобразованная матрица
     */
    public static int[] subBytes(int[] state) {
        int[] result = new int[4];

        for (int i = 0; i < 4; i++) {
            result[i] = subWord(state[i]);
        }

        return result;
    }

    /**
     * Преобразует матрицу состояний по SBOXD
     *
     * @param state матрица состояний
     * @return преобразованная матрица
     */
    public static int[] invSubBytes(int[] state) {
        int[] result = new int[4];

        for (int i = 0; i < 4; i++) {
            result[i] = invSubWord(state[i]);
        }

        return result;
    }

    /**
     * Преобразует матрицу состоянию, сдвигая каждую ее строку циклическим сдвигом влево.
     * Первая строка не сдвигается.
     * Вторая сдвигается на 1.
     * Третья сдвигается на 2.
     * Четвертая сдвигается на 3.
     *
     * @param state матрица состояний
     * @return преобразованная матрица состояний
     */
    public static int[] shiftRows(int[] state) {
        int[] result = new int[4];
        System.arraycopy(state, 0, result, 0, 4);

        for (int i = 0; i < 4; i++) {
            int p0 = (state[i] >> 24) & 0xFF;
            int p1 = (state[(i + 1) % 4] >> 16) & 0xFF;
            int p2 = (state[(i + 2) % 4] >> 8) & 0xFF;
            int p3 = state[(i + 3) % 4] & 0xFF;
            result[i] = ((p0 << 24) | (p1 << 16) | (p2 << 8) | p3);
        }

        return result;
    }

    /**
     * Преобразует матрицу состояний, сдвигая каждую ее строку циклическим сдвигом вправо.
     * Первая строка не сдвигается.
     * Вторая сдвигается на 1.
     * Третья сдвигается на 2.
     * Четвертая сдвигается на 3.
     *
     * @param state матрица состояний
     * @return преобразованная матрица состояний
     */
    public static int[] invShiftRows(int[] state) {
        int[] result = new int[4];
        System.arraycopy(state, 0, result, 0, 4);

        for (int i = 0; i < 4; i++) {
            int p0 = (state[i] >> 24) & 0xFF;
            int p1 = (state[(i + 3) % 4] >> 16) & 0xFF;
            int p2 = (state[(i + 2) % 4] >> 8) & 0xFF;
            int p3 = (state[(i + 1) % 4]) & 0xFF;
            result[i] = ((p0 << 24) | (p1 << 16) | (p2 << 8) | p3);
        }

        return result;
    }

    /**
     * Преобразует матрицу состояний умножая ее на квадратную матрицу констант
     * Умножение происходит в поле Галуа GF(2^8)
     *
     * @param state матрица состояний
     * @return преобразованная матрица состояний
     */
    public static int[] mixColumns(int[] state) {
        int[] result = new int[4];

        for (int i = 0; i < 4; i++) {
            // Разбиваем строку по 2 байта
            int s0 = (state[i] >> 24) & 0xFF;
            int s1 = (state[i] >> 16) & 0xFF;
            int s2 = (state[i] >> 8) & 0xFF;
            int s3 = state[i] & 0xFF;

            int res0 = galoisMultiply(s0, 0x02) ^ galoisMultiply(s1, 0x03) ^ s2 ^ s3;
            int res1 = s0 ^ galoisMultiply(s1, 0x02) ^ galoisMultiply(s2, 0x03) ^ s3;
            int res2 = s0 ^ s1 ^ galoisMultiply(s2, 0x02) ^ galoisMultiply(s3, 0x03);
            int res3 = galoisMultiply(s0, 0x03) ^ s1 ^ s2 ^ galoisMultiply(s3, 0x02);

            result[i] = ((res0 << 24) | (res1 << 16) | (res2 << 8) | res3);
        }

        return result;
    }

    /**
     * Преобразует матрицу состояний умножая ее на квадратную матрицу констант
     *
     * @param state матрица состояний
     * @return преобразованная матрица состояний
     */
    public static int[] invMixColumns(int[] state) {
        int[] result = new int[4];

        for (int i = 0; i < 4; i++) {
            // Разбиваем строку по 2 байта
            int s0 = (state[i] >> 24) & 0xFF;
            int s1 = (state[i] >> 16) & 0xFF;
            int s2 = (state[i] >> 8) & 0xFF;
            int s3 = state[i] & 0xFF;

            // Умножаем на инверсную квадратную матрицу
            int res0 = galoisMultiply(s0, 0x0E) ^ galoisMultiply(s1, 0x0B)
                    ^ galoisMultiply(s2, 0x0D) ^ galoisMultiply(s3, 0x09);
            int res1 = galoisMultiply(s0, 0x09) ^ galoisMultiply(s1, 0x0E)
                    ^ galoisMultiply(s2, 0x0B) ^ galoisMultiply(s3, 0x0D);
            int res2 = galoisMultiply(s0, 0x0D) ^ galoisMultiply(s1, 0x09)
                    ^ galoisMultiply(s2, 0x0E) ^ galoisMultiply(s3, 0x0B);
            int res3 = galoisMultiply(s0, 0x0B) ^ galoisMultiply(s1, 0x0D)
                    ^ galoisMultiply(s2, 0x09) ^ galoisMultiply(s3, 0x0E);

            result[i] = ((res0 << 24) | (res1 << 16) | (res2 << 8) | res3);
        }

        return result;
    }

    /**
     * Произведение числа a на b в поле Галуа GF(2^8)
     * По сути это произведение двух полиномов столбиком, проверка на переполнение
     * и вместо суммы - XORим слагаемые полиномов между собой
     *
     * @param a первый множитель
     * @param b второй множитель
     * @return результат произведения
     */
    public static int galoisMultiply(int a, int b) {
        int result = 0;

        while (b > 0) {
            // проверяем, умножаем ли мы на число или нет
            if ((b & 1) != 0) {
                result ^= a; // суммируем результат по модулю 2
            }

            a <<= 1;

            // проверяем на переполнение
            if ((a & 0x100) != 0) {
                a ^= 0x11B; // получение остатка от деления a на неприводимый полином x^8 + x^4 + x^3 + x + 1
            }

            b >>= 1;
        }

        return result;
    }

    /**
     * Преобразует int в массив из 4 байт
     */
    public static byte[] intToBytes(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value
        };
    }
}
