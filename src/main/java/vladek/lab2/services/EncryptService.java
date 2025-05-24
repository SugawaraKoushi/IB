package vladek.lab2.services;

import org.springframework.stereotype.Service;

@Service("encryptService2")
public class EncryptService implements IEncryptService {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъьыэюя_,.АБВГДЦЕЁЖЗИЙКЛМНОПРСТУФХЦШЩЭЮЯ";

    @Override
    public String encryptByRailFenceCipher(String text) {
        String[] railFence = new String[] {"", ""};

        for (int i = 0; i < text.length(); i++) {
            if (i % 2 == 0) {
                railFence[0] = railFence[0].concat(String.valueOf(text.charAt(i)));
            } else {
                railFence[1] = railFence[1].concat(String.valueOf(text.charAt(i)));
            }
        }

        return "".concat(railFence[0]).concat(railFence[1]);
    }
}
