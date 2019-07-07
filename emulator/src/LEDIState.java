public class LEDIState
{
	public LEDIStateIOD iod;
	public BitState ds;
	public BitState drdy;

	public LEDIState (Boolean init)
	{
		if (init)
		{
			this.iod = new LEDIStateIOD (init);
			this.ds = BitState.H;
			this.drdy = BitState.Z;
		}
	}

	public LEDIState copy ()
	{
		LEDIState lediState = new LEDIState (false);
		lediState.iod = this.iod.copy ();
		lediState.ds = this.ds;
		lediState.drdy = this.drdy;
		return lediState;
	}
}