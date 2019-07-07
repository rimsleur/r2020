public class RAM
{
    public RAMState ramState;
    public final String runCommand = "00100000";
    private Integer currentUIBusIndex = null;
    private String[] memoryArray = new String[256];
    private Integer currentCell = 0;

    public RAM ()
    {
        this.ramState = new RAMState (true);

        String[] codes = new String[] {"06", "1F", "01", "01", "02", "07", "01"};

        for(int i = 0; i < memoryArray.length; i++)
        {
            memoryArray[i] = "00";
        }

        for(int i = 0; i < codes.length; i++)
        {
            memoryArray[i] = codes[i];
        }
    }

    public void setUIBusIndex (Integer portNumb)
    {
        this.currentUIBusIndex = portNumb;
    }

    public void calculate (Integer times)
    {
        for (int i = 1; i <= times; i++)
        {
            String isbUIbinary = Common.getUIBusRegisterValue(this.currentUIBusIndex, "ISB");
            UB0State ub0State = UB0State.getInstance();

            if (runCommand.equals(isbUIbinary)) this.ramState.ds = BitState.H;
            else this.ramState.ds = BitState.L;

            if (this.currentUIBusIndex == 0)
            {
                if (this.ramState.ds == BitState.H)
                {
                    if (ub0State.getEreq() == BitState.H)
                    {
                        if (ub0State.getStb() == BitState.L)
                        {
                            if (ub0State.getDir() == BitState.L)
                            {
                                if (this.ramState.drdy == BitState.Z)
                                {
                                    Common.setDB (0, Common.hexToBin(memoryArray[this.currentCell++]));
                                    if (this.currentCell == 256) this.currentCell = 0;
                                    this.ramState.drdy = BitState.L;
                                    ub0State.setDrdy (BitState.L);
                                }
                            }
                            else if (ub0State.getDir() == BitState.H)
                            {

                            }
                        }
                        else
                        {
                            this.ramState.drdy = BitState.Z;
                            ub0State.setDrdy (BitState.H);
                        }
                    }
                }
                else
                {
                    this.ramState.drdy = BitState.Z;
                    ub0State.setDrdy (BitState.H);
                }
            }
        }
    }

    public RAMState getRAMState ()
    {
        return this.ramState;
    }

    public String[] getMemoryArray ()
    {
        return this.memoryArray;
    }

    public Integer getCurrentCell ()
    {
        return this.currentCell;
    }
}