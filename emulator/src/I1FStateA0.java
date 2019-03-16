public class I1FStateA0 implements SetValue
{
	public BitState b7;
	public BitState b6;
	public BitState b5;
	public BitState b4;
	public BitState b3;
	public BitState b2;
	public BitState b1;
	public BitState b0;

	public I1FStateA0 (Boolean init)
	{
		if (init)
		{
			this.b7 = BitState.L;
			this.b6 = BitState.L;
			this.b5 = BitState.L;
			this.b4 = BitState.L;
			this.b3 = BitState.L;
			this.b2 = BitState.L;
			this.b1 = BitState.L;
			this.b0 = BitState.L;
		}
	}

	public I1FStateA0 copy ()
	{
		I1FStateA0 i1FStateA0 = new I1FStateA0 (false);
		i1FStateA0.b7 = this.b7;
		i1FStateA0.b6 = this.b6;
		i1FStateA0.b5 = this.b5;
		i1FStateA0.b4 = this.b4;
		i1FStateA0.b3 = this.b3;
		i1FStateA0.b2 = this.b2;
		i1FStateA0.b1 = this.b1;
		i1FStateA0.b0 = this.b0;
		return i1FStateA0;
	}

	@Override
	public void setValue(String binaryCode) {
		this.b7 = Common.getPinValue(binaryCode.charAt(0));
		this.b6 = Common.getPinValue(binaryCode.charAt(1));
		this.b5 = Common.getPinValue(binaryCode.charAt(2));
		this.b4 = Common.getPinValue(binaryCode.charAt(3));
		this.b3 = Common.getPinValue(binaryCode.charAt(4));
		this.b2 = Common.getPinValue(binaryCode.charAt(5));
		this.b1 = Common.getPinValue(binaryCode.charAt(6));
		this.b0 = Common.getPinValue(binaryCode.charAt(7));
	}
}