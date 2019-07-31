package dictonary;

import java.util.ArrayList;
import java.util.Arrays;

import dataStructure.Word;
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

	    int minimumEditDistance = 1000;
    	String selectedWord = word2;
	    int wordListSize = wordList.size();


	    for(int i = 0; i < wordListSize; i++) {

	        String listedWord = wordList.get(i);
	        int firstWordEditDistance = calculateDP(word1, listedWord);
	        int secondWordEditDistance = calculateDP(word2, listedWord);

	        if(firstWordEditDistance < minimumEditDistance) {
	        	minimumEditDistance = firstWordEditDistance;
	        	selectedWord = word1;
//	        	selectedWord = listedWord;
	        }

	        if(secondWordEditDistance < minimumEditDistance) {
	        	minimumEditDistance = secondWordEditDistance;
	        	selectedWord = word2;
//	        	selectedWord = listedWord;
	        }

        }

	    return selectedWord;
    }


    public int getEditDistance(String word) {
    	int wordListSize = wordList.size();
    	int editDistance = word.length() + 100;
    	for(int i = 0; i < wordListSize; i++) {
    		
    		 String listedWord = wordList.get(i);
    		 int distance = calculate(word, listedWord);
    		 if(editDistance > distance)
    			 editDistance = distance;
    	
    	}

    	return editDistance;
    }

    public void calculate_edit_distance(ArrayList<Word> collected_word){
    	int word_list_size = wordList.size();
    	int collected_word_size = collected_word.size();

    	for(int i = 0; i < word_list_size; i++) {
    		String listed_word = wordList.get(i);
    		
    		for(int j = 0; j < collected_word_size; j ++) {
    			Word word = collected_word.get(j);
    			int edit_distance = 1000;
    			String selected_word = "";
    			
    			int first_word_edit_distance = calculateDP(listed_word, word.get_first_word());
    			int second_word_edit_distance = calculateDP(listed_word, word.get_second_word());
    			
    			if(first_word_edit_distance < edit_distance) {
    	        	edit_distance = first_word_edit_distance;
    	        	selected_word = word.get_first_word();

    	        }

    	        if(second_word_edit_distance < edit_distance) {
    	        	edit_distance = second_word_edit_distance;
    	        	selected_word = word.get_second_word();
    	        }
    	        
    	        word.set_edit_distance(edit_distance);
    	        word.set_selected_word(selected_word);
    		}
    	}
    }



}
