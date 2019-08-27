public class CPUStateExternal
{

	public BitState clk;
	public CPUStateExternalUI0 ui0;
	public CPUStateExternalUI1 ui1;

	public CPUStateExternal (Boolean init)
	{
		if (init)
		{
			this.clk = BitState.L;
			this.ui0 = new CPUStateExternalUI0 (true);
			this.ui1 = new CPUStateExternalUI1 (true);
		}
	}

	public CPUStateExternal copy ()
	{
		CPUStateExternal cpuStateExternal = new CPUStateExternal (false);
		cpuStateExternal.clk = this.clk;
		cpuStateExternal.ui0 = this.ui0.copy ();
		cpuStateExternal.ui1 = this.ui1.copy ();

		return cpuStateExternal;
	}
}