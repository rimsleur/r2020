public class RAM
{
    public RAMState ramState;
    public final String deviceAddress = "00000001";
    private Integer currentUbIndex = null;
    private String[] memoryArray = new String[256];
    private Integer currentCell = 0;

    public RAM ()
    {
        this.ramState = new RAMState (true);

        //String[] codes = new String[] {"02", "01", "01", "01", "FF", "04", "01"};
        String[] codes = new String[] {"03", "01", "01", "01", "01", "05", "01", "01", "01", "6D", "05", "01", "06", "02"};

        for (int i = 0; i < memoryArray.length; i++)
        {
            memoryArray[i] = "00";
        }

        for (int i = 0; i < codes.length; i++)
        {
            memoryArray[i] = codes[i];
        }
    }

    public void setUbIndex (Integer ubIndex)
    {
        this.currentUbIndex = ubIndex;
    }

    public void calculate (Integer times)
    {
        for (int i = 1; i <= times; i++)
        {
            String ubDaValue = null;
            UB0State ub0State = null;
            UB1State ub1State = null;
            
            if (this.currentUbIndex == 0)
            {
                ub0State = UB0State.getInstance();
                ubDaValue = ub0State.getDa ();
            }
            else if (this.currentUbIndex == 1)
            {
                ub1State = UB1State.getInstance();
                ubDaValue = ub1State.getDa ();
            }

            if (this.deviceAddress.equals (ubDaValue)) this.ramState.ds = BitState.H;
            else this.ramState.ds = BitState.L;

            if (this.currentUbIndex == 0)
            {
                if (this.ramState.ds == BitState.H)
                {
                    if (ub0State.getEreq () == BitState.H)
                    {
                        if (ub0State.getDir () == BitState.L)
                        {
                            this.ramState.rdy = ub0State.getRdy ();
                            if (this.ramState.rdy == BitState.L)
                            {
                                if (this.ramState.stb == BitState.Z)
                                {
                                    String iodValue = Common.hexToBin (memoryArray[this.currentCell++]);
                                    this.ramState.iod.setValue (iodValue);
                                    ub0State.setIod (iodValue);
                                    if (this.currentCell == 256) this.currentCell = 0;
                                    this.ramState.stb = BitState.L;
                                    ub0State.setStb (BitState.L);
                                }
                            }
                            else
                            {
                                this.ramState.stb = BitState.Z;
                                ub0State.setStb (BitState.H);
                            }
                        }
                        else
                        {

                        }
                    }
                    else
                    {
                        if (ub0State.getDir () == BitState.L)
                        {
                            this.ramState.rdy = BitState.Z;
                            this.ramState.iod.setToState (BitState.Z);
                            ub0State.setIod ("11111111");
                        }
                        else
                        {

                        }
                    }
                }
                else
                {
                    if (ub0State.getDir () == BitState.L)
                    {
                        this.ramState.stb = BitState.Z;
                        ub0State.setStb (BitState.H);
                    }
                    else
                    {

                    }
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