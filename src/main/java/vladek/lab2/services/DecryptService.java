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
            String[] groupSymbols = new String[group.length()];

            for (int j = 0; j < groupSymbols.length; j++) {
                int keyIndex = Integer.parseInt(String.valueOf(key.charAt(j))) - 1;
                groupSymbols[keyIndex] = String.valueOf(group.charAt(j));
            }

            sb.append(String.join("", groupSymbols));
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
                scrambleTable[j][i] = String.valueOf(text.charAt(index));
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
}
