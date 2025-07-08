package vladek.lab6.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vladek.lab4.services.PrimeNumbersService;
import vladek.lab6.dto.RSAKeyPair;
import vladek.lab6.dto.RSAPrivateKey;
import vladek.lab6.dto.RSAPublicKey;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RSAService {
    private final PrimeNumbersService primeNumbersService;

    public RSAKeyPair keyGeneration() {
        BigInteger p = new BigInteger(primeNumbersService.getRandomPrimeNumber(512, 20));
        BigInteger q;

        do {
            q = new BigInteger(primeNumbersService.getRandomPrimeNumber(512, 20));
        } while (p.equals(q));

        BigInteger n = p.multiply(q);
        BigInteger phi = eulerFunc(p, q);

        SecureRandom random = new SecureRandom();
        BigInteger e;

        do {
            e = new BigInteger(phi.bitLength(), random);
        } while (!e.gcd(phi).equals(BigInteger.ONE) || e.compareTo(phi) >= 0);

        BigInteger d = euclidAlgorithm(e, phi)[1].mod(phi); // нужно положительное d

        System.out.println(e.multiply(d).mod(phi).equals(BigInteger.ONE));

        RSAPublicKey publicKey = new RSAPublicKey();
        publicKey.setE(e);
        publicKey.setN(n);

        RSAPrivateKey privateKey = new RSAPrivateKey();
        privateKey.setD(d);
        privateKey.setN(n);

        RSAKeyPair keyPair = new RSAKeyPair();
        keyPair.setPublicKey(publicKey);
        keyPair.setPrivateKey(privateKey);

        return keyPair;
    }

//    public byte[] encrypt(String text, RSAPublicKey key) {
////        byte[] textBytes = Base64.getEncoder().encode(text.getBytes());
//        byte[] textBytes = text.getBytes();
//        BigInteger textBigInt = new BigInteger(textBytes);
//        int blockSize = key.getN().bitLength() - 1;
//        int chunksCount = (int) Math.ceil((double) textBigInt.bitLength() / blockSize);
//        List<Byte> encryptedBytesList = new ArrayList<>();
//
//        for (int i = 0; i < chunksCount; i++) {
//            BigInteger chunk = textBigInt.shiftRight(i * blockSize);
//
//            for (int j = blockSize; j < chunk.bitLength(); j++) {
//                chunk = chunk.clearBit(j);
//            }
//
//            chunk = chunk.modPow(key.getE(), key.getN());
//
//            for(byte b : chunk.toByteArray()) {
//                encryptedBytesList.add(b);
//            }
//        }
//
//        byte[] bytes = new byte[encryptedBytesList.size()];
//        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] = encryptedBytesList.get(i);
//        }
//
//        return bytes;
//    }
//
//    public String decrypt(byte[] text, RSAPrivateKey key) {
//        BigInteger textBigInt = new BigInteger(text);
//        int blockSize = key.getN().bitLength() - 1;
//        int chunksCount = (int) Math.ceil((double) textBigInt.bitLength() / blockSize);
//        List<Byte> decryptedBytesList = new ArrayList<>();
//
//        for (int i = 0; i < chunksCount; i++) {
//            BigInteger chunk = textBigInt.shiftRight(i * blockSize);
//
//            for (int j = blockSize; j < chunk.bitLength(); j++) {
//                chunk = chunk.clearBit(j);
//            }
//
//            chunk = chunk.modPow(key.getD(), key.getN());
//
//            for(byte b : chunk.toByteArray()) {
//                decryptedBytesList.add(b);
//            }
//        }
//
//        byte[] bytes = new byte[decryptedBytesList.size()];
//
//        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] = decryptedBytesList.get(i);
//        }
//

    /// /        bytes = Base64.getDecoder().decode(bytes);
//
//        return new String(bytes);
//    }
    public byte[] encrypt(String text, RSAPublicKey key) {
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        int blockSize = key.getN().bitLength() - 1; // Размер блока в битах
        byte[] modulusBytes = key.getN().toByteArray();
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
            BigInteger encryptedChunk = chunkBigInt.modPow(key.getE(), key.getN());

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

    public String decrypt(byte[] encryptedData, RSAPrivateKey key) {
        int modulusLength = key.getN().toByteArray().length;
        ByteArrayOutputStream decryptedBytes = new ByteArrayOutputStream();

        // Разбиваем на блоки и расшифровываем
        for (int i = 0; i < encryptedData.length; i += modulusLength) {
            // Выбираем куски закодированного текста длинной n-байт
            byte[] chunk = Arrays.copyOfRange(encryptedData, i, i + modulusLength);

            // Говорим, что каждый байт это положительное число, и 8й бит не знаковый, а входит в это число
            BigInteger chunkBigInt = new BigInteger(1, chunk);

            BigInteger decryptedChunk = chunkBigInt.modPow(key.getD(), key.getN());
            byte[] decryptedChunkBytes = decryptedChunk.toByteArray();

            // Удаляем ведущий нулевой байт, если он есть
            if (decryptedChunkBytes[0] == 0 && decryptedChunkBytes.length > 1) {
                decryptedChunkBytes = Arrays.copyOfRange(decryptedChunkBytes, 1, decryptedChunkBytes.length);
            }

            // Запись в буфер
            decryptedBytes.writeBytes(decryptedChunkBytes);
        }

        return decryptedBytes.toString(StandardCharsets.UTF_8);
    }

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

