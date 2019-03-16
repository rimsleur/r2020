public class I20State
{
	public I20StateA0 a0;
	public BitState sel;
	public BitState rdy;

	public I20State(Boolean init)
	{
		if (init)
		{
			this.a0 = new I20StateA0 (init);
			this.sel = BitState.H;
			this.rdy = BitState.Z;
		}
	}

	public I20State copy ()
	{
		I20State i1FState = new I20State(false);
		i1FState.a0 = this.a0.copy();
		i1FState.sel = this.sel;
		i1FState.rdy = this.rdy;
		return i1FState;
	}
}