import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class RAMCanvas extends ICanvas implements Runnable
{
	public static final Integer WIDTH = 500;
	public static final Integer HEIGHT = WIDTH / 12 * 9;

	private static Integer PINSIDESIZE = 19;
	private static Integer PINSTEPSIZE = PINSIDESIZE + 1;

	private JFrame frame;
	private BufferStrategy bufferStrategy;
	private Graphics graphics;
	private Font font;

	private Boolean running = false;

	private RAM ram = new RAM ();

	private final String[] rowTable = new String[]
			{".0", ".1", ".2", ".3", ".4", ".5", ".6", ".7", ".8", ".9", ".A", ".B", ".C", ".D", ".E", ".F"};
	private final String[] colTable = new String[]
			{"0000", "0010", "0020", "0030", "0040", "0050", "0060", "0070", "0080", "0090", "00A0", "00B0", "00C0", "00D0", "00E0", "00F0"};

	public RAMCanvas()
	{
		setMinimumSize (new Dimension (WIDTH, HEIGHT));
		setMaximumSize (new Dimension (WIDTH, HEIGHT));
		setPreferredSize (new Dimension (WIDTH, HEIGHT));

		this.frame = new JFrame ("RAM");
		this.frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout (new BorderLayout ());
		this.frame.add (this, BorderLayout.CENTER);
		this.frame.pack ();
		this.frame.setResizable (false);
		this.frame.setLocationRelativeTo (null);
		this.frame.setLocation(250, 360);
		this.frame.setVisible (true);
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

		this.ram.setUIBusIndex(0);

		while (this.running)
		{
			calculate ();
			redraw ();
			sleep (30);
		}
	}

	private void calculate ()
	{
		this.ram.calculate (1);
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
		drawHorizontalPin(pinX, pinY,64, "DS", false);

		//отображение RDY
		pinX = 135;
		pinY = 40;
		drawHorizontalPin(pinX, pinY,64, "DRDY", true);


		drawFrame (30, 80, 188, 50, "IOD");
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

		Integer topLeftX = 30;
		Integer topLeftY = 140;
		drawFrame (topLeftX, topLeftY, 361, 217, "Memory map");

		//отрисовка rowTable
		for (int i = 0, x = topLeftX + 43; i < this.rowTable.length; i++, x += 20)
		{
			this.graphics.drawString (this.rowTable[i], x, topLeftY + 20);
		}

		//отрисовка colTable
		for (int i = 0, y = topLeftY + 32; i < this.colTable.length; i++, y += 12)
		{
			this.graphics.drawString (this.colTable[i], topLeftX + 5, y);
		}

		String[] codes = this.ram.getMemoryArray ();

		Integer cellX = topLeftX + 43;
		Integer cellY = topLeftY + 32;

		//отрисовка значения таблицы
		for (int i = 0, v = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++, v++)
			{
				this.graphics.drawString (codes[v], cellX, cellY);
				cellX += 20;
			}
			cellX = topLeftX + 43;
			cellY += 12;
		}
	}

	@Override
	protected final void redrawComponets ()
	{
		Integer pinX;
		Integer pinY;

		RAMState ramState = this.ram.getRAMState();

		//перерисовываю A0
		pinX = 40;
		pinY = 90;
		redrawVerticalPin (pinX, pinY, ramState.iod.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ramState.iod.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ramState.iod.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ramState.iod.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ramState.iod.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ramState.iod.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ramState.iod.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ramState.iod.b0);

		//перерисовываю SEL
		pinX = 40;
		pinY = 40;
		redrawHorizontalPin(pinX, pinY, 64, ramState.ds);

		//перерисовываю RDY
		pinX = 135;
		pinY = 40;
		redrawHorizontalPin(pinX, pinY, 64, ramState.drdy);

		String[] memoryArray = this.ram.getMemoryArray();

		Integer topLeftX = 30;
		Integer topLeftY = 140;
		Integer rowSize = 16;
		Integer row = 0;
		Integer col = 0;

		Integer currentCell = this.ram.getCurrentCell ();
		row = (int) Math.floor (currentCell / rowSize);
		col = currentCell - (row * rowSize);
		//System.out.println (row + ":" + col);

		Integer cellX = topLeftX + 43 + 20 * col;
		Integer cellY = topLeftY + 32 + 12 * row;

		this.graphics.setColor (Color.BLACK);
		this.graphics.fillRect (cellX - 1, cellY - 10, 16, 12);
		this.graphics.setColor (Color.WHITE);
		this.graphics.drawString (memoryArray[currentCell], cellX, cellY);

		if (currentCell == 0) currentCell = 255;
		else currentCell--;
			
		row = (int) Math.floor (currentCell / rowSize);
		col = currentCell - (row * rowSize);
		cellX = topLeftX + 43 + 20 * col;
		cellY = topLeftY + 32 + 12 * row;

		this.graphics.setColor (Color.WHITE);
		this.graphics.fillRect (cellX - 1, cellY - 10, 16, 12);
		this.graphics.setColor (Color.BLACK);
		this.graphics.drawString (memoryArray[currentCell], cellX, cellY);
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