public class UB2StateIOD implements SetValue, GetValue
{
    public BitState b7;
    public BitState b6;
    public BitState b5;
    public BitState b4;
    public BitState b3;
    public BitState b2;
    public BitState b1;
    public BitState b0;

    public UB2StateIOD (Boolean init)
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

    public UB2StateIOD copy ()
    {
        UB2StateIOD iod = new UB2StateIOD (false);
        iod.b7 = this.b7;
        iod.b6 = this.b6;
        iod.b5 = this.b5;
        iod.b4 = this.b4;
        iod.b3 = this.b3;
        iod.b2 = this.b2;
        iod.b1 = this.b1;
        iod.b0 = this.b0;
        return iod;
    }

    @Override
    public String getValue ()
    {
        String result = Common.getPinState(this.b7) + Common.getPinState(this.b6)
                + Common.getPinState(this.b5) + Common.getPinState(this.b4)
                + Common.getPinState(this.b3) + Common.getPinState(this.b2)
                + Common.getPinState(this.b1) + Common.getPinState(this.b0);

        return result;
    }

    @Override
    public void setValue (String binaryCode)
    {
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