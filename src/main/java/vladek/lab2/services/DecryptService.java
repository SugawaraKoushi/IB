package vladek.lab2.services;

import org.springframework.stereotype.Service;

@Service("decryptService2")
public class DecryptService implements IDecryptService {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъьыэюя_,.АБВГДЦЕЁЖЗИЙКЛМНОПРСТУФХЦШЩЭЮЯ";

    @Override
    public String decryptFromRailFenceCipher(String text) {
        String part1 = text.substring(0, text.length() / 2);
        String part2 = text.substring(text.length() / 2);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < part1.length(); i++) {
            sb.append(part1.charAt(i));

            if (i < part2.length()) {
                sb.append(part2.charAt(i));
            }
        }

        return sb.toString();
    }

    @Override
    public String decryptFromChangeCipherWithKey(String text, String key) {
        StringBuilder sb = new StringBuilder();
        int groupLen = key.length();

        for (int i = 0; i < text.length(); i += groupLen) {
            String group = text.substring(i, Math.min(i + groupLen, text.length()));
            String[] groupSymbols = new String[key.length()];

            for (int j = 0; j < groupSymbols.length; j++) {
                int keyIndex = Character.getNumericValue(key.charAt(j)) - 1;

                if (j > group.length() - 1) {
                    groupSymbols[keyIndex] = "_";
                } else {
                    groupSymbols[keyIndex] = String.valueOf(group.charAt(j));
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
    public String decryptFromCombineChangeCipherWithKey(String text, String key) {
        StringBuilder sb = new StringBuilder();
        int rows = (int) Math.ceil((double) text.length() / key.length());
        String[][] scrambleTable = new String[rows][key.length()];

        // Преобразуем текст в таблицу, заполняя его по столбцам
        for (int i = 0; i < scrambleTable[0].length; i++) {
            for (int j = 0; j < scrambleTable.length; j++) {
                int index = i * rows + j;
                scrambleTable[j][i] = index < text.length() ? String.valueOf(text.charAt(index)) : "_";
            }
        }

        String[][] table = new String[scrambleTable.length][scrambleTable[0].length];

        // Поменяем местами столбцы согласно ключу
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                int keyPos = Character.getNumericValue(key.charAt(j));
                table[i][keyPos - 1] = scrambleTable[i][j];
            }
        }

        // Формируем строку
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                sb.append(table[i][j]);
            }
        }

        return sb.toString();
    }

    @Override
    public String decryptFromDoubleChangeCipherWithKey(String text, String[] keys) {
        String result = text;

        for (int i = keys.length - 1; i >= 0; i--) {
            result = decryptFromChangeCipherWithKey(result, keys[i]);
        }

        return result;
    }
}
