public class I00StateExternalUI0
{

    public BitState dir;
    public BitState ereq;
    public BitState stb;
    public BitState irdy;
    public I00StateExternalUI0IS is;
    public I00StateExternalUI0IOD iod;

    public I00StateExternalUI0 (Boolean init)
    {
        if (init)
        {
            this.dir = BitState.L;
            this.ereq = BitState.L;
            this.stb = BitState.L;
            this.irdy = BitState.L;
            this.is = new I00StateExternalUI0IS(init);
            this.iod = new I00StateExternalUI0IOD(init);
        }
    }

    public I00StateExternalUI0 copy ()
    {
        I00StateExternalUI0 i00StateExternalUI0 = new I00StateExternalUI0(false);
        i00StateExternalUI0.dir = this.dir;
        i00StateExternalUI0.ereq = this.ereq;
        i00StateExternalUI0.stb = this.stb;
        i00StateExternalUI0.irdy = this.irdy;
        i00StateExternalUI0.is = this.is.copy ();
        i00StateExternalUI0.iod = this.iod.copy ();
        return i00StateExternalUI0;
    }
}
