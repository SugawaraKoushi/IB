package vladek.lab1.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EncryptService implements IEncryptService {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъьыэюя_,.";

    @Override
    public String encryptByAdditiveCipherWithShift(String text, int shift) {
        String str = text.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int newSymbolIndex = (ALPHABET.indexOf(str.charAt(i)) + shift) % ALPHABET.length();
            sb.append(ALPHABET.charAt(newSymbolIndex));
        }

        return sb.toString();
    }

    @Override
    public String encryptByMultiplicativeCipherWithShift(String text, int shift) {
        String str = text.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int newSymbolIndex = ((ALPHABET.indexOf(str.charAt(i))) * shift);
            newSymbolIndex = newSymbolIndex % ALPHABET.length();
            newSymbolIndex = newSymbolIndex == 0 ? ALPHABET.length() - 1 : newSymbolIndex;

            sb.append(ALPHABET.charAt(newSymbolIndex));
        }

        return sb.toString();
    }

    @Override
    public String encryptByPlayfairCipher(String text, String key) {
        // Сформируем матрицу с кодовым слово
        String[][] matrix = getMatrix(key);

        // Разобъем текст на биграммы
        String[][] bigrams = getBigrams(text);

        if (bigrams[bigrams.length - 1][1] == null) {
            bigrams[bigrams.length - 1][1] = "я";
        }

        // Зашифруем текст
        StringBuilder sb = new StringBuilder();

        for (String[] bigram : bigrams) {
            int[] left = getSymbolPosition(matrix, bigram[0]);
            int[] right = getSymbolPosition(matrix, bigram[1]);

            if (left[0] == right[0]) {
                // Если символы биграммы находятся в одной строке матрицы,
                // берем символ из соседней правой колонки. Если символ последний -
                // берем символ из первой колонки

                sb.append(matrix[left[0]][(left[1] + 1) % matrix.length])
                        .append(matrix[right[0]][(right[1] + 1) % matrix.length]);
            } else if (left[1] == right[1]) {
                // Если символы биграммы находятся в одном столбце матрицы,
                // берем символ из соседней нижней строки. Если символ последний -
                // берем символ из первой строки

                sb.append(matrix[(left[0] + 1) % matrix.length][left[1]])
                        .append(matrix[(right[0] + 1) % matrix.length][right[1]]);
            } else {
                // Если символы биграммы находятся в разных столбцах и строках,
                // берем символ из противоположного угла строки символа образовавшегося прямоугольника

                sb.append(matrix[left[0]][right[1]]).append(matrix[right[0]][left[1]]);
            }
        }

        return sb.toString();
    }

    private String[][] getMatrix(String key) {
        String[][] matrix = new String[6][6];
        int fromAlphabetIndex = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int index = i * matrix.length + j;

                if (key == null || key.isEmpty() || index >= key.length()) {
                    String fromAlphabet = String.valueOf(ALPHABET.charAt(fromAlphabetIndex));

                    if (key != null && !key.isEmpty()) {
                        while (key.contains(fromAlphabet)) {
                            fromAlphabetIndex++;
                            fromAlphabet = String.valueOf(ALPHABET.charAt(fromAlphabetIndex));
                        }
                    }

                    matrix[i][j] = fromAlphabet;
                    fromAlphabetIndex++;
                } else {
                    matrix[i][j] = String.valueOf(key.charAt(index));
                }
            }
        }

        return matrix;
    }

    private String[][] getBigrams(String text) {
        int bigramsCount = (int) Math.ceil(text.length() / 2.0);
        String[][] bigrams = new String[bigramsCount][2];

        for (int i = 0; i < bigrams.length; i++) {
            bigrams[i][0] = String.valueOf(text.charAt(i * 2));

            if (i * 2 + 1 < text.length()) {
                bigrams[i][1] = String.valueOf(text.charAt(i * 2 + 1));
            }
        }

        String[] bigram;
        int i = 0;
        boolean symbolsAreEqual;

        // Проверим каждую биграмму на совпадение символов. Если символы совпадают,
        // вставляем между ними символ "я", формируем новую строку и составляем биграммы заново
        do {
            bigram = bigrams[i];
            symbolsAreEqual = bigram[0].equals(bigram[1]);

            if (symbolsAreEqual) {
                String left = text.substring(0, i * 2 + 1);
                String right = text.substring(i * 2 + 1);
                String newText = "".concat(left).concat("я").concat(right);
                bigrams = getBigrams(newText);
            }

            i++;
        } while (i < bigrams.length && !symbolsAreEqual);

        return bigrams;
    }

    private int[] getSymbolPosition(String[][] matrix, String symbol) {
        int row = -1;
        int column;

        do {
            row++;
            column = Arrays.asList(matrix[row]).indexOf(symbol);
        } while (row < matrix.length && column == -1);

        return new int[]{row, column};
    }
}
