package vladek.lab6.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vladek.lab6.dto.InputFileResponse;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

@Service
public class FileUploadService {
    /**
     * Считывает из файла параметры p и q для генерации RSA ключей
     */
    public InputFileResponse parseJsonFile(MultipartFile file) throws IOException {
        File temp = File.createTempFile("temp-", null);
        file.transferTo(temp);

        ObjectMapper mapper = new ObjectMapper();
        InputFileResponse response;
        response = mapper.readValue(temp, InputFileResponse.class);

        response.setP(new BigInteger(response.getP(), 2).toString());
        response.setQ(new BigInteger(response.getQ(), 2).toString());

        temp.delete();
        return response;
    }
}
