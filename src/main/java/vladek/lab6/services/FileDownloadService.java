package vladek.lab6.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import vladek.lab6.dto.EncryptResponse;
import vladek.lab6.dto.RSAKeyPair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileDownloadService {

    /**
     * Создает последователь байт для zip-архива с общим и секретным RSA ключами
     */
    public byte[] createRSAKeysZipArchive(RSAKeyPair keyPair) throws IOException {
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

    /**
     * Создает последовательность байт для zip-архива с зашифрованным
     * через RSA AES-ключом и зашифрованным через AES текстом
     *
     * @param key  AES-ключ
     * @param text зашифрованный текст
     */
    public byte[] createAESEncryptionZipArchive(String key, String text) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String aesKey = mapper.writeValueAsString(key);
        String jsonText = mapper.writeValueAsString(text);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);

        zipOut.putNextEntry(new ZipEntry("aes_key.json"));
        zipOut.write(aesKey.getBytes(StandardCharsets.UTF_8));
        zipOut.closeEntry();

        zipOut.putNextEntry(new ZipEntry("encrypted_text.json"));
        zipOut.write(jsonText.getBytes(StandardCharsets.UTF_8));
        zipOut.closeEntry();

        zipOut.close();

        return baos.toByteArray();
    }

    /**
     * Создает последовательность байт для дешифрованного через AES текста
     * @param text дешифрованный текст
     */
    public byte[] createAESDecryptionZipArchive(String text) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonText = mapper.writeValueAsString(text);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);

        zipOut.putNextEntry(new ZipEntry("decrypted_text.json"));
        zipOut.write(jsonText.getBytes(StandardCharsets.UTF_8));
        zipOut.closeEntry();

        zipOut.close();

        return baos.toByteArray();
    }

}
