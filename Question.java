// The "Question" class.
import java.awt.*;
import java.io.*;
import java.util.*;
import hsa.Console;
import javax.swing.*;

public class Question
{
    // all of these will be private in the end, some are public for now for testing
 
    
    public String [] prompts; // string array to store the lines fomr the files (with the question, choices and answers)
    public String [] choices = new String [4]; // string aray to store the choices
    private int currentQuestion = 0; // current question number    
    private int answer; // answer number (from the file)
    
    
    public Question (char category, JTextArea text ) throws IOException // constructor, IOException for file reading. Accepts a char for category choice
    {
	String fname; // file name
	ArrayList tempList = new ArrayList(); // arraylist so adding the lines of the files is more convenient
	
	// choose which file to read based on category
	if (category == 'w')
	    fname = "data/World.txt";
	
	else if (category == 'm')
	    fname = "data/MoviesAndFood.txt";
	
	else
	    fname = "data/Science.txt";
	    
	// File reader stuff
	InputStream in = getClass().getResourceAsStream(fname);
	BufferedReader filein = new BufferedReader (new InputStreamReader(in));
	
	String line = filein.readLine();

	// while it isn't a blank line, add the lines of the file to the array list
	while (line != null)
	{
	    tempList.add(new String(line));
	    line = filein.readLine();
	}
	
	Collections.shuffle(tempList); //randomize order of questions
	
	Object [] temp = tempList.toArray(); // turn the arraylist into an array, so we can convert the lines (stored as Objects) into Strings
	
	prompts = new String [temp.length]; 
	
	for (int i = 0; i < temp.length; i ++) 
	{
	    prompts [i] = (String)temp[i]; // convert into string array
	}
	

    }
    
    public String askQuestion () // to ask the question!
    {
	if (currentQuestion >= prompts.length) // If the questions have run out
	{
	    return ("NO MORE QUESTIONS LOL");
	}
	else
	{
	    
	    String currentPrompt = prompts[currentQuestion]; // get the current prompt (question line) from the array
	    //System.out.println(currentPrompt);
	    int firstSpace = currentPrompt.indexOf("  ") + 1; // find where the actual question ends, based on the double space in the line
	    
	    String theQuestion = currentPrompt.substring (0, firstSpace); // make that a new string
	    
	    // Gets the four choices 
	    int currentSpace = firstSpace; 
	    int nextSpace;
	    for (int i = 0; i < 4; i++)
	    {
		nextSpace = currentPrompt.indexOf("/", currentSpace+1);
		choices[i] = currentPrompt.substring(currentSpace+1, nextSpace);
		
		currentSpace = nextSpace;
	    }
	    
	    // gets the number of the correct choice
	    nextSpace = currentPrompt.indexOf("/", currentSpace+1);
	    answer = Integer.parseInt(currentPrompt.substring(currentSpace+1, currentPrompt.length()));
	    
	    //System.out.print(answer); //testing
	    
	    currentQuestion ++; // goes on to the next question in the array
	   
	    return theQuestion; // return the question string
	}
    }

    public boolean checkAnswer (int choice) // to check if the choice is correct
    {
	if (choice == answer)
	    return true;
	    
	else
	    return false;
    }

	
	// Place your program here.  'c' is the output console
     // main method
} // Question class
