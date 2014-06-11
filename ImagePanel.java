import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class ImagePanel extends JPanel
{
    public static BufferedImage image;
    
    public ImagePanel(String filePath)
    {
	super();
	try
	{
	    image = ImageIO.read(ImagePanel.class.getResource(filePath));
	}
	catch (IOException e)
	{}
    }
    
    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);   
	g.drawImage(image,0,0,getWidth(),getHeight(),null);
	
    }
}
