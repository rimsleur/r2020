public class CPUState
{
	public CPUStateInternal internal;
	public CPUStateExternal external;

	public CPUState (Boolean init)
	{
		if (init)
		{
			this.internal = new CPUStateInternal (init);
			this.external = new CPUStateExternal (init);
		}
	}

	public CPUState copy ()
	{
		CPUState cpuState = new CPUState (false);
		cpuState.internal = this.internal.copy ();
		cpuState.external = this.external.copy ();
		return cpuState;
	}
}