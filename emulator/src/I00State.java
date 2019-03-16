public class I00State
{
	public I00StateInternal internal;
	public I00StateExternal external;
	public I00StateUIBus0 systemBus;

	public I00State (Boolean init)
	{
		if (init)
		{
			this.internal = new I00StateInternal (init);
			this.external = new I00StateExternal (init);
			this.systemBus = I00StateUIBus0.getInstance();
		}
	}

	public I00State copy ()
	{
		I00State i00State = new I00State (false);
		i00State.internal = this.internal.copy ();
		i00State.external = this.external.copy ();
		i00State.systemBus = this.systemBus;
		return i00State;
	}
}