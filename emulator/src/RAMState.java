public class RAMState
{
	public RAMStateIOD iod;
	public BitState ds;
	public BitState rdy;
	public BitState stb;
	public BitState ireq;

	public RAMState (Boolean init)
	{
		if (init)
		{
			this.iod = new RAMStateIOD (init);
			this.ds = BitState.H;
			this.rdy = BitState.Z;
			this.stb = BitState.Z;
			this.ireq = BitState.Z;
		}
	}

	public RAMState copy ()
	{
		RAMState ramState = new RAMState (false);
		ramState.iod = this.iod.copy ();
		ramState.ds = this.ds;
		ramState.rdy = this.rdy;
		ramState.stb = this.stb;
		ramState.ireq = this.ireq;
		return ramState;
	}
}