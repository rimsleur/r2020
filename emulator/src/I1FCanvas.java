import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class I1FCanvas extends ICanvas implements Runnable
{
	public static final Integer WIDTH = 250;
	public static final Integer HEIGHT = WIDTH / 12 * 9;

	private static Integer PINSIDESIZE = 19;
	private static Integer PINSTEPSIZE = PINSIDESIZE + 1;

	private JFrame frame;
	private BufferStrategy bufferStrategy;
	private Graphics graphics;
	private Font font;

	private Boolean running = false;

	private I1F i1F = new I1F ();

	public I1FCanvas ()
	{
		setMinimumSize (new Dimension (WIDTH, HEIGHT));
		setMaximumSize (new Dimension (WIDTH, HEIGHT));
		setPreferredSize (new Dimension (WIDTH, HEIGHT));

		this.frame = new JFrame ("I10-I1F");
		this.frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout (new BorderLayout ());
		this.frame.add (this, BorderLayout.CENTER);
		this.frame.pack ();
		this.frame.setResizable (false);
		this.frame.setLocationRelativeTo (null);
		this.frame.setVisible (true);
		this.frame.setLocation(900, 550);
	}

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

		this.i1F.setUIBusIndex(1);

		while (this.running)
		{
			calculate ();
			redraw ();
			sleep (30);
		}
	}

	private void calculate ()
	{
		this.i1F.calculate (1);
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
		Integer pinX;
		Integer pinY;

		//отображение флагов I1F
		drawFrame(30, 30, 188, 40, "");

		//отображение SEL
		pinX = 40;
		pinY = 40;
		drawHorizontalPin(pinX, pinY,64, "SEL", false);

		//отображение RDY
		pinX = 135;
		pinY = 40;
		drawHorizontalPin(pinX, pinY,64, "RDY", false);


		drawFrame (30, 80, 188, 50, "A0");
		pinX = 40;
		pinY = 90;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);
	}

	@Override
	protected final void redrawComponets ()
	{
		Integer pinX;
		Integer pinY;

		I1FState i1FState = this.i1F.getI1FState ();

		//перерисовываю A0
		pinX = 40;
		pinY = 90;
		redrawVerticalPin (pinX, pinY, i1FState.a0.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i1FState.a0.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i1FState.a0.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i1FState.a0.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i1FState.a0.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i1FState.a0.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i1FState.a0.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i1FState.a0.b0);

		//перерисовываю SEL
		pinX = 40;
		pinY = 40;
		redrawHorizontalPin(pinX, pinY, 64, i1FState.sel);

		//перерисовываю RDY
		pinX = 135;
		pinY = 40;
		redrawHorizontalPin(pinX, pinY, 64, i1FState.rdy);
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