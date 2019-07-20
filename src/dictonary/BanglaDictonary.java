package dictonary;

import java.util.ArrayList;
import java.util.Arrays;

import util.Utils;

public class BanglaDictonary {

	private ArrayList<String> wordList;// = new ArrayList<>();

	public BanglaDictonary() {
		wordList = Utils.FILE_READ_WRITER.readStringsFromFile("data/braille.dict.txt");
	}

	public void test() {
		Utils.FILE_READ_WRITER.writeOutput(wordList, Utils.OUTPUT_FILE_NAME);
	}

	public int calculate(String x, String y) {
        if (x.isEmpty()) {
            return y.length();
        }

        if (y.isEmpty()) {
            return x.length();
        }

        int substitution = calculate(x.substring(1), y.substring(1))
         + costOfSubstitution(x.charAt(0), y.charAt(0));
        int insertion = calculate(x, y.substring(1)) + 1;
        int deletion = calculate(x.substring(1), y) + 1;

        return min(substitution, insertion, deletion);
    }

    public int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public int min(int... numbers) {
        return Arrays.stream(numbers)
          .min().orElse(Integer.MAX_VALUE);
    }
    
    public int calculateDP(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];
     
        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i - 1][j - 1] 
                     + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
                      dp[i - 1][j] + 1, 
                      dp[i][j - 1] + 1);
                }
            }
        }
     
        return dp[x.length()][y.length()];
    }

    public String getWordWithLessEditDistance(String word1, String word2) {

	    String selectedWord = word2;
	    int wordListSize = wordList.size();

	    for(int i = 0; i < wordListSize; i++) {

	        String listedWord = wordList.get(i);
	        int firstWordEditDistance = calculateDP(word1, listedWord);
	        int secondWordEditDistance = calculateDP(word2, listedWord);

	        if(firstWordEditDistance <= secondWordEditDistance)
	            selectedWord = word1;
	        else
	            selectedWord = word2;
        }

	    return selectedWord;
    }

}
