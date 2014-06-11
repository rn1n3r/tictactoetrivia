import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.*;
import java.util.*;
import sun.audio.*;

public class Game implements ActionListener
{
    private JFrame frame;
    private CardLayout cl;
    private JPanel mainPanel, back, cat, inst;

    public static AudioStream as = null;

    public static void Player (String audioname)  // Music method
    {
	InputStream in = null; // Readinput stream  for audio

       
	    in = Game.class.getResourceAsStream(audioname); // for importing Files into FileInputStream
       
	try
	{
	    as = new AudioStream (in);
	}
	catch (IOException e)
	{
	}

    }


    public Game ()
    {
	frame = new JFrame ("TIC TAC TOE TRIVIA");
	frame.setResizable(false);
	frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	Border raisedbevel = BorderFactory.createRaisedBevelBorder ();

	back = new ImagePanel ("img/Main.png");
	cat = new Category ();
	
	// Read the instructions from a text file
	String line = "";
	String curr = "";
	try 
	{
	// File reader stuff
	InputStream in = Game.class.getResourceAsStream("data/instructions.txt");
	BufferedReader filein = new BufferedReader (new InputStreamReader(in));
	
	curr = filein.readLine();
	
	while (curr != null)
	{
	    line +=curr;
	    curr = filein.readLine();
	 
	}
	}
	catch (IOException e){}
	
	
	// Creating the instructions panel
	
	inst = new JPanel(null);
	inst.setBackground(Color.WHITE);
	
	JTextArea instructions = new JTextArea(line);
	JButton returnButton = new JButton ("Return to menu!");
	returnButton.addActionListener(new MenuReturn());
	returnButton.setSize (180, 40);
	returnButton.setLocation (410, 200);
	inst.add(returnButton);
	
	instructions.setSize(750,400);
	instructions.setLocation(125,100);
	instructions.setLineWrap (true);
	instructions.setWrapStyleWord (true); // wrap words
	instructions.setEditable(false);
	instructions.setFont(new Font ("Segoe UI", Font.PLAIN, 20));
	
	inst.add(instructions);

	cl = new CardLayout (); // For cardlayout
	mainPanel = new JPanel (cl); //Cardlayout panel

	// Set JButton properties for menu

	JButton gameStart = new JButton ("START GAME");
	gameStart.setBackground (Color.yellow);
	gameStart.setBorder (raisedbevel);

	JButton instruct = new JButton ("INSTRUCTIONS");
	instruct.setBackground (Color.blue);
	instruct.setBorder (raisedbevel);

	JButton gameEnd = new JButton ("EXIT");
	gameEnd.setBackground (Color.red);
	gameEnd.setBorder (raisedbevel);




	back.setLayout (null);
	// Add buttons
	back.add (gameStart);
	back.add (instruct);
	back.add (gameEnd);

	// Set locations of buttons

	gameStart.setSize (180, 40);
	gameStart.setLocation (410, 200);

	instruct.setSize (180, 40);
	instruct.setLocation (410, 250);

	gameEnd.setSize (180, 40);
	gameEnd.setLocation (410, 300);

	// Add action listeners
	gameStart.addActionListener (this);
	instruct.addActionListener (this);
	gameEnd.addActionListener (this);

	// Add panels to cardlayout
	mainPanel.add (back, "menu");
	mainPanel.add (cat, "cat");
	mainPanel.add (inst, "inst");
	cl.show (mainPanel, "menu");

	frame.setContentPane (mainPanel);
	frame.setLocationRelativeTo (null);

	frame.pack ();
	frame.setSize (1000, 600);
	frame.setLocationRelativeTo (null);
	frame.setVisible (true);
    }


    public void actionPerformed (ActionEvent e)
    {
	String s = e.getActionCommand ();

	if (s.equalsIgnoreCase ("START GAME"))
	{
	    cl.show (mainPanel, "cat");

	}

	else if (s.equalsIgnoreCase ("INSTRUCTIONS"))
	{
	    cl.show(mainPanel, "inst");
	}

	else if (s.equalsIgnoreCase ("EXIT"))
	{
	    System.exit (0);
	}

    }


    public static void main (String[] args) throws IOException
    {
	new Game ();


	Player ("audio/Leisure.wav");
	AudioPlayer.player.start (as);

    }
    
    class MenuReturn implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    cl.show(mainPanel, "menu");
	}
    }

    class Category extends JPanel implements ActionListener
    {
	private char q;
      

	public Category ()
	{
	    super(null); // for layout
	    setOpaque (true);
	    setBackground (Color.WHITE);

	    JButton snc = new JButton ("Science");
	    JButton world = new JButton ("World");
	    JButton movie = new JButton ("Movies And Food");
	    JButton returnButton = new JButton ("Return to menu!");
	    
	    JLabel newLabel = new JLabel ("Choose a category!");
	    newLabel.setFont(new Font ("Segoe UI", Font.PLAIN, 24));
	    
	    add (newLabel);
	    add (snc);
	    add (world);
	    add (movie);
	    add (returnButton);

	    // Set locations of buttons and label
	    newLabel.setSize (210, 40);
	    newLabel.setLocation (395, 100);
	    
	    snc.setSize (180, 40);
	    snc.setLocation (410, 200);

	    world.setSize (180, 40);
	    world.setLocation (410, 250);

	    movie.setSize (180, 40);
	    movie.setLocation (410, 300);
	    returnButton.setSize (180, 40);
	    returnButton.setLocation (410, 350);


	    snc.addActionListener (this);
	    world.addActionListener (this);
	    movie.addActionListener (this);
	   returnButton.addActionListener(new MenuReturn());

	}


	public void actionPerformed (ActionEvent e)
	{
	    String s = e.getActionCommand ();

	    if (s.equalsIgnoreCase ("Science"))
	    {
		q = 's';
	    }

	    else if (s.equalsIgnoreCase ("World"))
	    {
		q = 'w';

	    }

	    else if (s.equalsIgnoreCase ("Movies And Food"))
	    {
		q = 'm';
	    }

	    new Board (3, q);
	    frame.dispose ();

	}

    }
}
