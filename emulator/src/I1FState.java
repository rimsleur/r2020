public class I1FState
{
	public I1FStateA0 a0;
	public BitState sel;
	public BitState rdy;

	public I1FState (Boolean init)
	{
		if (init)
		{
			this.a0 = new I1FStateA0 (init);
			this.sel = BitState.H;
			this.rdy = BitState.Z;
		}
	}

	public I1FState copy ()
	{
		I1FState i1FState = new I1FState (false);
		i1FState.a0 = this.a0.copy ();
		i1FState.sel = this.sel;
		i1FState.rdy = this.rdy;
		return i1FState;
	}
}