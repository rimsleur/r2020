public class UB1State
{
    private static UB1State instance;

    private BitState dir;
    private BitState ereq;
    private BitState ctl;
    private BitState stb;
    private BitState rdy;
    private BitState ireq;
    private UB1StateDA da;
    private UB1StateIOD iod;

    private Boolean setDirExecuted = false;
    private Boolean setEreqExecuted = false;
    private Boolean setCtlExecuted = false;
    private Boolean setStbExecuted = false;
    private Boolean setRdyExecuted = false;
    private Boolean setIreqExecuted = false;
    private Boolean setDaExecuted = false;
    private Boolean setIodExecuted = false;

    public synchronized void setDir (BitState dir)
    {
        this.setDirExecuted = true;
        this.dir = dir;
        this.setDirExecuted = false;
    }

    public BitState getDir ()
    {
        if (this.setDirExecuted) return BitState.U;

        return this.dir;
    }

    public synchronized void setEreq (BitState ereq)
    {
        this.setEreqExecuted = true;
        this.ereq = ereq;
        this.setEreqExecuted = false;
    }

    public BitState getEreq ()
    {
        if (this.setEreqExecuted) return BitState.U;

        return this.ereq;
    }

    public synchronized void setCtl (BitState ctl)
    {
        this.setCtlExecuted = true;
        this.ctl = ctl;
        this.setCtlExecuted = false;
    }

    public BitState getCtl ()
    {
        if (this.setCtlExecuted) return BitState.U;
        return this.ctl;
    }

    public synchronized void setStb (BitState stb)
    {
        this.setStbExecuted = true;
        this.stb = stb;
        this.setStbExecuted = false;
    }

    public BitState getStb ()
    {
        if (this.setStbExecuted) return BitState.U;
        return this.stb;
    }

    public synchronized void setRdy (BitState rdy)
    {
        this.setRdyExecuted = true;
        this.rdy = rdy;
        this.setRdyExecuted = false;
    }

    public BitState getRdy ()
    {
        if (this.setRdyExecuted) return BitState.U;

        return this.rdy;
    }

    public synchronized void setIreq (BitState ireq)
    {
        this.setIreqExecuted = true;
        this.ireq = ireq;
        this.setIreqExecuted = false;
    }

    public BitState getIreq ()
    {
        if (this.setIreqExecuted) return BitState.U;

        return this.ireq;
    }

    public synchronized void setDa (String binaryCode)
    {
        this.setDaExecuted = true;

        this.da.setValue (binaryCode);

        this.setDaExecuted = false;
    }

    public String getDa ()
    {
        if (this.setDaExecuted) return null;

        return this.da.getValue();
    }

    public synchronized void setIod (String binaryCode)
    {
        this.setIodExecuted = true;
        this.iod.setValue (binaryCode);
//        this.iod.b7 = iod.b7;
//        this.iod.b6 = iod.b6;
//        this.iod.b5 = iod.b5;
//        this.iod.b4 = iod.b4;
//        this.iod.b3 = iod.b3;
//        this.iod.b2 = iod.b2;
//        this.iod.b1 = iod.b1;
//        this.iod.b0 = iod.b0;
        this.setIodExecuted = false;
    }

    public String getIod ()
    {
        while (this.setIodExecuted);
        return this.iod.getValue ();
    }

    public UB1StateDA getDABitState ()
    {
        UB1StateDA da = new UB1StateDA (false);
        da.b7 = this.da.b7;
        da.b6 = this.da.b6;
        da.b5 = this.da.b5;
        da.b4 = this.da.b4;
        da.b3 = this.da.b3;
        da.b2 = this.da.b2;
        da.b1 = this.da.b1;
        da.b0 = this.da.b0;
        return da;
    }

    public UB1StateIOD getIODBitState ()
    {
        UB1StateIOD iod = new UB1StateIOD (false);
        iod.b7 = this.iod.b7;
        iod.b6 = this.iod.b6;
        iod.b5 = this.iod.b5;
        iod.b4 = this.iod.b4;
        iod.b3 = this.iod.b3;
        iod.b2 = this.iod.b2;
        iod.b1 = this.iod.b1;
        iod.b0 = this.iod.b0;
        return iod;
    }

    public UB1State ()
    {
        setDir (BitState.L);
        setEreq (BitState.L);
        setCtl (BitState.L);
        setStb (BitState.L);
        setRdy (BitState.H);
        setIreq (BitState.H);
        this.da = new UB1StateDA (true);
        this.iod = new UB1StateIOD (true);
    }

    public static UB1State getInstance ()
    {
        if (instance == null)
        {
            instance = new UB1State ();
        }

        return instance;
    }
}