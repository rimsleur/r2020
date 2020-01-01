import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class LEDICanvas extends ICanvas implements Runnable, KeyListener
{
	public static final Integer WIDTH = 155;
	public static final Integer WIDTH_EXPANDED = 334;
	public static final Integer HEIGHT = 158;

	private static Integer PINSIDESIZE = 19;
	private static Integer PINSTEPSIZE = PINSIDESIZE + 1;

	private JFrame frame;
	private BufferStrategy bufferStrategy;
	private Graphics graphics;
	private Font font;

	private Boolean running = false;
	private Boolean expand = false;

	private LEDI ledi = new LEDI ();

	public LEDICanvas ()
	{
		this.frame = new JFrame ("LED indicator");
		this.frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout (new BorderLayout ());
		this.frame.add (this, BorderLayout.CENTER);
		this.frame.pack ();
		this.frame.setResizable (false);
		if (expand) this.frame.setSize (new Dimension (WIDTH_EXPANDED, HEIGHT));
		else this.frame.setSize (new Dimension (WIDTH, HEIGHT));
		this.frame.setLocationRelativeTo (null);
		this.frame.setLocation(830, 549);
		this.frame.setVisible (true);

		setFocusTraversalKeysEnabled (false);
		addKeyListener (this);
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == 9)
		{
			expand = !expand;
			this.frame.setVisible (false);
			this.frame.dispose ();
			if (expand) this.frame.setSize (new Dimension (WIDTH_EXPANDED, HEIGHT));
			else this.frame.setSize (new Dimension (WIDTH, HEIGHT));
			this.frame.setVisible (true);
			if (this.draw ()) this.redraw ();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	public synchronized void start ()
	{
		this.running = true;
		new Thread (this).start ();
	}

	public synchronized void stop ()
	{
		this.running = false;
	}

	public void run ()
	{
		if (!draw ()) 
		{
			this.frame.setVisible (false);
			this.frame.dispose ();
			return;
		}

		this.ledi.setUbIndex(1);

		while (this.running)
		{
			calculate ();
			redraw ();
			//sleep (50);
		}
	}

	private void calculate ()
	{
		this.ledi.calculate (1);
	}

	@Override
	protected final Boolean draw ()
	{
		this.bufferStrategy = getBufferStrategy ();
		if (this.bufferStrategy == null)
		{
			createBufferStrategy (2);
			this.bufferStrategy = getBufferStrategy ();
			if (this.bufferStrategy == null) return false;
		}

		this.graphics = bufferStrategy.getDrawGraphics ();
		super.graphics = this.graphics;

		this.graphics.setColor (Color.WHITE);
		this.graphics.fillRect (0, 0, getWidth (), getHeight ());

		this.font = new Font ("Monospaced", Font.PLAIN, 12);
		this.graphics.setFont (this.font);

		drawComponets ();

		this.graphics.dispose ();
		bufferStrategy.show ();

		return true;
	}

	@Override
	protected final void redraw ()
	{
		this.graphics = bufferStrategy.getDrawGraphics ();
		super.graphics = this.graphics;
		this.graphics.setFont (this.font);

		redrawComponets ();

		this.graphics.dispose ();
		bufferStrategy.show ();
	}

	@Override
	protected final void drawComponets ()
	{
		if (expand)
		{
			drawFrame (10, 10, 168, 50, "");
			drawHorizontalPin (15, 15, 64, "DS", false);
			drawHorizontalPin (15, 35, 64, "IREQ", true);
			drawHorizontalPin (110, 15, 64, "RDY", true);
			drawHorizontalPin (110, 35, 64, "STB", true);

			drawFrame (10, 70, 168, 50, "IOD");
			Integer pinX = 15;
			Integer pinY = 80;
			drawVerticalPin (pinX, pinY, "B7", false);
			drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
			drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
			drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
			drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
			drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
			drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
			drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);
		}

		Integer topLeftX;
		if (expand) topLeftX = 188;
		else topLeftX = 10;
		Integer topLeftY = 10;
		this.drawDigit (topLeftX, topLeftY);
		topLeftX += 70;
		this.drawDigit (topLeftX, topLeftY);
	}

	@Override
	protected final void redrawComponets ()
	{
		LEDIState lediState = this.ledi.getLEDIState ();

		if (expand)
		{
			redrawHorizontalPin (15, 15, 64, lediState.ds);
			redrawHorizontalPin (15, 35, 64, lediState.ireq);
			redrawHorizontalPin (110, 15, 64, lediState.rdy);
			redrawHorizontalPin (110, 35, 64, lediState.stb);

			Integer pinX = 15;
			Integer pinY = 80;
			redrawVerticalPin (pinX, pinY, lediState.iod.b7);
			redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, lediState.iod.b6);
			redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, lediState.iod.b5);
			redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, lediState.iod.b4);
			redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, lediState.iod.b3);
			redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, lediState.iod.b2);
			redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, lediState.iod.b1);
			redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, lediState.iod.b0);
		}

		Integer topLeftX;
		if (expand) topLeftX = 188;
		else topLeftX = 10;
		Integer topLeftY = 10;
		this.redrawDigit (topLeftX, topLeftY, lediState.digit1);
		topLeftX += 70;
		this.redrawDigit (topLeftX, topLeftY, lediState.digit2);
	}

	@Override
	protected final void drawFrame (Integer x, Integer y, Integer width, Integer height, String title)
	{
		super.drawFrame (x, y, width, height, title);
	}

	@Override
	protected final void drawVerticalPin (Integer x, Integer y, String text, Boolean inversion)
	{
		super.drawVerticalPin (x, y, text, inversion);
	}

	@Override
	protected final void redrawVerticalPin (Integer x, Integer y, BitState state)
	{
		super.redrawVerticalPin (x, y, state);
	}

	@Override
	protected final void drawHorizontalPin (Integer x, Integer y, Integer width, String text, Boolean inversion)
	{
		super.drawHorizontalPin (x, y, width, text, inversion);
	}

	@Override
	protected final void redrawHorizontalPin (Integer x, Integer y, Integer width, BitState state)
	{
		super.redrawHorizontalPin (x, y, width, state);
	}

	private void drawDigit (Integer topLeftX, Integer topLeftY)
	{
		Integer segmentLenght = 40;
		Integer segmentWidth = 7;
		Integer shift = 2;
		this.graphics.setColor(Color.LIGHT_GRAY);
		Integer x = topLeftX + segmentWidth + shift;
		Integer y = topLeftY;
		this.graphics.drawLine (x, y, x + segmentLenght, y);
		this.graphics.drawLine (x + segmentLenght, y, x + segmentLenght, y + segmentWidth);
		this.graphics.drawLine (x, y, x, y + segmentWidth);
		this.graphics.drawLine (x, y + segmentWidth, x + segmentLenght, y + segmentWidth);
		x = topLeftX;
		y = topLeftY + segmentWidth + shift;
		this.graphics.drawLine (x, y, x + segmentWidth, y);
		this.graphics.drawLine (x + segmentWidth, y, x + segmentWidth, y + segmentLenght);
		this.graphics.drawLine (x, y, x, y + segmentLenght);
		this.graphics.drawLine (x, y + segmentLenght, x + segmentWidth, y + segmentLenght);
		x = topLeftX + segmentLenght + shift + segmentWidth + shift;
		y = topLeftY + segmentWidth + shift;
		this.graphics.drawLine (x, y, x + segmentWidth, y);
		this.graphics.drawLine (x + segmentWidth, y, x + segmentWidth, y + segmentLenght);
		this.graphics.drawLine (x, y, x, y + segmentLenght);
		this.graphics.drawLine (x, y + segmentLenght, x + segmentWidth, y + segmentLenght);
		x = topLeftX + segmentWidth + shift;
		y = topLeftY + segmentLenght + shift + segmentWidth + shift;
		this.graphics.drawLine (x, y, x + segmentLenght, y);
		this.graphics.drawLine (x + segmentLenght, y, x + segmentLenght, y + segmentWidth);
		this.graphics.drawLine (x, y, x, y + segmentWidth);
		this.graphics.drawLine (x, y + segmentWidth, x + segmentLenght, y + segmentWidth);
		x = topLeftX;
		y = topLeftY + segmentWidth + shift + segmentLenght + shift + segmentWidth + shift;
		this.graphics.drawLine (x, y, x + segmentWidth, y);
		this.graphics.drawLine (x + segmentWidth, y, x + segmentWidth, y + segmentLenght);
		this.graphics.drawLine (x, y, x, y + segmentLenght);
		this.graphics.drawLine (x, y + segmentLenght, x + segmentWidth, y + segmentLenght);
		x = topLeftX + segmentLenght + shift + segmentWidth + shift;
		y = topLeftY + segmentWidth + shift + segmentLenght + shift + segmentWidth + shift;
		this.graphics.drawLine (x, y, x + segmentWidth, y);
		this.graphics.drawLine (x + segmentWidth, y, x + segmentWidth, y + segmentLenght);
		this.graphics.drawLine (x, y, x, y + segmentLenght);
		this.graphics.drawLine (x, y + segmentLenght, x + segmentWidth, y + segmentLenght);
		x = topLeftX + segmentWidth + shift;
		y = topLeftY + segmentLenght + shift + segmentWidth + shift + segmentLenght + shift + segmentWidth + shift;
		this.graphics.drawLine (x, y, x + segmentLenght, y);
		this.graphics.drawLine (x + segmentLenght, y, x + segmentLenght, y + segmentWidth);
		this.graphics.drawLine (x, y, x, y + segmentWidth);
		this.graphics.drawLine (x, y + segmentWidth, x + segmentLenght, y + segmentWidth);
	}

	private void redrawDigit (Integer topLeftX, Integer topLeftY, String digit)
	{
		Integer segmentLenght = 40;
		Integer segmentWidth = 7;
		Integer shift = 2;
		// ГоризонтальнаA верх
		if (digit.equals ("0") ||
			digit.equals ("2") ||
			digit.equals ("3") ||
			digit.equals ("5") ||
			digit.equals ("6") ||
			digit.equals ("7") ||
			digit.equals ("8") ||
			digit.equals ("9") ||
			digit.equals ("A") ||
			digit.equals ("C") ||
			digit.equals ("E") ||
			digit.equals ("F")) this.graphics.setColor(Color.BLACK);
		else this.graphics.setColor(Color.WHITE);
		Integer x = topLeftX + segmentWidth + shift;
		Integer y = topLeftY;
		this.graphics.fillRect (x + 1, y + 1, segmentLenght - 1, segmentWidth - 1);
		// Вертикальная лево верх
		if (digit.equals ("0") ||
			digit.equals ("4") ||
			digit.equals ("5") ||
			digit.equals ("6") ||
			digit.equals ("8") ||
			digit.equals ("9") ||
			digit.equals ("A") ||
			digit.equals ("B") ||
			digit.equals ("C") ||
			digit.equals ("E") ||
			digit.equals ("F")) this.graphics.setColor(Color.BLACK);
		else this.graphics.setColor(Color.WHITE);
		x = topLeftX;
		y = topLeftY + segmentWidth + shift;
		this.graphics.fillRect (x + 1, y + 1, segmentWidth - 1, segmentLenght - 1);
		// Вертикальная право верх
		if (digit.equals ("0") ||
			digit.equals ("1") ||
			digit.equals ("2") ||
			digit.equals ("3") ||
			digit.equals ("4") ||
			digit.equals ("7") ||
			digit.equals ("8") ||
			digit.equals ("9") ||
			digit.equals ("A") ||
			digit.equals ("D")) this.graphics.setColor(Color.BLACK);
		else this.graphics.setColor(Color.WHITE);
		x = topLeftX + segmentLenght + shift + segmentWidth + shift;
		y = topLeftY + segmentWidth + shift;
		this.graphics.fillRect (x + 1, y + 1, segmentWidth - 1, segmentLenght - 1);
		// Горизонтальная средняя
		if (digit.equals ("2") ||
			digit.equals ("3") ||
			digit.equals ("4") ||
			digit.equals ("5") ||
			digit.equals ("6") ||
			digit.equals ("8") ||
			digit.equals ("9") ||
			digit.equals ("A") ||
			digit.equals ("B") ||
			digit.equals ("D") ||
			digit.equals ("E") ||
			digit.equals ("F")) this.graphics.setColor(Color.BLACK);
		else this.graphics.setColor(Color.WHITE);
		x = topLeftX + segmentWidth + shift;
		y = topLeftY + segmentLenght + shift + segmentWidth + shift;
		this.graphics.fillRect (x + 1, y + 1, segmentLenght - 1, segmentWidth - 1);
		// Вертикальная лево низ
		if (digit.equals ("0") ||
			digit.equals ("2") ||
			digit.equals ("6") ||
			digit.equals ("8") ||
			digit.equals ("A") ||
			digit.equals ("B") ||
			digit.equals ("C") ||
			digit.equals ("D") ||
			digit.equals ("E") ||
			digit.equals ("F")) this.graphics.setColor(Color.BLACK);
		else this.graphics.setColor(Color.WHITE);
		x = topLeftX;
		y = topLeftY + segmentWidth + shift + segmentLenght + shift + segmentWidth + shift;
		this.graphics.fillRect (x + 1, y + 1, segmentWidth - 1, segmentLenght - 1);
		// Вертикальная право низ
		if (digit.equals ("0") ||
			digit.equals ("1") ||
			digit.equals ("3") ||
			digit.equals ("4") ||
			digit.equals ("5") ||
			digit.equals ("6") ||
			digit.equals ("7") ||
			digit.equals ("8") ||
			digit.equals ("9") ||
			digit.equals ("A") ||
			digit.equals ("B") ||
			digit.equals ("D")) this.graphics.setColor(Color.BLACK);
		else this.graphics.setColor(Color.WHITE);
		x = topLeftX + segmentLenght + shift + segmentWidth + shift;
		y = topLeftY + segmentWidth + shift + segmentLenght + shift + segmentWidth + shift;
		this.graphics.fillRect (x + 1, y + 1, segmentWidth - 1, segmentLenght - 1);
		// Горизонтальная низ
		if (digit.equals ("0") ||
			digit.equals ("2") ||
			digit.equals ("3") ||
			digit.equals ("5") ||
			digit.equals ("6") ||
			digit.equals ("8") ||
			digit.equals ("9") ||
			digit.equals ("B") ||
			digit.equals ("C") ||
			digit.equals ("D") ||
			digit.equals ("E")) this.graphics.setColor(Color.BLACK);
		else this.graphics.setColor(Color.WHITE);
		x = topLeftX + segmentWidth + shift;
		y = topLeftY + segmentLenght + shift + segmentWidth + shift + segmentLenght + shift + segmentWidth + shift;
		this.graphics.fillRect (x + 1, y + 1, segmentLenght - 1, segmentWidth - 1);
	}

	private void sleep (Integer n)
	{
		if (n > 0)
		{
			try
			{
				Thread.sleep (n);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace ();
			}
		}
	}	
}