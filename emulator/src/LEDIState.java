public class LEDIState
{
	public LEDIStateIOD iod;
	public BitState ds;
	public BitState rdy;
	public BitState stb;
	public BitState ireq;
	public String digit1 = " ";
	public String digit2 = " ";

	public LEDIState (Boolean init)
	{
		if (init)
		{
			this.iod = new LEDIStateIOD (init);
			this.ds = BitState.H;
			this.rdy = BitState.Z;
			this.stb = BitState.Z;
			this.ireq = BitState.Z;
		}
	}

	public LEDIState copy ()
	{
		LEDIState lediState = new LEDIState (false);
		lediState.iod = this.iod.copy ();
		lediState.ds = this.ds;
		lediState.rdy = this.rdy;
		lediState.stb = this.stb;
		lediState.ireq = this.ireq;
		return lediState;
	}
}