import java.io.*;
import java.util.*;

public class WordSearch {
    private char[][]data;
    private Random randgen;
    private ArrayList<String> wordsToAdd = new ArrayList<String>();
    private ArrayList<String> wordsAdded = new ArrayList<String>();
    private static boolean ansKey;
    private static int seed;
    private int rows;
    private int cols;
    static String errorMsg = "The input should be in this format: java WordSearch [rows columns filename [seed [answers]]]. \nTo see the answers, please type 'key' after your seed input";
    
	public WordSearch(int rows, int cols, String fileName, int randSeed,boolean ansKey){
    	this.rows = rows;
    	this.cols = cols;
    	data = new char[rows][cols];
    	seed = randSeed;
    	randgen = new Random(seed);
    	try{
    		Scanner file = new Scanner(new FileReader(fileName));
    		 while(file.hasNext()) {
    			 wordsToAdd.add(file.next().toUpperCase());
             }   
		 clear();
		 if (ansKey){		     
		     fillWithWords();
		 }
		 else{
		     fillWithWords();
		     fillUpRest();
		 }
    	} catch (IOException e){
    		System.out.println("Misread Stuff");
    	}
    }
    private void clear(){
    	for (int i = 0; i < rows; i ++){
    		for (int j = 0; j < cols; j ++){
    			data[i][j] = '_';
    		}
    	}
    }

    public String toString(){
    	String output = "|";
		for(int i = 0; i<rows; i++)
		{
		    for(int j = 0; j<cols; j++)
		    {
			if (data[i][j]=='\u0000'){
			    output += " |";
			}
			else{
			    output += data[i][j] + "|";
			}
		    }
		    if (i != cols -1){
		    	output += "\n" + "|";
		    }
		}
		output += "\n\nThe seed is " + seed + ".\n\n";
		output += "The words in the puzzle are: \n";
		for (int i = 0; i < wordsAdded.size(); i++){
			if (i%3 == 0 && i != 0){
				output += "\n";
			}
			output += wordsAdded.get(i) + "     ";
			
		}
		return output;
    }

    private void fillWithWords() {
	//randomize words to add. 
    	for (int i = 0; i < wordsToAdd.size(); i++) {
    		int indexUsed = randgen.nextInt(wordsToAdd.size());
    	    String word = wordsToAdd.get(indexUsed);
    	    boolean wordAddedflag = false;
    	    int trials = 0, randomRow, randomColumn, rowIncrement, colIncrement;
    	    while (!wordAddedflag && trials <= 300) {
    	    	//System.out.println(rows);
    	    	randomRow = randgen.nextInt(rows);
    	    	randomColumn = randgen.nextInt(cols);
    	    	rowIncrement = randgen.nextInt(3) - 1;
    	    	colIncrement = randgen.nextInt(3) - 1;
	    		if (addWordRandom(word, randomRow, randomColumn, rowIncrement, colIncrement)) {
		    		    wordsToAdd.remove(indexUsed);
		    		    wordsAdded.add(word);
		    		    wordAddedflag = true;
		    		    i -= 1;
	    			}
	    		trials += 1;
    	    }
    	}
    }
    private boolean addWordRandom(String word,int row, int col, int rowIncrement, int colIncrement) {
    	if (data.length - col >= word.length() && data.length - row >= word.length() && spaceAvailable(word, row, col, rowIncrement, colIncrement)) {
    	    for (int i = 0; i < word.length(); i++) {
    	    	data[row + i * rowIncrement][col + i * colIncrement] = word.charAt(i);
    	    }
    	    return true;
    	} 
    	else {
    	    return false;
    	}
    }
  
    private boolean spaceAvailable(String word, int row, int col, int rowIncrement, int colIncrement) {
    	if (rowIncrement == 0 && colIncrement == 0) {
    	    return false;
    	}
    	for (int i = 0; i < word.length(); i++) {
    	    if (row + i * rowIncrement > rows || row + i * rowIncrement < 0 || col + i * colIncrement > cols || col + i * colIncrement < 0) {
    	    	return false;
    	    } 
    	    if (data[row + i * rowIncrement][col + i * colIncrement] != '_' && data[row + i * rowIncrement][col + i * colIncrement] != word.charAt(i)) {
    	    	return false;
    	    }
    	}
    	return true;
        }
    private void fillUpRest(){
    	for (int i = 0; i < rows; i++){
    		for (int j = 0; j < cols; j++){
    			if (data[i][j] == '_'){
    				data[i][j] = getRandomLetter();
    			}
    		}
    	}
    }
   private char getRandomLetter(){
	   String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	   return alpha.charAt(randgen.nextInt(alpha.length()));
   }
    public static void main(String[] args){
    	if (args.length <= 2) {
    		System.out.println("Not enough values as input");
    	    System.out.println(errorMsg);
		    System.exit(0);
    	} 
    	else {
    		//reading row and col
    	    int rows = 0;
    	    int cols = 0;
    	    seed = (int)(Math.random()*100000);
    	    try {
	    		rows = Integer.valueOf(args[0]);
	    		cols = Integer.valueOf(args[1]);
    	    } catch (IllegalArgumentException e) {
    	    	System.out.println("Not a proper input for rows and cols \n");
	    		System.out.println(errorMsg);
	    		System.exit(0);
    	    }
    	    //read filename
    	    String fileName = args[2];
    	    //read seed
    	    if (args.length >= 4) {
	    		try {
	    		    seed = Integer.valueOf(args[3]);
	    		} 
	    		catch (IllegalArgumentException e) {
	    			System.out.println("Not a proper input for the seed \n");
	    		    System.out.println(errorMsg);
	    		    System.exit(0);
	    		}
    	    }
    	    ansKey = false;

    	    //check if they input key
    	    if (args.length == 5 && args[4].equals("key")) {
    	        ansKey = true;
    	    }
    	    
    	    if (args.length > 5){
    	    	System.out.println("There are too many inputs! Check your inputs again");
    		    System.out.println(errorMsg);
    		    System.exit(0);
    	    }
    	    
    	    
    	    
    	    WordSearch puzzle = new WordSearch(rows, cols, fileName, seed, ansKey);
    	    System.out.println(puzzle);
    	}
    }
}