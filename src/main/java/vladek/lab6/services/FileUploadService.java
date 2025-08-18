package vladek.lab6.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vladek.lab4.services.PrimeNumbersService;
import vladek.lab6.dto.InputFileResponse;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final PrimeNumbersService primeNumbersService;

    /**
     * Считывает из файла параметры p и q для генерации RSA ключей
     */
    public InputFileResponse parseJsonFile(MultipartFile file) throws IOException {
        File temp = File.createTempFile("temp-", null);
        file.transferTo(temp);

        ObjectMapper mapper = new ObjectMapper();
        InputFileResponse response;
        response = mapper.readValue(temp, InputFileResponse.class);

        BigInteger p = new BigInteger(response.getP(), 2);
        BigInteger q = new BigInteger(response.getQ(), 2);

        // проверим на простоту
        if (primeNumbersService.millerRabinTest(p, 20).getNumberType() < 0) {
            temp.delete();
            throw new IllegalArgumentException("Число p не является простым");
        }

        if (primeNumbersService.millerRabinTest(q, 20).getNumberType() < 0) {
            temp.delete();
            throw new IllegalArgumentException("Число q не является простым");
        }

        response.setP(new BigInteger(response.getP(), 2).toString(16));
        response.setQ(new BigInteger(response.getQ(), 2).toString(16));

        temp.delete();
        return response;
    }
}
