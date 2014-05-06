// The "Tester" class.
import java.awt.*;
import java.io.*;
import hsa.Console;

class Tester
{
    static Console c;           // The output console

    public Tester () throws IOException
    {


	c = new Console ();

	c.println ("Which category?");
	c.println ("w - World, m - Movies and Food, s - Science");


	char l = c.getChar ();

	c.clear ();

	Question q = new Question (l);





	c.println (q.askQuestion ());
	for (int i = 0 ; i < 4 ; i++)
	{
	    c.println (i + 1 + ". " + q.choices [i]);
	}
	int a = c.readInt ();

	if (q.checkAnswer (a))
	    c.println ("CORRECT!");
	else
	    c.println ("WRONG!");
	c.getChar ();
	c.clear ();


	//c.print ((String)q.prompts.get(0));


	// Place your program here.  'c' is the output console
    } // main method
} // Tester class
