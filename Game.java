import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import sun.audio.*;

public class Game implements ActionListener
{
    private JFrame frame;

    public static AudioStream as = null;

    public static void Player (String audioname)  // Music method
    {
	InputStream in = null; // Readinput stream  for audio

	try
	{
	    in = new FileInputStream (new File (audioname)); // for importing Files into FileInputStream
	}
	catch (FileNotFoundException e)
	{
	}
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
	frame = new JFrame ("Menu");

	Container pane = frame.getContentPane ();
	frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

	JButton gameStart = new JButton ("Start Game!");
	JButton instruct = new JButton ("Instructions");
	JButton gameEnd = new JButton ("Quit");

	pane.setLayout (new BoxLayout (pane, BoxLayout.Y_AXIS));

	pane.add (gameStart);
	pane.add (instruct);
	pane.add (gameEnd);

	gameStart.addActionListener (this);
	instruct.addActionListener (this);
	//gameEnd.addActionListener (JFrame.EXIT_ON_CLOSE);

	frame.pack ();
	frame.setVisible (true);
    }


    public void actionPerformed (ActionEvent e)
    {
	Board game = new Board (3, 'm');
	game.setVisible (true);
	frame.setVisible (false);

    }



    public static void main (String[] args) throws IOException
    {
	new Game ();
	Player ("Leisure.wav");
	AudioPlayer.player.start (as);

    }
}




