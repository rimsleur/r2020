public class CPUStateExternal
{

	public BitState clk;
	public CPUStateExternalUI0 cpuStateExternalUI0;
	public CPUStateExternalUI1 cpuStateExternalUI1;

	public CPUStateExternal (Boolean init)
	{
		if (init)
		{
			this.clk = BitState.L;
			this.cpuStateExternalUI0 = new CPUStateExternalUI0(true);
			this.cpuStateExternalUI1 = new CPUStateExternalUI1(true);
		}
	}

	public CPUStateExternal copy()
	{
		CPUStateExternal cpuStateExternal = new CPUStateExternal(false);
		cpuStateExternal.clk = this.clk;
		cpuStateExternal.cpuStateExternalUI0 = this.cpuStateExternalUI0.copy();
		cpuStateExternal.cpuStateExternalUI1 = this.cpuStateExternalUI1.copy();

		return cpuStateExternal;
	}
}