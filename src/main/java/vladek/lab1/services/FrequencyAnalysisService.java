package vladek.lab1.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FrequencyAnalysisService implements IFrequencyAnalysisService{
    @Override
    public HashMap<String, Integer> calculateFrequencies(String text) {
        HashMap<String, Integer> result = new HashMap<>();

        for (int i = 0; i < text.length(); i++) {
            String symbol = String.valueOf(text.charAt(i));

            if (result.containsKey(symbol)) {
                result.put(symbol, result.get(symbol) + 1);
            } else {
                result.put(symbol, 1);
            }
        }

        return result;
    }
}
