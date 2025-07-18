package vladek.lab6.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vladek.lab4.services.PrimeNumbersService;
import vladek.lab6.dto.RSAKeyPair;
import vladek.lab6.dto.RSAPrivateKey;
import vladek.lab6.dto.RSAPublicKey;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RSAService {
    private final PrimeNumbersService primeNumbersService;

    /**
     * Генерирует RSA ключи
     * @return RSA ключи
     */
    public RSAKeyPair keyGeneration(BigInteger p, BigInteger q) {
        BigInteger n = p.multiply(q);
        BigInteger phi = eulerFunc(p, q);

        SecureRandom random = new SecureRandom();
        BigInteger e;

        do {
            e = new BigInteger(phi.bitLength(), random);
        } while (!e.gcd(phi).equals(BigInteger.ONE) || e.compareTo(phi) >= 0);

        BigInteger d = euclidAlgorithm(e, phi)[1].mod(phi); // нужно положительное d

        RSAPublicKey publicKey = new RSAPublicKey();
        publicKey.setE(e.toString(16));
        publicKey.setN(n.toString(16));

        RSAPrivateKey privateKey = new RSAPrivateKey();
        privateKey.setD(d.toString(16));
        privateKey.setN(n.toString(16));

        RSAKeyPair keyPair = new RSAKeyPair();
        keyPair.setPublicKey(publicKey);
        keyPair.setPrivateKey(privateKey);

        return keyPair;
    }

    /**
     * Зашифровывает текст при помощи общего RSA ключа
     * @param textBytes текст
     * @param key общий ключ
     * @return байты зашифрованного текст
     */
    public byte[] encrypt(byte[] textBytes, RSAPublicKey key) {
        BigInteger n = new BigInteger(key.getN(), 16);
        BigInteger e = new BigInteger(key.getE(), 16);
        int blockSize = n.bitLength() - 1; // Размер блока в битах
        byte[] modulusBytes = n.toByteArray();
        int outputBlockSize = modulusBytes.length;  // Размер блока в байтах
        ByteArrayOutputStream encryptedBytes = new ByteArrayOutputStream();

        // Разбиваем на блоки и шифруем
        for (int i = 0; i < textBytes.length; i += blockSize / 8) {
            // Берем кусок текста размер blockSize в байтах, или оставшийся кусок
            int chunkLength = Math.min(blockSize / 8, textBytes.length - i);
            // Копируем в кусок в переменную
            byte[] chunk = Arrays.copyOfRange(textBytes, i, i + chunkLength);

            // Говорим, что каждый байт это положительное число, и 8й бит не знаковый, а входит в это число
            BigInteger chunkBigInt = new BigInteger(1, chunk);

            // Шифруем кусок текста
            BigInteger encryptedChunk = chunkBigInt.modPow(e, n);

            byte[] encryptedChunkBytes = encryptedChunk.toByteArray();

            // Если в закодированном куске не хватает байт, дополняем их нулями справа
            if (encryptedChunkBytes.length < outputBlockSize) {
                byte[] padded = new byte[outputBlockSize];
                System.arraycopy(
                        encryptedChunkBytes, 0,
                        padded, outputBlockSize - encryptedChunkBytes.length,
                        encryptedChunkBytes.length
                );
                encryptedChunkBytes = padded;
            }

            // Записываем в буфер
            encryptedBytes.writeBytes(encryptedChunkBytes);
        }

        return encryptedBytes.toByteArray();
    }


    /**
     * Дешифрует текст при помощи секретного RSA ключа
     * @param encryptedData байты зашифрованного текста
     * @param key секретный ключ
     * @return байты дешифрованного текста
     */
    public byte[] decrypt(byte[] encryptedData, RSAPrivateKey key) {
        BigInteger n = new BigInteger(key.getN(), 16);
        BigInteger d = new BigInteger(key.getD(), 16);
        int modulusLength = n.toByteArray().length;
        ByteArrayOutputStream decryptedBytes = new ByteArrayOutputStream();

        // Разбиваем на блоки и расшифровываем
        for (int i = 0; i < encryptedData.length; i += modulusLength) {
            // Выбираем куски закодированного текста длинной n-байт
            byte[] chunk = Arrays.copyOfRange(encryptedData, i, i + modulusLength);

            // Говорим, что каждый байт это положительное число, и 8й бит не знаковый, а входит в это число
            BigInteger chunkBigInt = new BigInteger(1, chunk);

            BigInteger decryptedChunk = chunkBigInt.modPow(d, n);
            byte[] decryptedChunkBytes = decryptedChunk.toByteArray();

            // Удаляем ведущий нулевой байт, если он есть
            if (decryptedChunkBytes[0] == 0 && decryptedChunkBytes.length > 1) {
                decryptedChunkBytes = Arrays.copyOfRange(decryptedChunkBytes, 1, decryptedChunkBytes.length);
            }

            // Запись в буфер
            decryptedBytes.writeBytes(decryptedChunkBytes);
        }

        return decryptedBytes.toByteArray();
    }

    /**
     * Функция эйлера
     */
    private BigInteger eulerFunc(BigInteger p, BigInteger q) {
        return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }

    /**
     * Расширенный алгоритм Евклида
     */
    private BigInteger[] euclidAlgorithm(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[]{
                    a, BigInteger.ONE, BigInteger.ZERO};
        }

        BigInteger[] result = euclidAlgorithm(b, a.mod(b));
        BigInteger gcd = result[0];
        BigInteger x1 = result[1];
        BigInteger y1 = result[2];

        BigInteger x = y1;
        BigInteger y = x1.subtract(a.divide(b).multiply(y1));

        return new BigInteger[]{gcd, x, y};
    }
}

