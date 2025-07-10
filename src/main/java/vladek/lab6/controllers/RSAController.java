package vladek.lab6.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vladek.lab3.services.AESDecryptService;
import vladek.lab3.services.AESEncryptService;
import vladek.lab6.dto.*;
import vladek.lab6.services.FileDownloadService;
import vladek.lab6.services.FileUploadService;
import vladek.lab6.services.RSAService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;

@RestController
@RequestMapping("/api/lab6")
@RequiredArgsConstructor
public class RSAController {
    private final FileUploadService fileUploadService;
    private final FileDownloadService fileDownloadService;
    private final RSAService rsaService;
    private final AESEncryptService aesEncryptService;
    private final AESDecryptService aesDecryptService;

    @PostMapping("/upload-file")
    public ResponseEntity<InputFileResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        InputFileResponse response;

        try {
            response = fileUploadService.parseJsonFile(file);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-keys")
    public ResponseEntity<KeyPairResponse> getRSAKeys(@RequestParam String p, @RequestParam String q) {
        BigInteger pBig = new BigInteger(p, 16);
        BigInteger qBig = new BigInteger(q, 16);
        RSAKeyPair keyPair = rsaService.keyGeneration(pBig, qBig);

        try {
            byte[] zipBytes = fileDownloadService.createRSAKeysZipArchive(keyPair);
            String zipBase64 = Base64.getUrlEncoder().encodeToString(zipBytes);

            KeyPairResponse response = new KeyPairResponse();
            response.setPublicKey(keyPair.getPublicKey());
            response.setPrivateKey(keyPair.getPrivateKey());
            response.setZipFile(zipBase64);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/encrypt")
    public ResponseEntity<EncryptResponse> encrypt(@RequestBody EncryptRequest request) {
        String encryptedText = aesEncryptService.encryptByAES128(request.getText(), request.getAesKey(), 2);
        byte[] encryptedAESKeyBytes = rsaService.encrypt(request.getAesKey(), request.getRsaPublicKey());
        String encryptedAESKey = Base64.getEncoder().encodeToString(encryptedAESKeyBytes);

        try {
            byte[] zipBytes = fileDownloadService.createAESEncryptionZipArchive(encryptedAESKey, encryptedText);
            String zipBase64 = Base64.getUrlEncoder().encodeToString(zipBytes);

            EncryptResponse response = new EncryptResponse();
            response.setEncryptedText(encryptedText);
            response.setEncryptedKey(encryptedAESKey);
            response.setZip(zipBase64);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<DecryptResponse> decrypt(@RequestBody DecryptRequest request) {
        byte[] encryptedAesKeyBytes = Base64.getDecoder().decode(request.getEncryptedAESKey());
        String decryptedAESKey = rsaService.decrypt(encryptedAesKeyBytes, request.getRsaPrivateKey());
        String decryptedText = aesDecryptService.decryptFromAES128(request.getText(), decryptedAESKey, 2);

        try {
            byte[] zipBytes = fileDownloadService.createAESDecryptionZipArchive(decryptedText);
            String zipBase64 = Base64.getUrlEncoder().encodeToString(zipBytes);

            DecryptResponse response = new DecryptResponse();
            response.setDecryptedText(decryptedText);
            response.setZip(zipBase64);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
