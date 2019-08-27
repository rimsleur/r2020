public class LEDIStateIOD implements SetValue
{
	public BitState b7;
	public BitState b6;
	public BitState b5;
	public BitState b4;
	public BitState b3;
	public BitState b2;
	public BitState b1;
	public BitState b0;

	public LEDIStateIOD (Boolean init)
	{
		if (init)
		{
			this.b7 = BitState.Z;
			this.b6 = BitState.Z;
			this.b5 = BitState.Z;
			this.b4 = BitState.Z;
			this.b3 = BitState.Z;
			this.b2 = BitState.Z;
			this.b1 = BitState.Z;
			this.b0 = BitState.Z;
		}
	}

	public LEDIStateIOD copy ()
	{
		LEDIStateIOD iod = new LEDIStateIOD (false);
		iod.b7 = this.b7;
		iod.b6 = this.b6;
		iod.b5 = this.b5;
		iod.b4 = this.b4;
		iod.b3 = this.b3;
		iod.b2 = this.b2;
		iod.b1 = this.b1;
		iod.b0 = this.b0;
		return iod;
	}

	@Override
	public void setValue (String binaryCode)
	{
		this.b7 = Common.getPinValue (binaryCode.charAt (0));
		this.b6 = Common.getPinValue (binaryCode.charAt (1));
		this.b5 = Common.getPinValue (binaryCode.charAt (2));
		this.b4 = Common.getPinValue (binaryCode.charAt (3));
		this.b3 = Common.getPinValue (binaryCode.charAt (4));
		this.b2 = Common.getPinValue (binaryCode.charAt (5));
		this.b1 = Common.getPinValue (binaryCode.charAt (6));
		this.b0 = Common.getPinValue (binaryCode.charAt (7));
	}

	public void setToState (BitState bitState)
	{
		this.b7 = bitState;
		this.b6 = bitState;
		this.b5 = bitState;
		this.b4 = bitState;
		this.b3 = bitState;
		this.b2 = bitState;
		this.b1 = bitState;
		this.b0 = bitState;
	}
}