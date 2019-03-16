import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.awt.image.DataBufferInt;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import javax.swing.*;

public class I00Canvas extends ICanvas implements Runnable, KeyListener
{
	public static final Integer WIDTH = 950;
	public static final Integer HEIGHT = WIDTH / 12 * 9;

	private static Integer PINSIDESIZE = 19;
	private static Integer PINSTEPSIZE = PINSIDESIZE + 1;

	private JFrame frame;
	private BufferStrategy bufferStrategy;
	private Graphics graphics;
	private Font font;
	private JLabel status;

	private Boolean running = false;

	private I00 i00 = new I00();

	//private BufferedImage image = new BufferedImage (WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	//private int[] pixels = ((DataBufferInt) image.getRaster ().getDataBuffer ()).getData ();

	//private Integer count = 0;

	public I00Canvas ()
	{
		setMinimumSize (new Dimension (WIDTH, HEIGHT));
		setMaximumSize (new Dimension (WIDTH, HEIGHT));
		setPreferredSize (new Dimension (WIDTH, HEIGHT));

		this.frame = new JFrame ("I00");
		this.frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout (new BorderLayout ());
		this.frame.add (this, BorderLayout.CENTER);
		this.frame.pack ();
		this.frame.setResizable (false);
		this.frame.setLocationRelativeTo (null);

		this.frame.setVisible (true);

		addKeyListener(this);
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
		this.running = true;
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
			sleep (500);
		}
	}

	private void calculate ()
	{
		this.i00.calculate (1);
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
		drawText(10, HEIGHT - 6, WIDTH - 11, 19, this.i00.getStatusText());
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

		drawFrame (520, 10, 200, 435, "External");

		//отрисовка UI0
		drawFrame(530, 30, 180, 190, "UI0");

		drawFrame (535, 45, 168, 50, "IS");
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

		drawHorizontalPin (530, 420, 64, "CLK", false);

		//отрисовка DIR, EREQ, STB, IRDY (External)
		drawFrame (535, 160, 168, 50, "");
		drawHorizontalPin (545, 165, 64, "DIR", false);
		drawHorizontalPin (545, 185, 64, "EREQ", false);
		drawHorizontalPin (635, 165, 64, "STB", false);
		drawHorizontalPin (635, 185, 64, "IRDY", true);

		//отрисовка UI1
		drawFrame(530, 230, 180, 185, "UI1");

		//отрисовка IS (UI1)
		drawFrame (535, 245, 168, 50, "IS");
		pinX = 540;
		pinY = 258;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка IOD (UI1)
		drawFrame (535, 305, 168, 50, "IOD");
		pinX = 540;
		pinY = 318;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка DIR, EREQ, STB, IRDY (U1)
		drawFrame (535, 360, 168, 50, "");
		drawHorizontalPin (545, 365, 64, "DIR", false);
		drawHorizontalPin (545, 385, 64, "EREQ", false);
		drawHorizontalPin (635, 365, 64, "STB", false);
		drawHorizontalPin (635, 385, 64, "IRDY", true);

		//отрисовка UIBus
		drawFrame(730, 10, 200,435, "UIBus");

		//отрисовка UIBus0
		drawFrame(740, 30, 180,190, "UIBus0");

		//отрисовка ISB (UIBus0)
		drawFrame (745, 45, 168, 50, "ISB");
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
		drawFrame (745, 102, 168, 50, "DB");
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
		drawFrame (745, 160, 168, 50, "");
		drawHorizontalPin (750, 165, 64, "DIR", false);
		drawHorizontalPin (750, 185, 64, "EREQ", false);
		drawHorizontalPin (845, 165, 64, "STB", false);
		drawHorizontalPin (845, 185, 64, "IRDY", true);

		//отрисовка UIBus1
		drawFrame(740, 230, 180,185, "UIBus1");

		//отрисовка ISB (UIBus1)
		drawFrame (745, 245, 168, 50, "ISB");
		pinX = 750;
		pinY = 258;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка DB (UIBus1)
		drawFrame (745, 305, 168, 50, "DB");
		pinX = 750;
		pinY = 318;
		drawVerticalPin (pinX, pinY, "B7", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B6", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B5", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B4", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B3", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B2", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B1", false);
		drawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, "B0", false);

		//отрисовка RW, STB, RDY (UIBus1)
		drawFrame (745, 360, 168, 50, "");
		drawHorizontalPin (750, 365, 64, "DIR", false);
		drawHorizontalPin (750, 385, 64, "EREQ", false);
		drawHorizontalPin (845, 365, 64, "STB", false);
		drawHorizontalPin (845, 385, 64, "IRDY", true);

		//отрисовка рамки Status Bar
		drawFrame(0, HEIGHT - 21, WIDTH - 1, 20, "");
	}

	@Override
	protected final void redrawComponets ()
	{
		Integer pinX;
		Integer pinY;

		I00State i00State = this.i00.getI00State ();

		redrawHorizontalPin (30, 40, 80, i00State.internal.ctlr1.cmdrdy);

		//перерисовываю CMDRUN
		redrawHorizontalPin (30, 60, 80, i00State.internal.ctlr1.cmdrun);

		//перерисовываю UI0MD
		redrawHorizontalPin (30, 80, 80, i00State.internal.ctlr1.ui0md);

		pinX = 540;
		pinY = 55;
		redrawVerticalPin (pinX, pinY, i00State.external.i00StateExternalUI0.is.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.is.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.is.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.is.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.is.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.is.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.is.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.is.b0);

		pinX = 540;
		pinY = 115;
		redrawVerticalPin (pinX, pinY, i00State.external.i00StateExternalUI0.iod.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.iod.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.iod.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.iod.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.iod.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.iod.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.iod.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI0.iod.b0);

		redrawHorizontalPin (530, 420, 64, i00State.external.clk);

		//перерисовываю CMDR
		pinX = 140;
		pinY = 40;
		redrawVerticalPin (pinX, pinY, i00State.internal.cmdr.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.cmdr.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.cmdr.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.cmdr.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.cmdr.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.cmdr.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.cmdr.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.cmdr.b0);

		//перерисовываю ARG0R
		pinX = 140;
		pinY = 100;
		redrawVerticalPin (pinX, pinY, i00State.internal.arg0r.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg0r.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg0r.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg0r.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg0r.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg0r.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg0r.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg0r.b0);

		//перерисовываю ARG1R
		pinX = 140;
		pinY = 160;
		redrawVerticalPin (pinX, pinY, i00State.internal.arg1r.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg1r.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg1r.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg1r.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg1r.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg1r.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg1r.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.arg1r.b0);

		//перерисовываю R0
		pinX = 330;
		pinY = 40;
		redrawVerticalPin (pinX, pinY, i00State.internal.r0.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r0.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r0.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r0.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r0.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r0.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r0.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r0.b0);

		//перерисовываю R1
		pinX = 330;
		pinY = 100;
		redrawVerticalPin (pinX, pinY, i00State.internal.r1.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r1.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r1.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r1.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r1.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r1.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r1.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r1.b0);

		//перерисовываю R2
		pinX = 330;
		pinY = 160;
		redrawVerticalPin (pinX, pinY, i00State.internal.r2.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r2.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r2.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r2.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r2.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r2.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r2.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.internal.r2.b0);

		//перерисовываю DIR (UI0)
		redrawHorizontalPin (545, 165, 64, i00State.external.i00StateExternalUI0.dir);

		//перерисовываю EREQ (UI0)
		redrawHorizontalPin (545, 185, 64, i00State.external.i00StateExternalUI0.ereq);

		//перерисовываю STB (UI0)
		redrawHorizontalPin (635, 165, 64, i00State.external.i00StateExternalUI0.stb);

		//перерисовываю IRDY (UI0)
		redrawHorizontalPin (635, 185, 64, i00State.external.i00StateExternalUI0.irdy);

		//перерисовываю IS (UI1)
		pinX = 540;
		pinY = 258;
		redrawVerticalPin (pinX, pinY, i00State.external.i00StateExternalUI1.is.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.is.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.is.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.is.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.is.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.is.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.is.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.is.b0);

		//перерисовываю IOD (UI1)
		pinX = 540;
		pinY = 318;
		redrawVerticalPin (pinX, pinY, i00State.external.i00StateExternalUI1.iod.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.iod.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.iod.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.iod.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.iod.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.iod.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.iod.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, i00State.external.i00StateExternalUI1.iod.b0);

		//перерисовываю DIR (UI1)
		redrawHorizontalPin (545, 365, 64, i00State.external.i00StateExternalUI1.dir);

		//перерисовываю EREQ (UI1)
		redrawHorizontalPin (545, 385, 64, i00State.external.i00StateExternalUI1.ereq);

		//перерисовываю STB (UI1)
		redrawHorizontalPin (635, 365, 64, i00State.external.i00StateExternalUI1.stb);

		//перерисовываю IRDY (UI1)
		redrawHorizontalPin (635, 385, 64, i00State.external.i00StateExternalUI1.irdy);

		I00StateUIBus0 uIBus0 = I00StateUIBus0.getInstance();

		I00StateUIBus0ISB isb = uIBus0.getIsbBitState();

		//перерисовываю ISB (UIBus0)
		pinX = 750;
		pinY = 55;
		redrawVerticalPin (pinX, pinY, isb.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, isb.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, isb.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, isb.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, isb.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, isb.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, isb.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, isb.b0);

		I00StateUIBus0DB db = uIBus0.getDbBitState();

		//перерисовываю DB (UIBusBus0)
		pinX = 750;
		pinY = 115;
		redrawVerticalPin (pinX, pinY, db.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, db.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, db.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, db.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, db.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, db.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, db.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, db.b0);

		//перерисовываю DIR (UIBus0)
		redrawHorizontalPin (750, 165, 64, uIBus0.getDir());

		//перерисовываю EREQ (UIBus0)
		redrawHorizontalPin (750, 185, 64, uIBus0.getEreq());

		//перерисовываю STB (UIBus0)
		redrawHorizontalPin (845, 165, 64, uIBus0.getStb());

		//перерисовываю IRDY (UIBus0)
		redrawHorizontalPin (845, 185, 64, uIBus0.getIrdy());

		I00StateUIBus1 uIBus1 = I00StateUIBus1.getInstance();

		I00StateUIBus1ISB uIisb = uIBus1.getIsbBitState();

		//перерисовываю ISB (UIBus1)
		pinX = 750;
		pinY = 258;
		redrawVerticalPin (pinX, pinY, uIisb.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIisb.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIisb.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIisb.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIisb.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIisb.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIisb.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIisb.b0);

		I00StateUIBus1DB uIdb = uIBus1.getDbBitState();

		//перерисовываю DB (UIBusBus1)
		pinX = 750;
		pinY = 318;
		redrawVerticalPin (pinX, pinY, uIdb.b7);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIdb.b6);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIdb.b5);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIdb.b4);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIdb.b3);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIdb.b2);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIdb.b1);
		redrawVerticalPin (pinX = pinX + PINSTEPSIZE, pinY, uIdb.b0);

		//перерисовываю DIR (UIBus1)
		redrawHorizontalPin (750, 365, 64, uIBus1.getDir());

		//перерисовываю EREQ (UIBus1)
		redrawHorizontalPin (750, 385, 64, uIBus1.getEreq());

		//перерисовываю STB (UIBus1)
		redrawHorizontalPin (845, 365, 64, uIBus1.getStb());

		//перерисовываю IRDY (UIBus1)
		redrawHorizontalPin (845, 385, 64, uIBus1.getIrdy());
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