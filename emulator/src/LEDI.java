public class LEDI
{
    public LEDIState lediState;
    public final String deviceAddress = "00000001";
    private Integer currentUbIndex = null;
    private boolean rdyFired = false;

    public LEDI ()
    {
        this.lediState = new LEDIState (true);
    }

    public void setUbIndex(Integer ubIndex)
    {
        this.currentUbIndex = ubIndex;
    }

    public void calculate (Integer times)
    {
        for (int i = 1; i <= times; i++)
        {
            String ubDaValue = null;
            UB1State ub1State = null;
            UB2State ub2State = null;
            
            if (this.currentUbIndex == 1)
            {
                ub1State = UB1State.getInstance();
                ubDaValue = ub1State.getDa ();
            }
            else if (this.currentUbIndex == 2)
            {
                ub2State = UB2State.getInstance();
                ubDaValue = ub2State.getDa ();
            }

            if (this.deviceAddress.equals (ubDaValue)) this.lediState.ds = BitState.H;
            else this.lediState.ds = BitState.L;

            if (this.currentUbIndex == 1)
            {
                if (this.lediState.ds == BitState.H)
                {
                    if (ub1State.getEreq () == BitState.H)
                    {
                        if (ub1State.getDir () == BitState.L)
                        {
                            this.lediState.rdy = ub1State.getRdy ();
                            if (this.lediState.rdy == BitState.L)
                            {
                                if (this.lediState.stb == BitState.Z)
                                {
                                    //String iodValue = Common.hexToBin (memoryArray[this.currentCell++]);
                                    //this.ramState.iod.setValue (iodValue);
                                    //ub0State.setIod (iodValue);
                                    //if (this.currentCell == 256) this.currentCell = 0;
                                    this.lediState.stb = BitState.L;
                                    ub1State.setStb (BitState.L);
                                }
                            }
                            else
                            {
                                this.lediState.stb = BitState.Z;
                                ub1State.setStb (BitState.H);
                            }
                        }
                        else
                        {
                            if (this.lediState.rdy == BitState.Z && !rdyFired)
                            {
                                this.lediState.rdy = BitState.L;
                                ub1State.setRdy (BitState.L);
                                rdyFired = true;
                            }

                            this.lediState.stb = ub1State.getStb ();
                            if (this.lediState.stb == BitState.L)
                            {
                                String iodValue = ub1State.getIod ();
                                this.lediState.iod.setValue (iodValue);
                                this.lediState.digit1 = formDigit1 (iodValue);
                                this.lediState.digit2 = formDigit2 (iodValue);

                                if (this.lediState.rdy == BitState.L)
                                {
                                    this.lediState.rdy = BitState.Z;
                                    ub1State.setRdy (BitState.H);
                                }
                            }
                            /*
                            else
                            {
                                this.lediState.stb = BitState.Z;
                                ub1State.setStb (BitState.H);
                            }
                            */
                        }
                    }
                    else
                    {
                        if (ub1State.getDir () == BitState.L)
                        {
                            this.lediState.stb = BitState.Z;
                            this.lediState.rdy = BitState.Z;
                            this.lediState.iod.setToState (BitState.Z);
                            ub1State.setIod ("11111111");
                            rdyFired = false;
                        }
                    }
                }
                else
                {
                    this.lediState.stb = BitState.Z;
                    ub1State.setStb (BitState.H);
                    this.lediState.rdy = BitState.Z;
                    ub1State.setRdy (BitState.H);
                }
            }
            else if (this.currentUbIndex == 2)
            {
                if (this.lediState.ds == BitState.H)
                {
                    if (ub2State.getEreq () == BitState.H)
                    {
                        if (ub2State.getDir () == BitState.L)
                        {
                            this.lediState.rdy = ub2State.getRdy ();
                            if (this.lediState.rdy == BitState.L)
                            {
                                if (this.lediState.stb == BitState.Z)
                                {
                                    //String iodValue = Common.hexToBin (memoryArray[this.currentCell++]);
                                    //this.ramState.iod.setValue (iodValue);
                                    //ub0State.setIod (iodValue);
                                    //if (this.currentCell == 256) this.currentCell = 0;
                                    this.lediState.stb = BitState.L;
                                    ub2State.setStb (BitState.L);
                                }
                            }
                            else
                            {
                                this.lediState.stb = BitState.Z;
                                ub2State.setStb (BitState.H);
                            }
                        }
                        else
                        {
                            if (this.lediState.rdy == BitState.Z && !rdyFired)
                            {
                                this.lediState.rdy = BitState.L;
                                ub2State.setRdy (BitState.L);
                                rdyFired = true;
                            }

                            this.lediState.stb = ub2State.getStb ();
                            if (this.lediState.stb == BitState.L)
                            {
                                String iodValue = ub2State.getIod ();
                                this.lediState.iod.setValue (iodValue);
                                this.lediState.digit1 = formDigit1 (iodValue);
                                this.lediState.digit2 = formDigit2 (iodValue);

                                if (this.lediState.rdy == BitState.L)
                                {
                                    this.lediState.rdy = BitState.Z;
                                    ub2State.setRdy (BitState.H);
                                }
                            }
                            /*
                            else
                            {
                                this.lediState.stb = BitState.Z;
                                ub1State.setStb (BitState.H);
                            }
                            */
                        }
                    }
                    else
                    {
                        if (ub2State.getDir () == BitState.L)
                        {
                            this.lediState.stb = BitState.Z;
                            this.lediState.rdy = BitState.Z;
                            this.lediState.iod.setToState (BitState.Z);
                            ub2State.setIod ("11111111");
                            rdyFired = false;
                        }
                    }
                }
                else
                {
                    this.lediState.stb = BitState.Z;
                    ub2State.setStb (BitState.H);
                    this.lediState.rdy = BitState.Z;
                    ub2State.setRdy (BitState.H);
                }
            }


        }
    }

    private String formDigit1 (String value)
    {
        Integer d = 0;
        String digit = " ";

        for (int i = 3; i >= 0; i--)
        {
            String s = value.substring (i, i+1);

            if (i == 3)
            {
                if (s.equals ("1")) d += 1;
            }
            if (i == 2)
            {
                if (s.equals ("1")) d += 2;
            }
            if (i == 1)
            {
                if (s.equals ("1")) d += 4;
            }
            if (i == 0)
            {
                if (s.equals ("1")) d += 8;
            }
        }

        digit = d.toString ();

        if (d == 10) digit = "A";
        else if (d == 11) digit = "B";
        else if (d == 12) digit = "C";
        else if (d == 13) digit = "D";
        else if (d == 14) digit = "E";
        else if (d == 15) digit = "F";

        return digit;
    }

    private String formDigit2 (String value)
    {
        Integer d = 0;
        String digit = " ";

        for (int i = 7; i > 3; i--)
        {
            String s = value.substring (i, i+1);

            if (i == 7)
            {
                if (s.equals ("1")) d += 1;
            }
            if (i == 6)
            {
                if (s.equals ("1")) d += 2;
            }
            if (i == 5)
            {
                if (s.equals ("1")) d += 4;
            }
            if (i == 4)
            {
                if (s.equals ("1")) d += 8;
            }
        }

        digit = d.toString ();

        if (d == 10) digit = "A";
        else if (d == 11) digit = "B";
        else if (d == 12) digit = "C";
        else if (d == 13) digit = "D";
        else if (d == 14) digit = "E";
        else if (d == 15) digit = "F";

        return digit;
    }

    public LEDIState getLEDIState ()
    {
        return this.lediState;
    }
}