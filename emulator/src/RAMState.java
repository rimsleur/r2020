public class RAMState
{
	public RAMStateIOD iod;
	public BitState ds;
	public BitState drdy;

	public RAMState (Boolean init)
	{
		if (init)
		{
			this.iod = new RAMStateIOD (init);
			this.ds = BitState.H;
			this.drdy = BitState.Z;
		}
	}

	public RAMState copy ()
	{
		RAMState ramState = new RAMState (false);
		ramState.iod = this.iod.copy ();
		ramState.ds = this.ds;
		ramState.drdy = this.drdy;
		return ramState;
	}
}