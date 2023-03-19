import java.io.*;
import java.util.*;

public class WordCount {

    private static final char LINE_END = '\n';
    private static final int LINE_BUFFER_SIZE = 64 * 1024;
    private static final int CHAR_BUFFER_SIZE = 200;

    private Map<String, Integer> wc  = new HashMap<>();

    private Reader reader;

    private Writer writer;

    public WordCount(Reader reader, Writer writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void process() throws IOException {
        try (BufferedReader reader = new BufferedReader(this.reader, LINE_BUFFER_SIZE)) {
            char[] lineBuffer = new char[LINE_BUFFER_SIZE];
            StringBuilder wordBuffer = new StringBuilder(CHAR_BUFFER_SIZE);
            int nrChars;
            while((nrChars =  reader.read(lineBuffer)) != -1) {
                for (int i = 0; i < nrChars; i++) {
                    char currentChar = lineBuffer[i];
                    if (Character.isLetter(currentChar)) {
                        wordBuffer.append(Character.toLowerCase(currentChar));
                    } else if (Character.isSpaceChar(currentChar) || currentChar == LINE_END) {
                        if (wordBuffer.length() > 0) {
                            String word = wordBuffer.toString();
                            wc.put(word, wc.getOrDefault(word, 0) + 1);
                            // reset char buffer
                            wordBuffer.setLength(0);
                        }
                    } else {
                        wordBuffer.append(currentChar);
                    }
                }
            }

            // last word
            if (wordBuffer.length() > 0) {
                String word = wordBuffer.toString();
                wc.put(word, wc.getOrDefault(word, 0) + 1);
            }
        }
    }

    private void output() throws IOException {
        List<Map.Entry<String, Integer>> countsAsList = new ArrayList<>(wc.entrySet());
        Collections.sort(countsAsList, Map.Entry.comparingByValue(Comparator.reverseOrder()));

        for (Map.Entry<String, Integer> e : countsAsList) {
            writer.write(e.getKey());
            writer.write(" ");
            writer.write(e.getValue());
            writer.write(LINE_END);
        }

        writer.flush();
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        String inputFilePath = "/Users/wangguiyong/Desktop/english_camp.txt";
        InputStreamReader input = new InputStreamReader(new FileInputStream(inputFilePath));
        String outputPath = "/Users/wangguiyong/Desktop/english_camp_word_frequency.txt";
        OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(outputPath));
        WordCount wc = new WordCount(input, output);
        wc.process();
        wc.output();
    }







}
