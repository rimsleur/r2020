import java.awt.Canvas;
//import java.awt.Dimension;
//import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
//import java.awt.Font;
//import java.awt.image.BufferedImage;
//import java.awt.image.DataBufferInt;
//import java.awt.image.BufferStrategy;
//import javax.swing.JFrame;

public class ICanvas extends Canvas
{
	// TODO: Разобраться с этими константами
	private static Integer PINSIDESIZE = 19; 
	private static Integer PINSTEPSIZE = PINSIDESIZE + 1;

	protected Graphics graphics;

	public ICanvas () {}	

	protected Boolean draw ()
	{
		return true;
	}

	protected void redraw () {}

	protected void drawComponets () {}

	protected void redrawComponets () {}

	protected void drawFrame (Integer x, Integer y, Integer width, Integer height, String title)
	{
		int titleSpaceLen = title.length () * 7;
		this.graphics.setColor (Color.BLACK);
		if (titleSpaceLen == 0) this.graphics.drawLine (x, y, x + width, y);
		else
		{
			this.graphics.drawLine (x, y, x + 9, y);
			this.graphics.drawLine (x + 13 + titleSpaceLen, y, x + width, y);
		}
		this.graphics.drawLine (x + width, y, x + width, y + height);
		this.graphics.drawLine (x, y, x, y + height);
		this.graphics.drawLine (x, y + height, x + width, y + height);
		this.graphics.drawString (title, x + 11, y + 5);
	}

	protected void drawText(Integer x, Integer y, Integer width, Integer height, String text)
	{
		this.graphics.setColor(Color.WHITE);
		this.graphics.fillRect(x, y - 14, width, height);
		this.graphics.setColor(Color.BLACK);
		this.graphics.drawString(text, x, y);
	}
	
	protected void drawVerticalPin (Integer x, Integer y, String text, Boolean inversion)
	{
		this.graphics.setColor (Color.BLACK);
		int size = this.PINSIDESIZE - 1;
		this.graphics.drawLine (x, y, x + size, y);
		this.graphics.drawLine (x + size, y, x + size, y + size);
		this.graphics.drawLine (x, y, x, y + size);
		this.graphics.drawLine (x, y + size, x + size, y + size);
		this.graphics.drawString (text, x + 3, y + 33);

		if (inversion)
		{
			Integer textLength = (text.length() * 7) - 2;
			this.graphics.drawLine(x + 2, y + 22, x + textLength, y + 22);
		}
	}

	

	protected void redrawVerticalPin (Integer x, Integer y, BitState state)
	{
		if (state == BitState.H) this.graphics.setColor (Color.BLACK);
		else this.graphics.setColor (Color.WHITE);
		int size = this.PINSIDESIZE - 2;
		this.graphics.fillRect (x + 1, y + 1, size, size);
	}

	protected void drawHorizontalPin (Integer x, Integer y, Integer width, String text, Boolean inversion)
	{
		this.graphics.setColor (Color.BLACK);
		int size = this.PINSIDESIZE - 1;
		int pinX = x + width - this.PINSIDESIZE;
		this.graphics.drawLine (pinX, y, pinX + size, y);
		this.graphics.drawLine (pinX + size, y, pinX + size, y + size);
		this.graphics.drawLine (pinX, y, pinX, y + size);
		this.graphics.drawLine (pinX, y + size, pinX + size, y + size);
		this.graphics.drawString (text, x, y + this.PINSIDESIZE);
		int titleSpaceLen = text.length () * 7;
		this.graphics.drawLine (x + titleSpaceLen + 3, y + size, x + width - this.PINSIDESIZE - 4, y + size);

		if (inversion)
		{
			Integer textLength = (text.length() * 7) - 2;
			this.graphics.drawLine(x + 2, y + 8, x + textLength, y + 8);
		}
	}

	protected void redrawHorizontalPin (Integer x, Integer y, Integer width, BitState state)
	{
		if (state == BitState.H) this.graphics.setColor (Color.BLACK);
		else this.graphics.setColor (Color.WHITE);
		if (state == BitState.Z) this.graphics.setColor(Color.GRAY);
		int size = this.PINSIDESIZE - 2;
		int pinX = x + width - this.PINSIDESIZE;
		this.graphics.fillRect (pinX + 1, y + 1, size, size);
	}
}