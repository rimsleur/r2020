public class I00StateInternal
{
	public I00StateInternalCTLR1 ctlr1;
	public I00StateInternalCMDR cmdr;
	public I00StateInternalARG0R arg0r;
	public I00StateInternalARG1R arg1r;
	public I00StateInternalR0 r0;
	public I00StateInternalR1 r1;
	public I00StateInternalR2 r2;

	public I00StateInternal (Boolean init)
	{
		if (init)
		{
			this.ctlr1 = new I00StateInternalCTLR1 (init);
			this.cmdr = new I00StateInternalCMDR (init);
			this.arg0r = new I00StateInternalARG0R (init);
			this.arg1r = new I00StateInternalARG1R (init);
			this.r0 = new I00StateInternalR0 (init);
			this.r1 = new I00StateInternalR1 (init);
			this.r2 = new I00StateInternalR2 (init);
		}
	}

	public I00StateInternal copy ()
	{
		I00StateInternal i00StateInternal = new I00StateInternal (false);
		i00StateInternal.ctlr1 = this.ctlr1.copy ();
		i00StateInternal.cmdr = this.cmdr.copy ();
		i00StateInternal.arg0r = this.arg0r.copy ();
		i00StateInternal.arg1r = this.arg1r.copy ();
		i00StateInternal.r0 = this.r0.copy ();
		i00StateInternal.r1 = this.r1.copy ();
		i00StateInternal.r2 = this.r2.copy ();
		return i00StateInternal;
	}
}