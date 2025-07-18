package vladek.lab7.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import vladek.lab6.dto.RSAPrivateKey;
import vladek.lab6.dto.RSAPublicKey;
import vladek.lab6.services.RSAService;
import vladek.lab7.dto.RSADigitalSignResponse;

@Service
@RequiredArgsConstructor
public class RSADigitalSignService {
    private final RSAService rsaService;
    private final SHA256Service sha256Service;

    public RSADigitalSignResponse getSign(String message, RSAPublicKey publicKey) {
        byte[][] blocks = sha256Service.prepareMessageBlocks(message);
        byte[] hashBytes = sha256Service.getHashCode(blocks);
        byte[] signBytes = rsaService.encrypt(hashBytes, publicKey);
        RSADigitalSignResponse response = new RSADigitalSignResponse();
        response.setHashCode(Hex.encodeHexString(hashBytes));
        response.setSign(Base64.encodeBase64String(signBytes));
        return response;
    }

    public String checkSign(String sign, RSAPrivateKey privateKey) {
        byte[] signBytes = Base64.decodeBase64(sign);
        byte[] hashBytes = rsaService.decrypt(signBytes, privateKey);
        return Hex.encodeHexString(hashBytes);
    }
}
