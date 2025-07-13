package vladek.lab7.services;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class SHA256Service {
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
}
