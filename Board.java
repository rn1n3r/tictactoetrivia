import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import sun.audio.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.imageio.*; //For Loading Images
import java.util.TimerTask;


public class Board extends JFrame
{
    private Timer timer;
    private JButton[] tiles;
    private JButton[] choiceButtons = new JButton [4];
    private JTextArea textArea = new JTextArea (1, 1);
    private JPanel grid = new ImagePanel ("bg.png");
    private JPanel prompt = new ImagePanel ("bg2.png");
    private char category;
    private Question q;
    private boolean inQuestion = false;

    // Default game conditions
    private boolean win = false;
    private boolean lost = false;
    private int[] added = new int [9];
    private int[] opAdded = new int [9];


    public Board (int rows, char cat)  // Board constructor
    {
	// Set frame properties
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	setSize (1000, 600);
	
	//grid.add(image);

	grid.setLayout (new GridLayout (0, rows)); // Set GridLayout for the number of rows
	prompt.setLayout (new GridLayout (0,1));
	// Initialize and add the tiles to the game grid
	tiles = new JButton [rows * rows];
	for (int i = 0 ; i < rows * rows ; i++)
	{
	    tiles [i] = new JButton ();
	    tiles [i].setOpaque(false);
	    tiles [i].setContentAreaFilled(false);
	    tiles [i].setBorderPainted(false);
	    grid.add (tiles [i]);
	    tiles [i].addActionListener (new ButtonListener ()); // Add the ButtonListener
	}

	// Set the text area properties
	textArea.setLineWrap (true);
	textArea.setWrapStyleWord (true); // wrap words
	prompt.add (textArea); // add to the prompt panel

	// Add buttons for answer choices to the prompt panel
	for (int i = 0 ; i < 4 ; i++)
	{
	    choiceButtons [i] = new JButton (new ImageIcon("button.png"));
	    choiceButtons [i].setOpaque(false);
	    choiceButtons [i].setContentAreaFilled(false);
	    choiceButtons [i].setBorderPainted(false);
	    prompt.add (choiceButtons [i]);
	}

	// Initialize JSplitPane
	JSplitPane split = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT, grid, prompt);
	// Set position of the division, disable change
	
	split.setDividerLocation (600 + split.getInsets ().left);
	split.enable (false);

	this.getContentPane ().add (split); // Add split pane to the frame
 
	category = cat;
	try
	{
	    q = new Question (cat, textArea); // New question; try catch required because Question throws an IOException due to file reading
	}
	catch (IOException g)
	{
	}
	split.revalidate();
	split.repaint();
    }


    public void newBoard (int rows)  // Method to reset the board
    {
	//Removes existing tiles, create new ones, and add to the board
	for (int i = 0 ; i < rows * rows ; i++)
	{
	    grid.remove (tiles [i]);
	    tiles [i] = new JButton ();
	    tiles [i].setOpaque(false);
	    tiles [i].setContentAreaFilled(false);
	    tiles [i].setBorderPainted(false);
	    grid.add (tiles [i]);
	    tiles [i].addActionListener (new ButtonListener ());
	    grid.revalidate ();
	    grid.repaint ();
	}
    
	// Updates the choices buttons
	for (int i = 0 ; i < 4 ; i++)
	{
	    prompt.remove (choiceButtons [i]);
	    choiceButtons [i] = new JButton (new ImageIcon("button.png"));
	    choiceButtons [i].setOpaque(false);
	    choiceButtons [i].setContentAreaFilled(false);
	    choiceButtons [i].setBorderPainted(false);
	    prompt.add (choiceButtons [i]);

	    // Because components are being added at runtime
	    prompt.revalidate ();
	    prompt.repaint ();

	}

	textArea.setText ("");

	// Reset tic-tac-toe combinations
	added = new int [9];
	opAdded = new int [9];

	//New question
	try
	{
	    q = new Question ('w', textArea);
	}
	catch (IOException g)
	{
	}

    }


    class ButtonListener implements ActionListener // Listener for the tic-tac-toe grid
    {

	public void actionPerformed (ActionEvent e)
	{
	    if (!inQuestion)
	    {
		
		// When a tile on the grid is pressed, remove the action listeners from that button
		ActionListener[] listeners = ((JButton) e.getSource ()).getActionListeners ();
		for (int i = 0 ; i < listeners.length ; i++)
		{
		    ((JButton) e.getSource ()).removeActionListener (listeners [i]);
		}

		textArea.setText (q.askQuestion ()); // Set the text to the question

		inQuestion = true; // In question
	     
		// Updates the choices buttons
		for (int i = 0 ; i < 4 ; i++)
		{
		    prompt.remove (choiceButtons [i]);
		    choiceButtons [i] = new JButton (q.choices [i], new ImageIcon("button.png"));
		    choiceButtons [i].setFont(new Font ("Segoe UI", Font.PLAIN, 24));
		    choiceButtons [i].setHorizontalTextPosition(SwingConstants.CENTER);
		    choiceButtons [i].setOpaque(false);
		    choiceButtons [i].setContentAreaFilled(false);
		    choiceButtons [i].setBorderPainted(false);
		    prompt.add (choiceButtons [i]);

		    // Because components are being added at runtime
		    prompt.revalidate ();
		    prompt.repaint ();
		    choiceButtons [i].addActionListener (new ChoiceListener (e));
		    
		    
		}
		new QuestionTimer(e);
	    }
	}
    }
    


    class ChoiceListener implements ActionListener // Listener for the choice buttons
    {
	private ActionEvent t;
	public ChoiceListener (ActionEvent t)  // Get the ActionEvent for the tic tac toe tile so it can update the text on the button
	{
	    this.t = t;
	}
	public void actionPerformed (ActionEvent e)
	{
	    
	    int [][] winCombo = new int [] [] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, //horizontal wins
					 {1, 4, 7}, {2, 5, 8}, {3, 6, 9}, //vertical wins
					 {1, 5, 9}, {3, 5, 7} };            //Tic-tac-toe winning combinations


	    if (inQuestion)
	    {
		int answer = 0;
		inQuestion = false; // not in question anymore when pressed

		// Assign the answer based on which button is pressed
		for (int i = 0 ; i < 4 ; i++)
		{
		    if (e.getSource ().equals (choiceButtons [i]))
			answer = i + 1;
		}

		if (q.checkAnswer (answer)) // If the answer is correct
		{
		    ((JButton) t.getSource ()).setIcon (new ImageIcon("x.png"));

		    // Add the position to the array
		    for (int i = 0 ; i < 9 ; i++)
		    {
			if (t.getSource ().equals (tiles [i]))
			    added [i] = i + 1;
		    }
		    ((JButton) e.getSource ()).setIcon (new ImageIcon("winButton.png"));
		    ((JButton) e.getSource ()).setForeground(new Color(34,177,76));
		    ((JButton) e.getSource ()).setText ("CORRECT!");

		    
		    timer.stop();


		    // Check if the game has been won
		    for (int i = 0 ; i < 8 ; i++)
		    {
			if (added [winCombo [i] [0] - 1] != 0 && added [winCombo [i] [1] - 1] != 0 && added [winCombo [i] [2] - 1] != 0)
			    win = true; //

		    }

		}
		else // If the answer is incorrect
		{
		    
		    ((JButton) t.getSource ()).setIcon (new ImageIcon("o.png"));
		  
		  
		    // Add the position to the opposite array
		    
		    boolean timeOut = true;
		    for (int i = 0 ; i < 9 ; i++)
		    {
			if (t.getSource ().equals (tiles [i]))
			{
			    opAdded [i] = i + 1;

			}
		
		    }


		    for (int i = 0; i <4; i++)
		    {
			if (e.getSource().equals(choiceButtons[i]))
			{
			    timer.stop();
			    timeOut = false;
			}
		    }
		    
		    
		    if (timeOut)
		    {
			textArea.append("\nRan out of time!");
		    }
		    else
		    {
					((JButton) e.getSource ()).setIcon (new ImageIcon("loseButton.png"));
		    ((JButton) e.getSource ()).setForeground(new Color(237,28,36));
		    ((JButton) e.getSource ()).setText ("INCORRECT!");
		    }
		    
		    

		    //Check if the game has been lost
		    for (int i = 0 ; i < 8 ; i++)
		    {
			if (opAdded [winCombo [i] [0] - 1] != 0 && opAdded [winCombo [i] [1] - 1] != 0 && opAdded [winCombo [i] [2] - 1] != 0)
			    lost = true;
		    }
		}

		if (win) // if the game has been won
		{
		    Game.Player ("tada.wav");
		    AudioPlayer.player.start (Game.as);
		    win = false;
		    gameOver (true);



		}

		else if (lost) // if the game has been lost
		{
		    Game.Player ("wah.wav");
		    AudioPlayer.player.start (Game.as);
		    lost = false;
		    gameOver (false);

		}
	    }
	}
    }


    public void gameOver (boolean hasWon)
    {
	String message = "";

	if (hasWon)
	    message += "win";
	else
	    message += "lose";

	JFrame newFrame = new JFrame ();
	JOptionPane test = new JOptionPane ();
	String[] options = {"New game!", "Return to menu.", "Exit."};
	int userOption = test.showOptionDialog (newFrame, "You " + message + "!!", "You " + message + "!!",
		JOptionPane.YES_NO_CANCEL_OPTION,
		JOptionPane.INFORMATION_MESSAGE, null, options, options [0]);

	if (userOption == 0 || userOption == -1)
	{
	    newBoard (3);

	}
	else if (userOption == 1)
	{
	    dispose ();
	    Game game = new Game ();
	}
	else if (userOption == 2)
	{
	    dispose ();
	}


    }
    
    // Timer

    class QuestionTimer implements ActionListener
    {
	int timerCount = 10;

	ActionEvent t;
	public QuestionTimer (ActionEvent t) // Constructor for QuestionTimer class
	{
	   this.t = t; //Gets the grid button that was pressed
	   timer = new Timer (1000,this);
	   timer.setInitialDelay(500); // start timer after 500 milliseconds
	   timer.start();

	}
	
	
	public void actionPerformed (ActionEvent e)
	{
	    textArea.append (timerCount + "..");
	    timerCount--;
	    if (timerCount == -1)
	    {
		timer.stop();
		new ChoiceListener(t).actionPerformed(e); //fire action for that grid button
	    }
	}
    }


    public static void main (String[] args) throws IOException
    {

	Board game = new Board (3, 'w');

	game.setVisible (true);
    }
}
