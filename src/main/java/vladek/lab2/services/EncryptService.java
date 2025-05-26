package vladek.lab2.services;

import org.springframework.stereotype.Service;

@Service("encryptService2")
public class EncryptService implements IEncryptService {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъьыэюя_,.АБВГДЦЕЁЖЗИЙКЛМНОПРСТУФХЦШЩЭЮЯ";

    @Override
    public String encryptByRailFenceCipher(String text) {
        String[] railFence = new String[]{"", ""};

        for (int i = 0; i < text.length(); i++) {
            if (i % 2 == 0) {
                railFence[0] = railFence[0].concat(String.valueOf(text.charAt(i)));
            } else {
                railFence[1] = railFence[1].concat(String.valueOf(text.charAt(i)));
            }
        }

        return "".concat(railFence[0]).concat(railFence[1]);
    }

    @Override
    public String encryptByChangeCipherWithKey(String text, String key) {
        StringBuilder sb = new StringBuilder();
        int groupSize = key.length();

        for (int i = 0; i < text.length(); i += groupSize) {
            String group = text.substring(i, Math.min(i + groupSize, text.length()));
            String[] groupSymbols = new String[key.length()];

            for (int j = 0; j < key.length(); j++) {
                int keyIndex = Character.getNumericValue(key.charAt(j)) - 1;

                if (keyIndex > group.length() - 1) {
                    groupSymbols[j] = "_";
                } else {
                    groupSymbols[j] = String.valueOf(group.charAt(keyIndex));
                }
            }

            for (String symbol : groupSymbols) {
                String ch = symbol == null ? "_" : symbol;
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    @Override
    public String encryptByCombineChangeCipherWithKey(String text, String key) {
        StringBuilder sb = new StringBuilder();
        int rows = (int) Math.ceil((double) text.length() / key.length());
        String[][] table = new String[rows][key.length()];
        int rowSize = key.length();

        // Заполняем таблицу построчно
        for (int i = 0; i < text.length(); i += rowSize) {
            String row = text.substring(i, Math.min(i + rowSize, text.length()));

            for (int j = 0; j < row.length(); j++) {
                table[i / rowSize][j] = String.valueOf(row.charAt(j));
            }
        }

        String[][] scrambledtable = new String[table.length][table[0].length];

        // Поменяем местами колонки согласно ключу
        for (int i = 0; i < scrambledtable.length; i++) {
            for (int j = 0; j < scrambledtable[i].length; j++) {
                int keyPos = Character.getNumericValue(key.charAt(j));
                scrambledtable[i][j] = table[i][keyPos - 1];
            }
        }

        // Формируем строку
        for (int i = 0; i < scrambledtable[0].length; i++) {
            for (int j = 0; j < scrambledtable.length; j++) {
                String ch = scrambledtable[j][i] == null ? "_" : scrambledtable[j][i];
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    @Override
    public String encryptByDoubleChangeCipherWithKey(String text, String[] keys) {
        String result = text;

        for (String key : keys) {
            result = encryptByChangeCipherWithKey(result, key);
        }

        return result;
    }
}
