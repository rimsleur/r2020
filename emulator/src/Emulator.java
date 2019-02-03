import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;

public class Emulator extends Applet implements WindowListener
{
	Frame frame;
	/*
	int cellSize = 50;
	int fieldSize = 10;
	int xMargin = 10;
	int yMargin = 15;
	Channel channel;
	Frame frame;
	Graphics graphics;
	ArrayList<String> cellDataList;
	int cellData [][];

	int startPosX = 1;
	int startPosY = 1;
	int curPosX = startPosX;
	int curPosY = startPosY;
*/

	public static void main (String [] args)
	{
		Frame frame = new Frame ("r2020 Emulator");
		Emulator emulator = new Emulator ();
		frame.add ("Center", emulator);
		frame.addWindowListener (emulator);
		emulator.init (frame);
	}

	public void init (Frame frame)
	{
		this.frame = frame;
		//frame.setSize (xMargin * 2 + cellSize * fieldSize + 11, yMargin * 2 + cellSize * fieldSize + 25);
		frame.setSize (500, 500);
		//this.loadField ();
		frame.setVisible (true);
	}

	public void loadField ()
	{

	}

	public void paint (Graphics graphics)
	{
/*
		this.graphics = graphics;

		for (int i = 0; i <= fieldSize; i++)
		{
			graphics.drawLine (xMargin + i * cellSize, yMargin, xMargin + i * cellSize, yMargin + cellSize * fieldSize);
		}

		for (int i = 0; i <= fieldSize; i++)
		{
			graphics.drawLine (xMargin, yMargin + i * cellSize, xMargin + cellSize * fieldSize, yMargin + i * cellSize);
		}

		for (int x1 = 1; x1 <= fieldSize; x1++)
		{
			for (int y1 = 1; y1 <= fieldSize; y1++)
			{
				if (this.cellData [x1][y1] == 1)
				{
					int x = xMargin + (x1 - 1) * (cellSize - 1) + x1;
					int y = yMargin + (fieldSize - y1) * (cellSize - 1) + (fieldSize - y1 + 1);
					graphics.setColor (new Color (150, 150, 150));
					graphics.fillRect (x, y, cellSize - 1, cellSize - 1);
				}
			}
		}

		int x = xMargin + cellSize * (fieldSize - (fieldSize - curPosX)) - cellSize / 2 - 10;
		int y = yMargin + cellSize * (fieldSize - curPosY + 1) - cellSize / 2 - 10;
		graphics.setColor (Color.black);
		graphics.fillRect (x, y, 20, 20);
*/
	}

	public void callback (String data)
	{
/*
		int x = xMargin + cellSize * (fieldSize - (fieldSize - curPosX)) - cellSize / 2 - 10;
		int y = yMargin + cellSize * (fieldSize - curPosY + 1) - cellSize / 2 - 10;
		graphics.setColor (Color.black);
		this.graphics.fillRect (x, y, 20, 20);
		super.paintComponents(this.graphics);
		super.repaint ();
*/
	}

	public void windowActivated (WindowEvent windowEvent) {}

	public void windowDeactivated (WindowEvent windowEvent) {}

	public void windowIconified (WindowEvent windowEvent) {}

	public void windowDeiconified (WindowEvent windowEvent) {}

	public void windowOpened (WindowEvent windowEvent) {}

	public void windowClosing (WindowEvent windowEvent)
	{
		System.exit (0);
	}

	public void windowClosed (WindowEvent windowEvent) {}
}