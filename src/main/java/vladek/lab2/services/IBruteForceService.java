package vladek.lab2.services;

import vladek.lab2.dto.BruteForceResponse;

public interface IBruteForceService {
    BruteForceResponse bruteForceCombineCipher(String text, String lastKey);
}
