public class Emulator
{
	public static void main(String[] args)
	{
		new CPUCanvas().start();
		new RAMCanvas().start();
		new LEDICanvas ().start ();
	}
}
