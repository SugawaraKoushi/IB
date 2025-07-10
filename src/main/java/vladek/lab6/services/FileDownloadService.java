package vladek.lab6.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import vladek.lab6.dto.RSAKeyPair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileDownloadService {
    public byte[] createFileToDownload(RSAKeyPair keyPair) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String publicKeyJson = mapper.writeValueAsString(keyPair.getPublicKey());
        String privateKeyJson = mapper.writeValueAsString(keyPair.getPrivateKey());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);

        zipOut.putNextEntry(new ZipEntry("public_key.json"));
        zipOut.write(publicKeyJson.getBytes(StandardCharsets.UTF_8));
        zipOut.closeEntry();

        zipOut.putNextEntry(new ZipEntry("private_key.json"));
        zipOut.write(privateKeyJson.getBytes(StandardCharsets.UTF_8));
        zipOut.closeEntry();

        zipOut.close();

        return baos.toByteArray();
    }
}
