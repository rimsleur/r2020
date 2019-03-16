public class I20
{
    public I20State i20State;
    public final String runCommand = "00100000";
    private Integer currentUIBusIndex = null;
    private String[] memoryArray = new String[256];
    private Integer currentCell = 0;

    public I20()
    {
        this.i20State = new I20State (true);

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

            if (runCommand.equals(isbUIbinary)) this.i20State.sel = BitState.H;
            else this.i20State.sel = BitState.L;

            if (this.currentUIBusIndex == 0)
            {
                if (this.i20State.sel == BitState.H)
                {
                    if (uiBus0.getEreq() == BitState.H)
                    {
                        if (uiBus0.getStb() == BitState.L)
                        {
                            if (uiBus0.getDir() == BitState.L)
                            {
                                if (this.i20State.rdy == BitState.Z)
                                {
                                    Common.setDB (0, Common.hexToBin(memoryArray[this.currentCell++]));
                                    if (this.currentCell == 256) this.currentCell = 0;
                                    this.i20State.rdy = BitState.L;
                                    uiBus0.setIrdy(BitState.L);
                                }
                            }
                            else if (uiBus0.getDir() == BitState.H)
                            {

                            }
                        }
                        else
                        {
                            this.i20State.rdy = BitState.Z;
                            uiBus0.setIrdy(BitState.H);
                        }
                    }
                }
                else
                {
                    this.i20State.rdy = BitState.Z;
                    uiBus0.setIrdy(BitState.H);
                }
            }
        }
    }

    public I20State getI20State()
    {
        return this.i20State;
    }

    public String[] getMemoryArray()
    {
        return this.memoryArray;
    }

    public Integer getCurrentCell()
    {
        return this.currentCell;
    }
}