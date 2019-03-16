public class I00StateInternalCTLR1
{
	public BitState cmdrdy;
	public BitState cmdrun;
	public BitState ui0md;

	public I00StateInternalCTLR1 (Boolean init)
	{
		if (init)
		{
			this.cmdrdy = BitState.L;
			this.cmdrun = BitState.L;
			this.ui0md = BitState.L;
		}
	}

	public I00StateInternalCTLR1 copy ()
	{
		I00StateInternalCTLR1 i00StateInternalCTLR1 = new I00StateInternalCTLR1 (false);
		i00StateInternalCTLR1.cmdrdy = this.cmdrdy;
		i00StateInternalCTLR1.cmdrun = this.cmdrun;
		i00StateInternalCTLR1.ui0md = this.ui0md;
		return i00StateInternalCTLR1;
	}
}