public class CPUStateInternalCTLR1
{
	public BitState cmdrdy;
	public BitState cmdrun;
	public BitState ui0md;

	public CPUStateInternalCTLR1 (Boolean init)
	{
		if (init)
		{
			this.cmdrdy = BitState.L;
			this.cmdrun = BitState.L;
			this.ui0md = BitState.L;
		}
	}

	public CPUStateInternalCTLR1 copy ()
	{
		CPUStateInternalCTLR1 ctrl1 = new CPUStateInternalCTLR1 (false);
		ctrl1.cmdrdy = this.cmdrdy;
		ctrl1.cmdrun = this.cmdrun;
		ctrl1.ui0md = this.ui0md;
		return ctrl1;
	}
}