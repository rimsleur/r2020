public class CPUStateExternalUI0
{

    public BitState dir;
    public BitState ereq;
    public BitState ctl;
    public BitState stb;
    public BitState rdy;
    public BitState ireq;
    public CPUStateExternalUI0DA da;
    public CPUStateExternalUI0IOD iod;

    public CPUStateExternalUI0 (Boolean init)
    {
        if (init)
        {
            this.dir = BitState.L;
            this.ereq = BitState.L;
            this.ctl = BitState.L;
            this.stb = BitState.L;
            this.rdy = BitState.L;
            this.ireq = BitState.L;
            this.da = new CPUStateExternalUI0DA (init);
            this.iod = new CPUStateExternalUI0IOD (init);
        }
    }

    public CPUStateExternalUI0 copy ()
    {
        CPUStateExternalUI0 ui0 = new CPUStateExternalUI0(false);
        ui0.dir = this.dir;
        ui0.ereq = this.ereq;
        ui0.ctl = this.ctl;
        ui0.stb = this.stb;
        ui0.rdy = this.rdy;
        ui0.ireq = this.ireq;
        ui0.da = this.da.copy ();
        ui0.iod = this.iod.copy ();
        return ui0;
    }
}
