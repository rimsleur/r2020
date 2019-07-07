public class CPUStateInternal
{
	public CPUStateInternalCTLR1 ctlr1;
	public CPUStateInternalCMDR cmdr;
	public CPUStateInternalARG0R arg0r;
	public CPUStateInternalARG1R arg1r;
	public CPUStateInternalR0 r0;
	public CPUStateInternalR1 r1;
	public CPUStateInternalR2 r2;

	public CPUStateInternal (Boolean init)
	{
		if (init)
		{
			this.ctlr1 = new CPUStateInternalCTLR1 (init);
			this.cmdr = new CPUStateInternalCMDR (init);
			this.arg0r = new CPUStateInternalARG0R (init);
			this.arg1r = new CPUStateInternalARG1R (init);
			this.r0 = new CPUStateInternalR0 (init);
			this.r1 = new CPUStateInternalR1 (init);
			this.r2 = new CPUStateInternalR2 (init);
		}
	}

	public CPUStateInternal copy ()
	{
		CPUStateInternal internal = new CPUStateInternal (false);
		internal.ctlr1 = this.ctlr1.copy ();
		internal.cmdr = this.cmdr.copy ();
		internal.arg0r = this.arg0r.copy ();
		internal.arg1r = this.arg1r.copy ();
		internal.r0 = this.r0.copy ();
		internal.r1 = this.r1.copy ();
		internal.r2 = this.r2.copy ();
		return internal;
	}
}