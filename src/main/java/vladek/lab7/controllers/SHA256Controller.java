package vladek.lab7.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab7.services.SHA256Service;

import java.util.Base64;

@RestController
@RequestMapping("/api/lab7/sha-256")
@RequiredArgsConstructor
public class SHA256Controller {
    private final SHA256Service sha256Service;

    @GetMapping("/get")
    public ResponseEntity<String> getHashCode(@RequestParam String text) {
        byte[][] blocks = sha256Service.prepareMessageBlocks(text);
        byte[] hashBytes = sha256Service.getHashCode(blocks);
        String result = Hex.encodeHexString(hashBytes);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
