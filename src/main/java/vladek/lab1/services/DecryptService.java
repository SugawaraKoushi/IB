package vladek.lab1.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service("decryptService1")
public class DecryptService implements IDecryptService {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъьыэюя_,.";

    @Override
    public String decryptFromAdditiveCipherWithShift(String text, int shift) {
        String str = text.toLowerCase();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            int newSymbolIndex = getSymbolPosition(ALPHABET.indexOf(str.charAt(i)), shift);
            sb.append(ALPHABET.charAt(newSymbolIndex));
        }

        return sb.toString();
    }

    @Override
    public String decryptFromMultiplicativeCipherWithShift(String text, int shift) {
        String str = text.toLowerCase();
        StringBuilder sb = new StringBuilder();
        int alphabetLen = ALPHABET.length();

        int inverseShift = findMultiplicativeInverse(shift, alphabetLen);

        if (inverseShift == -1) {
            throw new IllegalArgumentException("Shift " + shift + " не имеет обратного по модулю " + alphabetLen);
        }

        for (int i = 0; i < text.length(); i++) {
            int newSymbolIndex = ((ALPHABET.indexOf(str.charAt(i))) * inverseShift) % (alphabetLen);
            sb.append(ALPHABET.charAt(newSymbolIndex));
        }

        return sb.toString();
    }

    @Override
    public String decryptFromPlayfairCipher(String text, String key) {
        // Получим матрицу с ключевым словом
        String[][] matrix = getMatrix(key);

        // Сформируем биграммы из зашифрованного текста
        String[][] bigrams = getBigrams(text);

        StringBuilder sb = new StringBuilder();

        // Инвертируем примененные при шифровании правила
        for (String[] bigram : bigrams) {
            int[] left = getSymbolPosition(matrix, bigram[0]);
            int[] right = getSymbolPosition(matrix, bigram[1]);

            if (left[0] == right[0]) {
                // Если символы биграммы находятся в одной строке матрицы,
                // берем символ из соседней левой колонки. Если символ первый -
                // берем символ из последней колонки

                if (left[1] == 0) {
                    sb.append(matrix[left[0]][matrix.length - 1]);
                } else {
                    sb.append(matrix[left[0]][left[1] - 1]);
                }

                if (right[1] == 0) {
                    sb.append(matrix[right[0]][matrix.length - 1]);
                } else {
                    sb.append(matrix[right[0]][right[1] - 1]);
                }
            } else if (left[1] == right[1]) {
                // Если символы биграммы находятся в одном столбце матрицы,
                // берем символ из соседней верхней строки. Если символ первый -
                // берем символ из последней строки

                if (left[0] == 0) {
                    sb.append(matrix[matrix.length - 1][left[1]]);
                } else {
                    sb.append(matrix[left[0] - 1][left[1]]);
                }

                if (right[0] == 0) {
                    sb.append(matrix[matrix.length - 1][right[1]]);
                } else {
                    sb.append(matrix[right[0] - 1][right[1]]);
                }
            } else {
                // Если символы биграммы находятся в разных столбцах и строках,
                // берем символ из противоположного угла строки символа образовавшегося прямоугольника

                sb.append(matrix[left[0]][right[1]]).append(matrix[right[0]][left[1]]);
            }
        }

        // Избавимся от добавленных лишних символов "я"
        String result = sb.toString();
        sb = new StringBuilder();
        bigrams = getBigrams(result);

        for (int i = 0; i < bigrams.length - 1; i++) {
            String[] current = bigrams[i];
            String[] next = bigrams[i + 1];

            if (current[0].equals(next[0]) && current[1].equals("я")) {
                sb.append(current[0]);
            } else {
                sb.append(current[0]).append(current[1]);
            }
        }

        sb.append(bigrams[bigrams.length - 1][0]).append(bigrams[bigrams.length - 1][1]);

        if (sb.toString().length() % 2 != 0) {
            return sb.toString().substring(0, sb.toString().length() - 1);
        } else {
            return sb.toString();
        }
    }

    private int findMultiplicativeInverse(int a, int m) {
        a = a % m;

        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }

        return -1;
    }

    private int getSymbolPosition(int pos, int shift) {
        int absolutePos = pos - shift;

        if (absolutePos < 0) {
            absolutePos += ALPHABET.length();
        }

        return absolutePos % ALPHABET.length();
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
        int bigramsCount = text.length() / 2;
        String[][] bigrams = new String[bigramsCount][2];

        for (int i = 0; i < bigrams.length; i++) {
            bigrams[i][0] = String.valueOf(text.charAt(i * 2));
            bigrams[i][1] = String.valueOf(text.charAt(i * 2 + 1));
        }

//        for (int i = 0; i < bigrams.length; i++) {
//            bigrams[i][0] = String.valueOf(text.charAt(i * 2));
//
//            if ((i * 2 + 1) >= text.length()) {
//                bigrams[i][1] = "я";
//            } else {
//                bigrams[i][1] = String.valueOf(text.charAt(i * 2 + 1));
//            }
//        }
//
//        String[] bigram;
//        int i = 0;
//        boolean symbolsAreEqual;
//
//        // Проверим каждую биграмму на совпадение символов. Если символы совпадают,
//        // вставляем между ними символ "я", формируем новую строку и составляем биграммы заново
//        do {
//            bigram = bigrams[i];
//            symbolsAreEqual = bigram[0].equals(bigram[1]);
//
//            if (symbolsAreEqual) {
//                String left = text.substring(0, i * 2 + 1);
//                String right = text.substring(i * 2 + 1);
//                String newText = "".concat(left).concat("я").concat(right);
//                bigrams = getBigrams(newText);
//            }
//
//            i++;
//        } while (i < bigrams.length && !symbolsAreEqual);

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
