public class I00StateInternalCMDR implements SetValue, GetValue
{
	public BitState b7;
	public BitState b6;
	public BitState b5;
	public BitState b4;
	public BitState b3;
	public BitState b2;
	public BitState b1;
	public BitState b0;

	public I00StateInternalCMDR (Boolean init)
	{
		if (init)
		{
			this.b7 = BitState.L;
			this.b6 = BitState.L;
			this.b5 = BitState.L;
			this.b4 = BitState.L;
			this.b3 = BitState.L;
			this.b2 = BitState.L;
			this.b1 = BitState.L;
			this.b0 = BitState.L;
		}
	}

	public I00StateInternalCMDR copy ()
	{
		I00StateInternalCMDR i00StateInternalCMDR = new I00StateInternalCMDR (false);
		i00StateInternalCMDR.b7 = this.b7;
		i00StateInternalCMDR.b6 = this.b6;
		i00StateInternalCMDR.b5 = this.b5;
		i00StateInternalCMDR.b4 = this.b4;
		i00StateInternalCMDR.b3 = this.b3;
		i00StateInternalCMDR.b2 = this.b2;
		i00StateInternalCMDR.b1 = this.b1;
		i00StateInternalCMDR.b0 = this.b0;
		return i00StateInternalCMDR;
	}

	@Override
	public void setValue(String binaryCode)
	{
		this.b7 = Common.getPinValue(binaryCode.charAt(0));
		this.b6 = Common.getPinValue(binaryCode.charAt(1));
		this.b5 = Common.getPinValue(binaryCode.charAt(2));
		this.b4 = Common.getPinValue(binaryCode.charAt(3));
		this.b3 = Common.getPinValue(binaryCode.charAt(4));
		this.b2 = Common.getPinValue(binaryCode.charAt(5));
		this.b1 = Common.getPinValue(binaryCode.charAt(6));
		this.b0 = Common.getPinValue(binaryCode.charAt(7));
	}

	@Override
	public String getValue()
	{
		String result = Common.getPinState(this.b7) + Common.getPinState(this.b6)
				+ Common.getPinState(this.b5) + Common.getPinState(this.b4)
				+ Common.getPinState(this.b3) + Common.getPinState(this.b2)
				+ Common.getPinState(this.b1) + Common.getPinState(this.b0);

		return result;
	}
}