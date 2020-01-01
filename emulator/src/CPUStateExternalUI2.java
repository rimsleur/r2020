public class CPUStateExternalUI2
{
    public BitState dir;
    public BitState ereq;
    public BitState ctl;
    public BitState stb;
    public BitState rdy;
    public BitState ireq;
    public CPUStateExternalUI2DA da;
    public CPUStateExternalUI2IOD iod;


    public CPUStateExternalUI2 (Boolean init)
    {
        this.dir = BitState.L;
        this.ereq = BitState.L;
        this.ctl = BitState.L;
        this.stb = BitState.L;
        this.rdy = BitState.L;
        this.ireq = BitState.L;
        this.da = new CPUStateExternalUI2DA (true);
        this.iod = new CPUStateExternalUI2IOD (true);
    }

    public CPUStateExternalUI2 copy()
    {
        CPUStateExternalUI2 ui2 = new CPUStateExternalUI2 (false);
        ui2.dir = this.dir;
        ui2.ereq = this.ereq;
        ui2.ctl = this.ctl;
        ui2.stb = this.stb;
        ui2.rdy = this.rdy;
        ui2.ireq = this.ireq;
        ui2.da = this.da.copy ();
        ui2.iod = this.iod.copy ();

        return ui2;
    }
}
