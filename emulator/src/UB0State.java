public class UB0State
{
    private static UB0State instance;

    private BitState dir;
    private BitState ereq;
    private BitState ctl;
    private BitState stb;
    private BitState drdy;
    private BitState ireq;
    private UB0StateDA da;
    private UB0StateIOD iod;

    private Boolean setDirExecuted = false;
    private Boolean setEreqExecuted = false;
    private Boolean setCtlExecuted = false;
    private Boolean setStbExecuted = false;
    private Boolean setDrdyExecuted = false;
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

    public synchronized void setDrdy (BitState drdy)
    {
        this.setDrdyExecuted = true;
        this.drdy = drdy;
        this.setDrdyExecuted = false;
    }

    public BitState getDrdy ()
    {
        if (this.setDrdyExecuted) return BitState.U;

        return this.drdy;
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

        return this.da.getValue ();
    }

    public UB0StateDA getDABitState()
    {
        UB0StateDA da = new UB0StateDA (false);
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

    public synchronized void setIod (String binaryCode)
    {
        this.setIodExecuted = true;

        this.iod.setValue (binaryCode);

        this.setIodExecuted = false;
    }

    public String getIod ()
    {
        if (this.setIodExecuted) return null;

        return this.iod.getValue ();
    }

    public UB0StateIOD getIODBitState()
    {
        UB0StateIOD iod = new UB0StateIOD (false);
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

    public UB0State()
    {
        setDir (BitState.L);
        setEreq (BitState.L);
        setCtl (BitState.L);
        setStb (BitState.L);
        setDrdy (BitState.H);
        setIreq (BitState.H);
        this.da = new UB0StateDA (true);
        this.iod = new UB0StateIOD (true);
    }

    public static UB0State getInstance()
    {
        if (instance == null)
        {
            instance = new UB0State ();
        }

        return instance;
    }
}