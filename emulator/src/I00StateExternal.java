public class I00StateExternal {

	public BitState clk;
	public I00StateExternalUI0 i00StateExternalUI0;
	public I00StateExternalUI1 i00StateExternalUI1;

	public I00StateExternal(Boolean init)
	{
		if (init)
		{
			this.clk = BitState.L;
			this.i00StateExternalUI0 = new I00StateExternalUI0(true);
			this.i00StateExternalUI1 = new I00StateExternalUI1(true);
		}
	}

	public I00StateExternal copy()
	{
		I00StateExternal i00StateExternal = new I00StateExternal(false);
		i00StateExternal.clk = this.clk;
		i00StateExternal.i00StateExternalUI0 = this.i00StateExternalUI0.copy();
		i00StateExternal.i00StateExternalUI1 = this.i00StateExternalUI1.copy();

		return i00StateExternal;
	}
}