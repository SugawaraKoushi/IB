package vladek.lab6.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vladek.lab6.dto.*;
import vladek.lab6.services.FileDownloadService;
import vladek.lab6.services.FileUploadService;
import vladek.lab6.services.RSAService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.RSAKey;
import java.util.Base64;

@RestController
@RequestMapping("/api/lab6")
@RequiredArgsConstructor
public class RSAController {
    private final FileUploadService fileUploadService;
    private final FileDownloadService fileDownloadService;
    private final RSAService rsaService;

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
        BigInteger pBig = new BigInteger(p);
        BigInteger qBig = new BigInteger(q);
        RSAKeyPair keyPair = rsaService.keyGeneration(pBig, qBig);

        try {
            byte[] zipBytes = fileDownloadService.createFileToDownload(keyPair);
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
    public ResponseEntity<String> encrypt(@RequestBody EncryptRequest request) {
        request.getKey().setE(request.getKey().getE().replace("n", ""));
        request.getKey().setN(request.getKey().getN().replace("n", ""));
        byte[] encryptedTextBytes = rsaService.encrypt(request.getText(), request.getKey());
        String result = Base64.getEncoder().encodeToString(encryptedTextBytes);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> encrypt(@RequestBody DecryptRequest request) {
        request.getKey().setD(request.getKey().getD().replace("n", ""));
        request.getKey().setN(request.getKey().getN().replace("n", ""));
        byte[] bytes = Base64.getDecoder().decode(request.getText());
        String decryptedText = rsaService.decrypt(bytes, request.getKey());
        return new ResponseEntity<>(decryptedText, HttpStatus.OK);
    }
}
