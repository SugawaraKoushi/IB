package vladek.lab7.services;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Service
public class SHA256Service {
    private static final BigInteger MASK = BigInteger.ONE.shiftLeft(512).subtract(BigInteger.ONE);  // 2^512 - 1;

    public BigInteger prepareSourceMessage(String sourceMsg) {
        byte[] msgBytes = sourceMsg.getBytes(StandardCharsets.UTF_8);
        BigInteger msgBigInt = new BigInteger(1, msgBytes);

        // Добавим 1 бит "1" в конец
        msgBigInt = msgBigInt.shiftLeft(1).add(BigInteger.ONE);

        // Проверим кратность 448 % 512
        int remainder = msgBigInt.bitLength() % 512;
        // Определим сколько нужно добавить нулей
        int zerosToAdd = remainder <= 448 ? (448 - remainder) : (512 + 448 - remainder);
        msgBigInt = msgBigInt.shiftLeft(zerosToAdd);
        // Добавим оставшийся блок в 64 бита
        msgBigInt = msgBigInt.shiftLeft(64);

        return msgBigInt;
    }

    public BigInteger getHashCode(BigInteger msg) {
        int blockCounts = msg.bitLength() / 512;
        int[] initialHashes = {
                0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
                0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
        };

        for (int i = 1; i <= blockCounts; i++) {
            int bitsToShift = 512 * (blockCounts - i);
            BigInteger block = msg.shiftRight(bitsToShift);
            block = block.and(MASK);

            int[] w = splitIntoWords(block);


            int a = initialHashes[0], b = initialHashes[1], c = initialHashes[2], d = initialHashes[3];
            int e = initialHashes[4], f = initialHashes[5], g = initialHashes[6], h = initialHashes[7];
            // основной алгоритм хэш функции
            for (int j = 0; j < 64; j++) {

            }
        }

        return new BigInteger("1");
    }

    private int[] splitIntoWords(BigInteger block) {
        byte[] bytes = block.toByteArray();
        int[] words = new int[16];

        for (int i = 0; i < 16; i += 4) {
            byte[] temp = new byte[4];
            System.arraycopy(bytes, 4* i, temp, 0, 4);
            words[i] = bytesToInt(temp);
        }

        return words;
    }

    /**
     * Преобразует последовательность из 4-х байтов в слово
     *
     * @param bytes - 4 байта
     * @return слово
     */
    private static int bytesToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |  // сдвигаем на первый байт
                ((bytes[1] & 0xFF) << 16) | // сдвигаем на второй байт
                ((bytes[2] & 0xFF) << 8) |  // сдвигаем на третий байт
                (bytes[3] & 0xFF);          // оставляем на четвертом байте и все соединяем между собой
    }

    private int ch(int x, int y, int z) {
        int a = x & y;
        int b = (~x) & z;
        return a ^ b;
    }

    private int maj(int x, int y, int z) {
        int a = x & y;
        int b = x & z;
        int c = y & z;
        return a ^ b ^ c;
    }

    private int shiftRight(int x, int n) {
        for (int i = 0; i < n; i++) {
            x = x >>> 1;
        }

        return x;
    }

    public int rotShiftRight(int x, int n) {
        for (int i = 0; i < n; i++) {
            int temp = x & 1;
            temp = shiftLeft(temp,31);
            x = x >>> 1;
            x = x | temp;
        }

        return x;
    }

    private int shiftLeft(int x, int n) {
        for (int i = 0; i < n; i++) {
            x = x << 1;
        }

        return x;
    }

}
