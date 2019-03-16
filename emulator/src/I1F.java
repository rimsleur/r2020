public class I1F
{
    public I1FState i1FState;
    public final String runCommand = "00011111";
    private Integer currentUIBusIndex = null;

    public I1F ()
    {
        this.i1FState = new I1FState (true);
    }

    public void setUIBusIndex(Integer portNumb)
    {
        this.currentUIBusIndex = portNumb;
    }

    public void calculate (Integer times)
    {
        for (int i = 1; i <= times; i++)
        {
            String isbUIbinary = Common.getUIBusRegisterValue(this.currentUIBusIndex, "ISB");

            I00StateUIBus0 uiBus0 = I00StateUIBus0.getInstance();
            I00StateUIBus1 uiBus1 = I00StateUIBus1.getInstance();

            if (runCommand.equals(isbUIbinary)) this.i1FState.sel = BitState.H;
            else this.i1FState.sel = BitState.L;


            if (this.currentUIBusIndex == 0)
            {
                if (uiBus0.getDir() == BitState.H && uiBus0.getEreq() == BitState.H && uiBus0.getStb() == BitState.L)
                {
                    if (this.i1FState.rdy == BitState.Z)
                    {
                        this.i1FState.rdy = BitState.L;
                        uiBus0.setIrdy(BitState.L);
                    }
                }

                if (this.i1FState.sel == BitState.H && this.i1FState.rdy == BitState.L && uiBus0.getStb() == BitState.H)
                {
                    String binaryDB = Common.getUIBusRegisterValue(0, "DB");
                    Common.setA0(this.i1FState.a0, binaryDB);
                    this.i1FState.rdy = BitState.Z;
                    uiBus0.setIrdy(BitState.H);
                }
            }

            if (this.currentUIBusIndex == 1)
            {
                if (uiBus1.getDir() == BitState.H && uiBus1.getEreq() == BitState.H && uiBus1.getStb() == BitState.L)
                {
                    if (this.i1FState.rdy == BitState.Z)
                    {
                        this.i1FState.rdy = BitState.L;
                        uiBus1.setIrdy(BitState.L);
                    }
                }

                if (this.i1FState.sel == BitState.H && this.i1FState.rdy == BitState.L && uiBus1.getStb() == BitState.H)
                {
                    String binaryDB = Common.getUIBusRegisterValue(1, "DB");
                    Common.setA0(this.i1FState.a0, binaryDB);
                    this.i1FState.rdy = BitState.Z;
                    uiBus1.setIrdy(BitState.H);
                }
            }
        }
    }

    public I1FState getI1FState ()
    {
        return this.i1FState;
    }
}