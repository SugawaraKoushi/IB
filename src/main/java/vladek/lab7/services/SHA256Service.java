package vladek.lab7.services;

import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Service
public class SHA256Service {
    private static final int[] K = {
            0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
            0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
            0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    /**
     * Подготовка сообщения, чтобы оно было кратно 512 битам
     *
     * @param sourceMsg исходного сообщение
     * @return последовательность блоков по 512 бит
     */
    public byte[][] prepareMessageBlocks(String sourceMsg) {
        byte[] msgBytes = sourceMsg.getBytes(StandardCharsets.UTF_8);
        int originalBitLength = msgBytes.length * 8;

        // Вычисляем необходимое количество блоков вместе с добавленным битом "1" и недостающими нулями
        int totalBlocks = (msgBytes.length + 8 + 1 + 63) / 64;

        byte[][] blocks = new byte[totalBlocks][64];
        int bytesCopied = 0;

        // Копируем данные сообщения во все блоки, кроме последнего
        for (int i = 0; i < totalBlocks - 1; i++) {
            int copyLength = Math.min(64, msgBytes.length - bytesCopied);
            System.arraycopy(msgBytes, bytesCopied, blocks[i], 0, copyLength);
            bytesCopied += copyLength;
        }

        // Обрабатываем последний блок
        byte[] lastBlock = blocks[totalBlocks - 1];

        // Копируем оставшиеся данные
        if (bytesCopied < msgBytes.length) {
            System.arraycopy(msgBytes, bytesCopied, lastBlock, 0, msgBytes.length - bytesCopied);
        }

        // Добавляем бит "1" как байт
        lastBlock[msgBytes.length - bytesCopied] = (byte) 0x80;

        // Добавляем длину сообщения в битах в конец последнего блока
        // 56 - потому что в предыдущем шаге добавили "1000 0000"
        ByteBuffer.wrap(lastBlock, 56, 8).putLong(originalBitLength);

        return blocks;
    }


    /**
     * Алгоритм SHA-256 получение хэша
     */
    public byte[] getHashCode(byte[][] blocks) {
        int[] initialHashes = {
                0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
        };

        for (int i = 0; i < blocks.length; i++) {
            // Преобразуем блок байтов в массив 32-битных слов
            int[] w = splitIntoWords(blocks[i]);

            // Расширяем блок из 16 слов до 64
            w = expandWords(w);

            int a = initialHashes[0];
            int b = initialHashes[1];
            int c = initialHashes[2];
            int d = initialHashes[3];
            int e = initialHashes[4];
            int f = initialHashes[5];
            int g = initialHashes[6];
            int h = initialHashes[7];

            // основной алгоритм хэш функции
            for (int j = 0; j < 64; j++) {
                int s1 = Integer.rotateRight(e, 6) ^ Integer.rotateRight(e, 11)
                        ^ Integer.rotateRight(e, 25);
                int t1 = h + s1 + ch(e, f, g) + K[j] + w[j];
                int s0 = Integer.rotateRight(a, 2) ^ Integer.rotateRight(a, 13)
                        ^ Integer.rotateRight(a, 22);
                int t2 = s0 + maj(a, b, c);
                h = g;
                g = f;
                f = e;
                e = d + t1;
                d = c;
                c = b;
                b = a;
                a = t1 + t2;
            }

            initialHashes[0] += a;
            initialHashes[1] += b;
            initialHashes[2] += c;
            initialHashes[3] += d;
            initialHashes[4] += e;
            initialHashes[5] += f;
            initialHashes[6] += g;
            initialHashes[7] += h;
        }

        // Собираем хэш из слов
        ByteBuffer buffer = ByteBuffer.allocate(32);

        for (int value : initialHashes) {
            buffer.putInt(value);
        }

        return buffer.array();
    }

    /**
     * Преобразует последовательность из 512 бит в массив слов по 32 бита
     */
    private int[] splitIntoWords(byte[] block) {
        int[] words = new int[16];

        for (int i = 0; i < 16; i++) {
            byte[] bytes = new byte[4];
            System.arraycopy(block, 4 * i, bytes, 0, 4);
            words[i] = bytesToInt(bytes);
        }

        return words;
    }

    /**
     * Преобразует последовательность из 4 байт в int
     */
    public static int bytesToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |  // сдвигаем на первый байт
                ((bytes[1] & 0xFF) << 16) | // сдвигаем на второй байт
                ((bytes[2] & 0xFF) << 8) |  // сдвигаем на третий байт
                (bytes[3] & 0xFF);          // оставляем на четвертом байте и все соединяем между собой
    }

    /**
     * Расширяет массив из 16 слов до 64 слов
     */
    private int[] expandWords(int[] words) {
        int[] w = new int[64];
        System.arraycopy(words, 0, w, 0, 16);

        for (int i = 16; i < 64; i++) {
            int s0 = Integer.rotateRight(w[i - 15], 7) ^ Integer.rotateRight(w[i - 15], 18)
                    ^ (w[i - 15] >>> 3);
            int s1 = Integer.rotateRight(w[i - 2], 17) ^ Integer.rotateRight(w[i - 2], 19)
                    ^ (w[i - 2] >>> 10);
            w[i] = w[i - 16] + s0 + w[i - 7] + s1;
        }

        return w;
    }

    private int ch(int x, int y, int z) {
        int a = x & y;
        int b = ~x & z;
        return a ^ b;
    }

    private int maj(int x, int y, int z) {
        int a = x & y;
        int b = x & z;
        int c = y & z;
        return a ^ b ^ c;
    }
}
