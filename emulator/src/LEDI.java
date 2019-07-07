public class LEDI
{
    public LEDIState lediState;
    public final String runCommand = "00011111";
    private Integer currentUIBusIndex = null;

    public LEDI ()
    {
        this.lediState = new LEDIState (true);
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

            UB0State ub0State = UB0State.getInstance();
            UB1State ub1State = UB1State.getInstance();

            if (runCommand.equals(isbUIbinary)) this.lediState.ds = BitState.H;
            else this.lediState.ds = BitState.L;


            if (this.currentUIBusIndex == 0)
            {
                if (ub0State.getDir() == BitState.H && ub0State.getEreq() == BitState.H && ub0State.getStb() == BitState.L)
                {
                    if (this.lediState.drdy == BitState.Z)
                    {
                        this.lediState.drdy = BitState.L;
                        ub0State.setDrdy (BitState.L);
                    }
                }

                if (this.lediState.ds == BitState.H && this.lediState.drdy == BitState.L && ub0State.getStb() == BitState.H)
                {
                    String binaryDB = Common.getUIBusRegisterValue(0, "DB");
                    Common.setIOD (this.lediState.iod, binaryDB);
                    this.lediState.drdy = BitState.Z;
                    ub0State.setDrdy (BitState.H);
                }
            }

            if (this.currentUIBusIndex == 1)
            {
                if (ub1State.getDir() == BitState.H && ub1State.getEreq() == BitState.H && ub1State.getStb() == BitState.L)
                {
                    if (this.lediState.drdy == BitState.Z)
                    {
                        this.lediState.drdy = BitState.L;
                        ub1State.setDrdy (BitState.L);
                    }
                }

                if (this.lediState.ds == BitState.H && this.lediState.drdy == BitState.L && ub1State.getStb() == BitState.H)
                {
                    String binaryDB = Common.getUIBusRegisterValue(1, "DB");
                    Common.setIOD (this.lediState.iod, binaryDB);
                    this.lediState.drdy = BitState.Z;
                    ub1State.setDrdy (BitState.H);
                }
            }
        }
    }

    public LEDIState getLEDIState ()
    {
        return this.lediState;
    }
}