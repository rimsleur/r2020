public class UB2State
{
    private static UB2State instance;

    private BitState dir;
    private BitState ereq;
    private BitState ctl;
    private BitState stb;
    private BitState rdy;
    private BitState ireq;
    private UB2StateDA da;
    private UB2StateIOD iod;

    private Serial serial;
    private boolean adapterInitialized = false;
    private boolean iodSetMode = false;

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
        if (dir == BitState.L) this.serial.write (30);
        else if (dir == BitState.H) this.serial.write (31);
    }

    public BitState getDir ()
    {
        while (this.setDirExecuted);
        return this.dir;
    }

    public synchronized void setEreq (BitState ereq)
    {
        this.setEreqExecuted = true;
        this.ereq = ereq;
        this.setEreqExecuted = false;
        if (ereq == BitState.L) this.serial.write (40);
        else if (ereq == BitState.H) this.serial.write (41);
    }

    public BitState getEreq ()
    {
        while (this.setEreqExecuted);
        return this.ereq;
    }

    public synchronized void setCtl (BitState ctl)
    {
        this.setCtlExecuted = true;
        this.ctl = ctl;
        this.setCtlExecuted = false;
        if (ctl == BitState.L) this.serial.write (70);
        else if (ctl == BitState.H) this.serial.write (71);
    }

    public BitState getCtl ()
    {
        while (this.setCtlExecuted);
        return this.ctl;
    }

    private synchronized void setStb (BitState stb, boolean send)
    {
        this.setStbExecuted = true;
        this.stb = stb;
        this.setStbExecuted = false;

        if (send)
        {
            if (stb == BitState.L) this.serial.write (50);
            else if (stb == BitState.H) this.serial.write (51);
        }
    }

    public void setStb (BitState stb)
    {
        setStb (stb, true);
    }

    public BitState getStb ()
    {
        while (this.setStbExecuted);
        return this.stb;
    }

    private synchronized void setRdy (BitState rdy, boolean send)
    {
        this.setRdyExecuted = true;
        this.rdy = rdy;
        this.setRdyExecuted = false;

        if (send)
        {
            if (rdy == BitState.L) this.serial.write (60);
            else if (rdy == BitState.H) this.serial.write (61);
        }
    }

    public void setRdy (BitState rdy)
    {
        setRdy (rdy, true);
    }

    public BitState getRdy ()
    {
        while (this.setRdyExecuted);
        return this.rdy;
    }

    public synchronized void setIreq (BitState ireq)
    {
        this.setIreqExecuted = true;
        this.ireq = ireq;
        this.setIreqExecuted = false;
        if (ireq == BitState.L) this.serial.write (80);
        else if (ireq == BitState.H) this.serial.write (81);
    }

    public BitState getIreq ()
    {
        while (this.setIreqExecuted);
        return this.ireq;
    }

    public synchronized void setDa (String binaryCode)
    {
        this.setDaExecuted = true;
        this.da.setValue (binaryCode);
        this.setDaExecuted = false;
        this.serial.write (2);
        this.serial.write (Integer.parseInt (binaryCode, 2));
    }

    public String getDa ()
    {
        while (this.setDaExecuted);
        return this.da.getValue();
    }

    private synchronized void setIod (String binaryCode, boolean send)
    {
        this.setIodExecuted = true;
        this.iod.setValue (binaryCode);
        this.setIodExecuted = false;

        if (send)
        {
            this.serial.write (1);
            this.serial.write (Integer.parseInt (binaryCode, 2));
        }
    }

    public void setIod (String binaryCode)
    {
        setIod (binaryCode, true);
    }

    public String getIod ()
    {
        while (this.setIodExecuted);
        return this.iod.getValue ();
    }

    public UB2StateDA getDABitState ()
    {
        UB2StateDA da = new UB2StateDA (false);
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

    public UB2StateIOD getIODBitState ()
    {
        UB2StateIOD iod = new UB2StateIOD (false);
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

    public void serialReadCallback (int i)
    {
        //System.out.println ("adapterInitialized = " + isAdapterInitialized ());
        if (!isAdapterInitialized ()) 
        {
            if (i == 160) setAdapterInitialized ();
            return;
        }

        if (!this.iodSetMode)
        {
            switch (i)
            {
                case 1:
                    this.iodSetMode = true;
                    break;
                case 50:
                    setStb (BitState.L, false);
                    break;
                case 51:
                    setStb (BitState.H, false);
                    break;
                case 60:
                    setRdy (BitState.L, false);
                    break;
                case 61:
                    setRdy (BitState.H, false);
                    break;
                case 80:
                    setIreq (BitState.L);
                    break;
                case 81:
                    setIreq (BitState.H);
                    break;
            }
        }
        else
        {
            String s = Integer.toBinaryString (i);
            int n = 8 - s.length();
            String s1 = "";
            for (int y = n; y > 0; y--)
            {
                s1 = s1 + "0";
            }
            s = s1 + s;
            setIod (s, false);
            this.iodSetMode = false;
        }

        //System.out.println ("serialReadCallback " + i);
    }

    private synchronized void setAdapterInitialized ()
    {
        this.adapterInitialized = true;
    }

    private boolean isAdapterInitialized ()
    {
        //System.out.println (this.adapterInitialized);
        return this.adapterInitialized;
    }

    public UB2State ()
    {
        this.da = new UB2StateDA (true);
        this.iod = new UB2StateIOD (true);

        this.serial = new Serial (this);
        this.serial.write (160); // initialize adapter
        //while (!isAdapterInitialized ()); //wait for response ???? не работает сука
        try
        {
            Thread.sleep (1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace ();
        }

        setDir (BitState.L);
        setEreq (BitState.L);
        setCtl (BitState.L);
    }

    public static UB2State getInstance ()
    {
        if (instance == null)
        {
            instance = new UB2State ();
        }

        return instance;
    }
}