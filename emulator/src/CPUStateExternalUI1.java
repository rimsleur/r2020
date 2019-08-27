public class CPUStateExternalUI1
{
    public BitState dir;
    public BitState ereq;
    public BitState ctl;
    public BitState stb;
    public BitState rdy;
    public BitState ireq;
    public CPUStateExternalUI1DA da;
    public CPUStateExternalUI1IOD iod;


    public CPUStateExternalUI1 (Boolean init)
    {
        this.dir = BitState.L;
        this.ereq = BitState.L;
        this.ctl = BitState.L;
        this.stb = BitState.L;
        this.rdy = BitState.L;
        this.ireq = BitState.L;
        this.da = new CPUStateExternalUI1DA (true);
        this.iod = new CPUStateExternalUI1IOD (true);
    }

    public CPUStateExternalUI1 copy()
    {
        CPUStateExternalUI1 ui1 = new CPUStateExternalUI1 (false);
        ui1.dir = this.dir;
        ui1.ereq = this.ereq;
        ui1.ctl = this.ctl;
        ui1.stb = this.stb;
        ui1.rdy = this.rdy;
        ui1.ireq = this.ireq;
        ui1.da = this.da.copy ();
        ui1.iod = this.iod.copy ();

        return ui1;
    }
}
