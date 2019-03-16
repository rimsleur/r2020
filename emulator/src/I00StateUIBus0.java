public class I00StateUIBus0
{
    private static I00StateUIBus0 instance;

    private BitState dir;
    private BitState ereq;
    private BitState stb;
    private BitState irdy;
    private I00StateUIBus0ISB isb;
    private I00StateUIBus0DB db;

    private Boolean setDirExecuted = false;
    private Boolean setEreqExecuted = false;
    private Boolean setStbExecuted = false;
    private Boolean setIrdyExecuted = false;
    private Boolean setIsbExecuted = false;
    private Boolean setDbExecuted = false;

    public synchronized void setDir(BitState dir)
    {
        this.setDirExecuted = true;
        this.dir = dir;
        this.setDirExecuted = false;
    }

    public BitState getDir()
    {
        if (this.setDirExecuted) return BitState.U;

        return this.dir;
    }

    public synchronized void setEreq(BitState ereq)
    {
        this.setEreqExecuted = true;
        this.ereq = ereq;
        this.setEreqExecuted = false;
    }

    public BitState getEreq()
    {
        if (this.setEreqExecuted) return BitState.U;

        return this.ereq;
    }

    public synchronized void setStb(BitState stb)
    {
        this.setStbExecuted = true;
        this.stb = stb;
        this.setStbExecuted = false;
    }

    public BitState getStb()
    {
        if (this.setStbExecuted) return BitState.U;
        return this.stb;
    }

    public synchronized void setIrdy(BitState irdy)
    {
        this.setIrdyExecuted = true;
        this.irdy = irdy;
        this.setIrdyExecuted = false;
    }

    public BitState getIrdy()
    {
        if(this.setIrdyExecuted) return BitState.U;

        return this.irdy;
    }

    public synchronized void setIsb(String binaryCode)
    {
        this.setIsbExecuted = true;

        this.isb.setValue(binaryCode);

        this.setIsbExecuted = false;
    }

    public String getIsb()
    {

        if (this.setIsbExecuted) return null;

        return this.isb.getValue();
    }

    public I00StateUIBus0ISB getIsbBitState()
    {
        I00StateUIBus0ISB isb = new I00StateUIBus0ISB(false);
        isb.b7 = this.isb.b7;
        isb.b6 = this.isb.b6;
        isb.b5 = this.isb.b5;
        isb.b4 = this.isb.b4;
        isb.b3 = this.isb.b3;
        isb.b2 = this.isb.b2;
        isb.b1 = this.isb.b1;
        isb.b0 = this.isb.b0;
        return isb;
    }

    public synchronized void setDb(String binaryCode)
    {
        this.setDbExecuted = true;

        this.db.setValue(binaryCode);

        this.setDbExecuted = false;
    }

    public String getDb()
    {
        if (this.setDbExecuted) return null;

        return this.db.getValue();
    }

    public I00StateUIBus0DB getDbBitState()
    {
        I00StateUIBus0DB db = new I00StateUIBus0DB(false);
        db.b7 = this.db.b7;
        db.b6 = this.db.b6;
        db.b5 = this.db.b5;
        db.b4 = this.db.b4;
        db.b3 = this.db.b3;
        db.b2 = this.db.b2;
        db.b1 = this.db.b1;
        db.b0 = this.db.b0;
        return db;
    }

    public I00StateUIBus0()
    {
        setDir(BitState.L);
        setEreq(BitState.L);
        setStb(BitState.L);
        setIrdy(BitState.H);
        this.isb = new I00StateUIBus0ISB(true);
        this.db = new I00StateUIBus0DB(true);
    }

    public static I00StateUIBus0 getInstance()
    {

        if (instance == null)
        {
            instance = new I00StateUIBus0();
        }

        return instance;
    }

    //Думаю что для этого класса копию не нужно реализовывать
//    public I00StateUIBus0 copy()
//    {
//        I00StateUIBus0 i00StateSystemBus = new I00StateUIBus0(false);
//        i00StateSystemBus.dir = this.dir;
//        i00StateSystemBus.ereq = this.ereq;
//        i00StateSystemBus.stb = this.stb;
//        i00StateSystemBus.irdy = this.irdy;
//        i00StateSystemBus.isb = this.isb.copy();
//        i00StateSystemBus.db = this.db.copy();
//        return i00StateSystemBus;
//    }
}