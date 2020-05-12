import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.awt.image.DataBufferInt;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import javax.swing.*;

public class CPUCanvas extends ICanvas implements Runnable, KeyListener
{
	public static final Integer WIDTH = 1000;
	public static final Integer HEIGHT = WIDTH / 12 * 9;

	private static Integer PINSIDESIZE = 19;
	private static Integer PINSTEPSIZE = PINSIDESIZE + 1;

	private JFrame frame;
	private BufferStrategy bufferStrategy;
	private Graphics graphics;
	private Font font;
	private JLabel status;

	private Boolean running = true;

	private CPU cpu = new CPU();

	//private BufferedImage image = new BufferedImage (WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	//private int[] pixels = ((DataBufferInt) image.getRaster ().getDataBuffer ()).getData ();

	//private Integer count = 0;

	public CPUCanvas ()
	{
		setMinimumSize (new Dimension (WIDTH, HEIGHT));
		setMaximumSize (new Dimension (WIDTH, HEIGHT));
		setPreferredSize (new Dimension (WIDTH, HEIGHT));

		this.frame = new JFrame ("CPU");
		this.frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout (new BorderLayout ());
		this.frame.add (this, BorderLayout.CENTER);
		this.frame.pack ();
		this.frame.setResizable (false);
		this.frame.setLocationRelativeTo (null);
		this.frame.setLocation(250, 10);
		this.frame.setVisible (true);

		addKeyListener (this);
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 32) //если нажат пробел нажат пробел
		{
			if(this.running)  suspend();
			else resume();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { }

	public synchronized void start ()
	{
		//this.running = true;
		new Thread (this).start ();
	}

	public synchronized void stop ()
	{
		this.running = false;
	}

	public synchronized void resume ()
	{
		this.running = true;
	}

	public synchronized void suspend ()
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

		while (1 == 1)
		{
			if (this.running)
			{
				calculate();
				redraw();
			}
			sleep (100);
		}
	}

	private void calculate ()
	{
		this.cpu.calculate (1);
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

		//graphics.drawImage (image, 0, 0, getWidth (), getHeight (), null);

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
		drawText(10, HEIGHT - 6, WIDTH - 11, 19, this.cpu.getStatusText());
		//drawFrame(0, HEIGHT - 21, WIDTH - 1, 20, "");

		this.graphics.dispose ();
		bufferStrategy.show ();
	}

	@Override
	protected final void drawComponets ()
	{
		Integer pinX;
		Integer pinY;

		drawFrame (10, 10, 500, 300, "Internal");

		drawFrame (20, 30, 100, 80, "CTLR1");
		drawHorizontalPin (30, 40, 80, "CMDRDY", false);

		//отрисовка CMDRUN
		drawHorizontalPin(30, 60, 80, "CMDRUN", false);

		//отрисовка UI0MD
		drawHorizontalPin(30, 80, 80, "UI0MD", false);

		drawFrame (130, 30, 178, 50, "CMDR");
		pinX = 140;
		pinY = 40;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		drawFrame (130, 90, 178, 50, "ARG0R");
		pinX = 140;
		pinY = 100;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		drawFrame (130, 150, 178, 50, "ARG1R");
		pinX = 140;
		pinY = 160;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		drawFrame (320, 30, 178, 50, "R0");
		pinX = 330;
		pinY = 40;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		drawFrame (320, 90, 178, 50, "R1");
		pinX = 330;
		pinY = 100;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		drawFrame (320, 150, 178, 50, "R2");
		pinX = 330;
		pinY = 160;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		drawFrame (520, 10, 200, 700, "External");

		//отрисовка UI0
		drawFrame(530, 30, 180, 210, "UI0");

		drawFrame (535, 45, 168, 50, "DA");
		pinX = 540;
		pinY = 55;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		drawFrame (535, 102, 168, 50, "IOD");
		pinX = 540;
		pinY = 115;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка DIR, EREQ, STB, IRDY (External)
		drawFrame (535, 160, 168, 70, "");
		drawHorizontalPin (540, 165, 64, "DIR", false);
		drawHorizontalPin (540, 185, 64, "EREQ", false);
		drawHorizontalPin (540, 205, 64, "CTL", false);
		drawHorizontalPin (635, 165, 64, "STB", true);
		drawHorizontalPin (635, 185, 64, "RDY", true);
		drawHorizontalPin (635, 205, 64, "IREQ", true);

		//отрисовка UI1
		drawFrame(530, 250, 180, 210, "UI1");

		//отрисовка IS (UI1)
		drawFrame (535, 265, 168, 50, "DA");
		pinX = 540;
		pinY = 278;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка IOD (UI1)
		drawFrame (535, 325, 168, 50, "IOD");
		pinX = 540;
		pinY = 338;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка DIR, EREQ, STB, IRDY (U1)
		drawFrame (535, 380, 168, 70, "");
		drawHorizontalPin (540, 385, 64, "DIR", false);
		drawHorizontalPin (540, 405, 64, "EREQ", false);
		drawHorizontalPin (540, 425, 64, "CTL", false);
		drawHorizontalPin (635, 385, 64, "STB", true);
		drawHorizontalPin (635, 405, 64, "RDY", true);
		drawHorizontalPin (635, 425, 64, "IREQ", true);

		//отрисовка UI1
		drawFrame(530, 470, 180, 210, "UI2");

		//отрисовка IS (UI1)
		drawFrame (535, 485, 168, 50, "DA");
		pinX = 540;
		pinY = 498;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка IOD (UI1)
		drawFrame (535, 545, 168, 50, "IOD");
		pinX = 540;
		pinY = 558;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка DIR, EREQ, STB, IRDY (U1)
		drawFrame (535, 600, 168, 70, "");
		drawHorizontalPin (540, 605, 64, "DIR", false);
		drawHorizontalPin (540, 625, 64, "EREQ", false);
		drawHorizontalPin (540, 645, 64, "CTL", false);
		drawHorizontalPin (635, 605, 64, "STB", true);
		drawHorizontalPin (635, 625, 64, "RDY", true);
		drawHorizontalPin (635, 645, 64, "IREQ", true);

		drawHorizontalPin (530, 685, 64, "CLK", false);

		//отрисовка UIBus
		drawFrame(730, 10, 200, 680, "Bus");

		//отрисовка UIBus0
		drawFrame(740, 30, 180, 210, "UB0");

		//отрисовка ISB (UIBus0)
		drawFrame (745, 45, 168, 50, "DA");
		pinX = 750;
		pinY = 55;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка DB (UIBus0)
		drawFrame (745, 102, 168, 50, "IOD");
		pinX = 750;
		pinY = 115;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка RW, STB, RDY (UIBus0)
		drawFrame (745, 160, 168, 70, "");
		drawHorizontalPin (750, 165, 64, "DIR", false);
		drawHorizontalPin (750, 185, 64, "EREQ", false);
		drawHorizontalPin (750, 205, 64, "CTL", false);
		drawHorizontalPin (845, 165, 64, "STB", true);
		drawHorizontalPin (845, 185, 64, "RDY", true);
		drawHorizontalPin (845, 205, 64, "IREQ", true);

		//отрисовка UIBus1
		drawFrame(740, 250, 180, 210, "UB1");

		//отрисовка ISB (UIBus1)
		drawFrame (745, 265, 168, 50, "DA");
		pinX = 750;
		pinY = 278;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка DB (UIBus1)
		drawFrame (745, 325, 168, 50, "IOD");
		pinX = 750;
		pinY = 338;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка RW, STB, RDY (UIBus1)
		drawFrame (745, 380, 168, 70, "");
		drawHorizontalPin (750, 385, 64, "DIR", false);
		drawHorizontalPin (750, 405, 64, "EREQ", false);
		drawHorizontalPin (750, 425, 64, "CTL", false);
		drawHorizontalPin (845, 385, 64, "STB", true);
		drawHorizontalPin (845, 405, 64, "RDY", true);
		drawHorizontalPin (845, 425, 64, "IREQ", true);

		//отрисовка UIBus2
		drawFrame(740, 470, 180, 210, "UB2");

		//отрисовка ISB (UIBus1)
		drawFrame (745, 485, 168, 50, "DA");
		pinX = 750;
		pinY = 498;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка DB (UIBus1)
		drawFrame (745, 545, 168, 50, "IOD");
		pinX = 750;
		pinY = 558;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка RW, STB, RDY (UIBus1)
		drawFrame (745, 600, 168, 70, "");
		drawHorizontalPin (750, 605, 64, "DIR", false);
		drawHorizontalPin (750, 625, 64, "EREQ", false);
		drawHorizontalPin (750, 645, 64, "CTL", false);
		drawHorizontalPin (845, 605, 64, "STB", true);
		drawHorizontalPin (845, 625, 64, "RDY", true);
		drawHorizontalPin (845, 645, 64, "IREQ", true);

		//отрисовка рамки Status Bar
		drawFrame(0, HEIGHT - 21, WIDTH - 1, 20, "");
	}

	@Override
	protected final void redrawComponets ()
	{
		Integer pinX;
		Integer pinY;

		CPUState cpuState = this.cpu.getCPUState ();

		redrawHorizontalPin (30, 40, 80, cpuState.internal.ctlr1.cmdrdy);

		//перерисовываю CMDRUN
		redrawHorizontalPin (30, 60, 80, cpuState.internal.ctlr1.cmdrun);

		//перерисовываю UI0MD
		redrawHorizontalPin (30, 80, 80, cpuState.internal.ctlr1.ui0md);

		redrawHorizontalPin (530, 685, 64, cpuState.external.clk);

		//перерисовываю CMDR
		pinX = 140;
		pinY = 40;
		redrawVerticalPin (pinX, pinY, cpuState.internal.cmdr.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.cmdr.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.cmdr.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.cmdr.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.cmdr.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.cmdr.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.cmdr.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.cmdr.b0);

		//перерисовываю ARG0R
		pinX = 140;
		pinY = 100;
		redrawVerticalPin (pinX, pinY, cpuState.internal.arg0r.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg0r.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg0r.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg0r.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg0r.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg0r.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg0r.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg0r.b0);

		//перерисовываю ARG1R
		pinX = 140;
		pinY = 160;
		redrawVerticalPin (pinX, pinY, cpuState.internal.arg1r.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg1r.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg1r.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg1r.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg1r.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg1r.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg1r.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.arg1r.b0);

		//перерисовываю R0
		pinX = 330;
		pinY = 40;
		redrawVerticalPin (pinX, pinY, cpuState.internal.r0.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r0.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r0.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r0.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r0.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r0.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r0.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r0.b0);

		//перерисовываю R1
		pinX = 330;
		pinY = 100;
		redrawVerticalPin (pinX, pinY, cpuState.internal.r1.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r1.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r1.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r1.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r1.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r1.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r1.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r1.b0);

		//перерисовываю R2
		pinX = 330;
		pinY = 160;
		redrawVerticalPin (pinX, pinY, cpuState.internal.r2.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r2.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r2.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r2.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r2.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r2.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r2.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.internal.r2.b0);

		//перерисовываю DA (UI0)
		pinX = 540;
		pinY = 55;
		redrawVerticalPin (pinX, pinY, cpuState.external.ui0.da.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.da.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.da.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.da.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.da.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.da.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.da.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.da.b0);

		//перерисовываю IOD (UI0)
		pinX = 540;
		pinY = 115;
		redrawVerticalPin (pinX, pinY, cpuState.external.ui0.iod.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.iod.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.iod.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.iod.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.iod.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.iod.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.iod.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui0.iod.b0);

		redrawHorizontalPin (540, 165, 64, cpuState.external.ui0.dir);
		redrawHorizontalPin (540, 185, 64, cpuState.external.ui0.ereq);
		redrawHorizontalPin (540, 205, 64, cpuState.external.ui0.ctl);
		redrawHorizontalPin (635, 165, 64, cpuState.external.ui0.stb);
		redrawHorizontalPin (635, 185, 64, cpuState.external.ui0.rdy);
		redrawHorizontalPin (635, 205, 64, cpuState.external.ui0.ireq);

		//перерисовываю DA (UI1)
		pinX = 540;
		pinY = 278;
		redrawVerticalPin (pinX, pinY, cpuState.external.ui1.da.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.da.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.da.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.da.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.da.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.da.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.da.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.da.b0);

		//перерисовываю IOD (UI1)
		pinX = 540;
		pinY = 338;
		redrawVerticalPin (pinX, pinY, cpuState.external.ui1.iod.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.iod.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.iod.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.iod.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.iod.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.iod.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.iod.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui1.iod.b0);

		redrawHorizontalPin (540, 385, 64, cpuState.external.ui1.dir);
		redrawHorizontalPin (540, 405, 64, cpuState.external.ui1.ereq);
		redrawHorizontalPin (540, 425, 64, cpuState.external.ui1.ctl);
		redrawHorizontalPin (635, 385, 64, cpuState.external.ui1.stb);
		redrawHorizontalPin (635, 405, 64, cpuState.external.ui1.rdy);
		redrawHorizontalPin (635, 425, 64, cpuState.external.ui1.ireq);

		//перерисовываю DA (UI2)
		pinX = 540;
		pinY = 498;
		redrawVerticalPin (pinX, pinY, cpuState.external.ui2.da.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.da.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.da.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.da.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.da.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.da.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.da.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.da.b0);

		//перерисовываю IOD (UI2)
		pinX = 540;
		pinY = 558;
		redrawVerticalPin (pinX, pinY, cpuState.external.ui2.iod.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.iod.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.iod.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.iod.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.iod.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.iod.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.iod.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, cpuState.external.ui2.iod.b0);

		redrawHorizontalPin (540, 605, 64, cpuState.external.ui2.dir);
		redrawHorizontalPin (540, 625, 64, cpuState.external.ui2.ereq);
		redrawHorizontalPin (540, 645, 64, cpuState.external.ui2.ctl);
		redrawHorizontalPin (635, 605, 64, cpuState.external.ui2.stb);
		redrawHorizontalPin (635, 625, 64, cpuState.external.ui2.rdy);
		redrawHorizontalPin (635, 645, 64, cpuState.external.ui2.ireq);

		UB0State ub0State = UB0State.getInstance();

		UB0StateDA da = ub0State.getDABitState();

		//перерисовываю DA (UIBus0)
		pinX = 750;
		pinY = 55;
		redrawVerticalPin (pinX, pinY, da.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, da.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, da.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, da.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, da.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, da.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, da.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, da.b0);

		UB0StateIOD iod = ub0State.getIODBitState ();

		//перерисовываю IOD (UIBus0)
		pinX = 750;
		pinY = 115;
		redrawVerticalPin (pinX, pinY, iod.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, iod.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, iod.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, iod.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, iod.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, iod.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, iod.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, iod.b0);

		redrawHorizontalPin (750, 165, 64, ub0State.getDir());
		redrawHorizontalPin (750, 185, 64, ub0State.getEreq());
		redrawHorizontalPin (750, 205, 64, ub0State.getCtl());
		redrawHorizontalPin (845, 165, 64, ub0State.getStb());
		redrawHorizontalPin (845, 185, 64, ub0State.getRdy());
		redrawHorizontalPin (845, 205, 64, ub0State.getIreq());

		UB1State ub1State = UB1State.getInstance();

		UB1StateDA ub1StateDA = ub1State.getDABitState();

		//перерисовываю DA (UIBus1)
		pinX = 750;
		pinY = 278;
		redrawVerticalPin (pinX, pinY, ub1StateDA.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateDA.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateDA.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateDA.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateDA.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateDA.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateDA.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateDA.b0);

		UB1StateIOD ub1StateIOD = ub1State.getIODBitState();

		//перерисовываю IOD (UIBus1)
		pinX = 750;
		pinY = 338;
		redrawVerticalPin (pinX, pinY, ub1StateIOD.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateIOD.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateIOD.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateIOD.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateIOD.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateIOD.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateIOD.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub1StateIOD.b0);

		redrawHorizontalPin (750, 385, 64, ub1State.getDir());
		redrawHorizontalPin (750, 405, 64, ub1State.getEreq());
		redrawHorizontalPin (750, 425, 64, ub1State.getCtl());
		redrawHorizontalPin (845, 385, 64, ub1State.getStb());
		redrawHorizontalPin (845, 405, 64, ub1State.getRdy());
		redrawHorizontalPin (845, 425, 64, ub1State.getIreq());

		UB2State ub2State = UB2State.getInstance();

		UB2StateDA ub2StateDA = ub2State.getDABitState();

		//перерисовываю DA (UIBus2)
		pinX = 750;
		pinY = 498;
		redrawVerticalPin (pinX, pinY, ub2StateDA.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateDA.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateDA.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateDA.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateDA.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateDA.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateDA.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateDA.b0);

		UB2StateIOD ub2StateIOD = ub2State.getIODBitState();

		//перерисовываю IOD (UIBus2)
		pinX = 750;
		pinY = 558;
		redrawVerticalPin (pinX, pinY, ub2StateIOD.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateIOD.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateIOD.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateIOD.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateIOD.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateIOD.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateIOD.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, ub2StateIOD.b0);

		redrawHorizontalPin (750, 605, 64, ub2State.getDir());
		redrawHorizontalPin (750, 625, 64, ub2State.getEreq());
		redrawHorizontalPin (750, 645, 64, ub2State.getCtl());
		redrawHorizontalPin (845, 605, 64, ub2State.getStb());
		redrawHorizontalPin (845, 625, 64, ub2State.getRdy());
		redrawHorizontalPin (845, 645, 64, ub2State.getIreq());
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