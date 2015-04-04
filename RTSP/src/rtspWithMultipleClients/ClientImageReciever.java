package rtspWithMultipleClients;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ClientImageReciever extends Thread
{
	ObjectInputStream	inStream;
	int					i;

	public ClientImageReciever(ObjectInputStream inStream)
	{
		this.inStream = inStream;
	}

	public void run()
	{
		ImageIcon icon = null;

		while (true)
		{
			try
			{
				icon = (ImageIcon) this.inStream.readObject();
				BufferedImage image = new BufferedImage(
					    icon.getIconWidth(),
					    icon.getIconHeight(),
					    BufferedImage.TYPE_INT_RGB);
					Graphics g = image.createGraphics();
					// paint the Icon to the BufferedImage.
					icon.paintIcon(null, g, 0,0);
					g.dispose();
				ImageIO.write(image, "jpg", new File("D:\\receivedImages\\"
						+ (i++) + ".jpg"));
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			// if (this.list.size() >= 200)
			// {
			// this.clear();
			// System.gc();
			// }
			icon = null;
		}
	}

	// protected synchronized void clear()
	// {
	// for (int i = 0; i < 75; i++)
	// {
	// this.list.remove(i);
	// }
	// if (Client.picCount >= 75)
	// {
	// Client.picCount = Client.picCount - 75;
	// }
	// }
	//
	// protected synchronized void reset()
	// {
	// Client.picCount = 0;
	// }
}
