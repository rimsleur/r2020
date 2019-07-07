public class CPUStateExternalUI0
{

    public BitState dir;
    public BitState ereq;
    public BitState stb;
    public BitState drdy;
    public CPUStateExternalUI0DA da;
    public CPUStateExternalUI0IOD iod;

    public CPUStateExternalUI0 (Boolean init)
    {
        if (init)
        {
            this.dir = BitState.L;
            this.ereq = BitState.L;
            this.stb = BitState.L;
            this.drdy = BitState.L;
            this.da = new CPUStateExternalUI0DA (init);
            this.iod = new CPUStateExternalUI0IOD (init);
        }
    }

    public CPUStateExternalUI0 copy ()
    {
        CPUStateExternalUI0 ui0 = new CPUStateExternalUI0(false);
        ui0.dir = this.dir;
        ui0.ereq = this.ereq;
        ui0.stb = this.stb;
        ui0.drdy = this.drdy;
        ui0.da = this.da.copy ();
        ui0.iod = this.iod.copy ();
        return ui0;
    }
}
