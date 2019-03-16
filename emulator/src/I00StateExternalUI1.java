public class I00StateExternalUI1 {

    public BitState dir;
    public BitState ereq;
    public BitState stb;
    public BitState irdy;
    public I00StateExternalUI1IS is;
    public I00StateExternalUI1IOD iod;


    public I00StateExternalUI1 (Boolean init)
    {
        this.dir = BitState.L;
        this.ereq = BitState.L;
        this.stb = BitState.L;
        this.irdy = BitState.H;
        this.is = new I00StateExternalUI1IS(true);
        this.iod = new I00StateExternalUI1IOD(true);
    }

    public I00StateExternalUI1 copy()
    {
        I00StateExternalUI1 i00StateExternalUI1 = new I00StateExternalUI1(false);
        i00StateExternalUI1.dir = this.dir;
        i00StateExternalUI1.ereq = this.ereq;
        i00StateExternalUI1.stb = this.stb;
        i00StateExternalUI1.irdy = this.irdy;
        is = this.is.copy();
        iod = this.iod.copy();

        return i00StateExternalUI1;
    }
}
