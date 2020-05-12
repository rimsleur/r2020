public class CPU
{
    public CPUState cpuState;

    private CPUState cpuStatePrev;

    private String commandPrev; //переменная для записи прошлой комманды

    private Integer iteratorCommand; //переменная для итерации команд

    private String statusText;

    private CommandStep step;
    private CommandCycle cycle;

    private String cmd; //первый параметр
    private String arg0; //второй параметр
    private String arg1; //третий параметр

    private String binaryCMDR; //двоичный код первого параметра
    private String binaryARG0R; //двоичный код второго параметра
    private String binaryARG1R; //двоичный код третьего параметра

    private String[] commandList = new String[]{"SELI1 0x1F", "LDI R1, 0x02", "OUT1 R1"}; //Массив, в котором хранятся все команды

    public CPU()
    {
        this.cpuState = new CPUState(true);
        this.cpuStatePrev = this.cpuState.copy();
        this.iteratorCommand = 0;
        this.commandPrev = null;

        this.cmd = "";
        this.arg0 = "";
        this.arg1 = "";
        this.binaryCMDR = "";
        this.binaryARG0R = "";
        this.binaryARG1R = "";

        this.step = CommandStep.STEP1;
        this.cycle = CommandCycle.CYCLE1;

        this.cpuState.external.ui0.dir = BitState.L;
        this.cpuState.external.ui0.ereq = BitState.L;
        this.cpuState.external.ui0.ctl = BitState.L;
        this.cpuState.external.ui0.stb = BitState.H;
        this.cpuState.external.ui0.rdy = BitState.H;
        this.cpuState.external.ui0.ireq = BitState.H;
        this.cpuState.external.ui0.iod.setToState (BitState.H);

        UB0State ub0State = UB0State.getInstance ();
        ub0State.setDir (this.cpuState.external.ui0.dir);
        ub0State.setEreq (this.cpuState.external.ui0.ereq);
        ub0State.setCtl (this.cpuState.external.ui0.ctl);
        ub0State.setStb (this.cpuState.external.ui0.stb);
        ub0State.setRdy (this.cpuState.external.ui0.rdy);
        ub0State.setIreq (this.cpuState.external.ui0.ireq);
        ub0State.setIod (this.cpuState.external.ui0.iod.getValue ());

        this.cpuState.external.ui1.dir = BitState.L;
        this.cpuState.external.ui1.ereq = BitState.L;
        this.cpuState.external.ui1.ctl = BitState.L;
        this.cpuState.external.ui1.stb = BitState.H;
        this.cpuState.external.ui1.rdy = BitState.H;
        this.cpuState.external.ui1.ireq = BitState.H;
        this.cpuState.external.ui1.iod.setToState (BitState.H);

        UB1State ub1State = UB1State.getInstance ();
        ub1State.setDir (this.cpuState.external.ui1.dir);
        ub1State.setEreq (this.cpuState.external.ui1.ereq);
        ub1State.setCtl (this.cpuState.external.ui1.ctl);
        ub1State.setStb (this.cpuState.external.ui1.stb);
        ub1State.setRdy (this.cpuState.external.ui1.rdy);
        ub1State.setIreq (this.cpuState.external.ui1.ireq);
        ub1State.setIod (this.cpuState.external.ui1.iod.getValue ());
/*
        this.cpuState.external.ui2.dir = BitState.L;
        this.cpuState.external.ui2.ereq = BitState.L;
        this.cpuState.external.ui2.ctl = BitState.L;
        this.cpuState.external.ui2.stb = BitState.H;
        this.cpuState.external.ui2.rdy = BitState.H;
        this.cpuState.external.ui2.ireq = BitState.H;
        this.cpuState.external.ui2.iod.setToState (BitState.H);

        
        ub2State.setDir (this.cpuState.external.ui2.dir);
        ub2State.setEreq (this.cpuState.external.ui2.ereq);
        ub2State.setCtl (this.cpuState.external.ui2.ctl);
        ub2State.setStb (this.cpuState.external.ui2.stb);
        ub2State.setRdy (this.cpuState.external.ui2.rdy);
        ub2State.setIreq (this.cpuState.external.ui2.ireq);
        ub2State.setIod (this.cpuState.external.ui2.iod.getValue ());
*/
        this.cpuState.external.ui2.dir = BitState.L;
        this.cpuState.external.ui2.ereq = BitState.L;
        this.cpuState.external.ui2.ctl = BitState.L;

        UB2State ub2State = UB2State.getInstance ();
        this.cpuState.external.ui2.stb = ub2State.getStb ();
        this.cpuState.external.ui2.rdy = ub2State.getRdy ();
        this.cpuState.external.ui2.ireq = ub2State.getIreq ();
        this.cpuState.external.ui2.iod.setValue (ub2State.getIod ());
    }

    public void calculate (Integer times)
    {
        for (int i = 1; i <= times; i++)
        {
            this.cpuState.external.clk = Common.invertPinState (this.cpuState.external.clk);

            UB0State ub0State = UB0State.getInstance ();
            UB1State ub1State = UB1State.getInstance ();
            UB2State ub2State = UB2State.getInstance ();

            this.cpuState.external.ui0.ireq = ub0State.getIreq ();
            this.cpuState.external.ui1.ireq = ub1State.getIreq ();
            this.cpuState.external.ui2.ireq = ub2State.getIreq ();

            if (this.cpuState.external.ui0.dir == BitState.L)
            {
                this.cpuState.external.ui0.stb = ub0State.getStb ();
                this.cpuState.external.ui0.iod.setValue (ub0State.getIod ());
            }
            else
            {
                this.cpuState.external.ui0.rdy = ub0State.getRdy ();
            }

            if (this.cpuState.external.ui1.dir == BitState.L)
            {
                this.cpuState.external.ui1.stb = ub1State.getStb ();
                this.cpuState.external.ui1.iod.setValue (ub1State.getIod ());
            }
            else
            {
                this.cpuState.external.ui1.rdy = ub1State.getRdy ();
            }

            if (this.cpuState.external.ui2.dir == BitState.L)
            {
                this.cpuState.external.ui2.stb = ub2State.getStb ();
                this.cpuState.external.ui2.iod.setValue (ub2State.getIod ());
            }
            else
            {
                this.cpuState.external.ui2.rdy = ub2State.getRdy ();
            }

            if (this.cpuState.internal.ctlr1.ui0md == BitState.H)
            {
                if (iteratorCommand >= commandList.length)
                {
                    iteratorCommand = 0;
                }

                if (this.cpuState.external.clk == BitState.H)
                {
                    if (this.cpuState.internal.ctlr1.cmdrun == BitState.L && this.cpuState.internal.ctlr1.cmdrdy == BitState.L)
                    {
                        String[] options = Common.getOptions(commandList[iteratorCommand++]);

                        cmd = options[0];
                        if (options.length > 1) arg0 = options[1];
                        else arg0 = "";
                        if (options.length > 2) arg1 = options[2];
                        else arg1 = "";

                        binaryCMDR = Common.getCommandToBinary(cmd);
                        Common.setRegisterValue(this.cpuState.internal, "CMDR", binaryCMDR);

                        this.step = CommandStep.STEP1;

                        if (!arg0.equals(""))
                        {
                            if (arg0.substring(0, 2).equals("0x"))
                                binaryARG0R = Common.hexToBin(arg0); //16-й первого параметра в 2-й
                            else
                                binaryARG0R = Common.getRegisterToBinary(arg0); //16-й первого параметра в 2-й
                            Common.setRegisterValue(this.cpuState.internal, "ARG0R", binaryARG0R);
                        } else Common.clearRegister(this.cpuState.internal, "ARG0R");

                        if (!arg1.equals(""))
                        {
                            if (arg1.substring(0, 2).equals("0x"))
                                binaryARG1R = Common.hexToBin(arg1);
                            else
                                binaryARG1R = Common.getRegisterToBinary(arg1);
                            Common.setRegisterValue(this.cpuState.internal, "ARG1R", binaryARG1R);
                        } else Common.clearRegister(this.cpuState.internal, "ARG1R");

                        setStatusText(cmd, arg0, arg1);
                        this.cpuState.internal.ctlr1.cmdrdy = BitState.H;
                        this.commandPrev = cmd;
                    }
                    else
                    {
                        cmd = this.commandPrev;
                    }
                }
                else
                {
                    this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                    cmd = this.commandPrev;
                }
            }
            else
            {
                if (this.cpuState.internal.ctlr1.cmdrun == BitState.L && this.cpuState.internal.ctlr1.cmdrdy == BitState.L)
                {
                    switch (this.step)
                    {
                        case STEP1:
                            if (this.cpuState.external.clk == BitState.H && this.cpuState.external.ui0.stb == BitState.H)
                            {
                                if (this.cycle == CommandCycle.CYCLE1)
                                {
                                    this.cpuState.internal.cmdr.setValue ("00000000");
                                    this.cpuState.internal.arg0r.setValue ("00000000");
                                    this.cpuState.internal.arg1r.setValue ("00000000");
                                    this.cpuState.external.ui0.da.setValue ("00000001");
                                }
                                this.cpuState.external.ui0.ereq = BitState.H;
                                this.cpuState.external.ui0.rdy = BitState.L;
                                this.step = CommandStep.STEP2;
                            }
                            break;

                        case STEP2:
                            if (this.cpuState.external.ui0.stb == BitState.L)
                            {
                                String value = this.cpuState.external.ui0.iod.getValue ();
                                if (this.cycle == CommandCycle.CYCLE1) this.cpuState.internal.cmdr.setValue (value);
                                else if (this.cycle == CommandCycle.CYCLE2) this.cpuState.internal.arg0r.setValue (value);
                                else if (this.cycle == CommandCycle.CYCLE3) this.cpuState.internal.arg1r.setValue (value);

                                this.cpuState.external.ui0.rdy = BitState.H;
                                this.step = CommandStep.STEP3;
                            }
                            break;

                        case STEP3:
                            if (this.cpuState.external.ui0.stb == BitState.H)
                            {
                                this.cpuState.external.ui0.ereq = BitState.L;
                                this.step = CommandStep.STEP1;


                                if (this.cycle == Common.isLastCycle (this.cpuState.internal.cmdr.getValue()))
                                {
                                    this.cpuState.internal.ctlr1.cmdrdy = BitState.H;
                                    this.cycle = CommandCycle.CYCLE1;
                                }
                                else
                                {
                                    if (this.cycle == CommandCycle.CYCLE1) this.cycle = CommandCycle.CYCLE2;
                                    else if (this.cycle == CommandCycle.CYCLE2) this.cycle = CommandCycle.CYCLE3;
                                }
                            }
                            break;
/*
                        case STEP4:
                            if (this.cpuState.external.ui0.stb == BitState.L)
                            {
                                //String binaryUI0iod = Common.getUIExternalRegisterValue(this.cpuState.external, 0, "IOD");
                                //Common.setRegisterValue(this.cpuState.internal, "ARG0R", binaryUI0iod);
                                this.cpuState.internal.arg0r.setValue (this.cpuState.external.ui0.iod.getValue ());

                                if (this.step == Common.isLastStep (this.cpuState.internal.cmdr.getValue()))
                                {
                                    //Common.setRegisterValue(this.cpuState.internal, "ARG1R", "00000000");
                                    this.cpuState.internal.arg1r.setValue ("00000000");
                                    this.cpuState.internal.ctlr1.cmdrdy = BitState.H;
                                    this.step = CommandStep.STEP1;
                                }
                                else
                                {
                                    this.step = CommandStep.STEP5;
                                }
                            }
                            break;

                        case STEP5:
                            if (this.cpuState.external.ui0.rdy == BitState.H)
                            {
                                //Common.setUIFlagExternal(this.cpuState.external, 0, "EREQ", BitState.H);
                                this.cpuState.external.ui0.ereq = BitState.H;
                                this.step = CommandStep.STEP6;
                            }
                            break;

                        case STEP6:
                            if (this.cpuState.external.ui0.rdy == BitState.L)
                            {
                                this.cpuState.external.ui0.stb = BitState.H;
                                String binaryUI0iod = Common.getUIExternalRegisterValue(this.cpuState.external, 0, "IOD");
                                Common.setRegisterValue(this.cpuState.internal, "ARG1R", binaryUI0iod);

                                if (this.step == Common.isLastStep (this.cpuState.internal.cmdr.getValue()))
                                {
                                    this.cpuState.internal.ctlr1.cmdrdy = BitState.H;
                                    this.step = CommandStep.STEP1;
                                }
                                else
                                {
                                    //this.step = CommandStep.STEP7;
                                }
                            }
                            break;
*/
                    }
                }
            }

            if (this.cpuState.internal.ctlr1.cmdrdy == BitState.H || this.cpuState.internal.ctlr1.cmdrun == BitState.H)
            {
                this.cpuState.internal.ctlr1.cmdrun = BitState.H;

                switch (Common.binaryToCommand (this.cpuState.internal.cmdr.getValue()))
                {
                    //реализация комманды NOP
                    case "NOP":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.clk == BitState.H)
                            {
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            this.step = CommandStep.STEP1;
                            this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    //реализация комманды LDI
                    case "LDI":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.clk == BitState.H)
                            {
                                String binaryArg0 = this.cpuState.internal.arg0r.getValue ();
                                String register = Common.binaryToHexName (binaryArg0);

                                if(register != null)
                                {
                                    Common.setRegisterValue (this.cpuState.internal, register, this.cpuState.internal.arg1r.getValue ());
                                }
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            this.step = CommandStep.STEP1;
                            this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;
/*
                    //реализация комманды OUT0
                    case "OUT0":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.ui0.rdy == BitState.H)
                            {
                                //Common.setUIFlagExternal(this.cpuState.external, 0, "DIR", BitState.H);
                                this.cpuState.external.ui0.dir = BitState.H;
                                String outBinaryOptions = Common.getRegisterValue(this.cpuState, arg0);
                                //Common.setUIiod(this.cpuState.external, 0, outBinaryOptions);
                                this.cpuState.external.ui0.iod.setValue (outBinaryOptions);
                                //Common.setUIFlagExternal(this.cpuState.external, 0, "EREQ", BitState.H);
                                this.cpuState.external.ui0.ereq = BitState.H;
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            if (this.cpuState.external.ui0.rdy == BitState.L)
                            {
                                this.cpuState.external.ui0.stb = BitState.H;
                                this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                            }
                        }
                        break;
*/
                    //реализация комманды OUT1
                    case "OUT1":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.ui1.rdy == BitState.H)
                            {
                                this.cpuState.external.ui1.dir = BitState.H;
                                this.cpuState.external.ui1.ereq = BitState.H;
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            if (this.cpuState.external.ui1.rdy == BitState.L)
                            {
                                this.step = CommandStep.STEP3;
                                String binaryArg0 = this.cpuState.internal.arg0r.getValue();
                                String register = Common.binaryToHexName(binaryArg0);
                                this.cpuState.external.ui1.iod.setValue (Common.getRegisterValue (this.cpuState.internal, register));
                                this.cpuState.external.ui1.stb = BitState.L;
                            }
                        }
                        else if (this.step == CommandStep.STEP3)
                        {
                            if (this.cpuState.external.ui1.rdy == BitState.H)
                            {
                                this.step = CommandStep.STEP4;
                                this.cpuState.external.ui1.iod.setValue ("11111111");
                                this.cpuState.external.ui1.stb = BitState.H;
                            }
                        }
                        else if (this.step == CommandStep.STEP4)
                        {
                            this.step = CommandStep.STEP1;
                            this.cpuState.external.ui1.ereq = BitState.L;
                            this.cpuState.external.ui1.dir = BitState.L;
                            this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    case "OUT2":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.ui2.rdy == BitState.H)
                            {
                                this.cpuState.external.ui2.dir = BitState.H;
                                this.cpuState.external.ui2.ereq = BitState.H;
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            if (this.cpuState.external.ui2.rdy == BitState.L)
                            {
                                this.step = CommandStep.STEP3;
                                String binaryArg0 = this.cpuState.internal.arg0r.getValue();
                                String register = Common.binaryToHexName(binaryArg0);
                                this.cpuState.external.ui2.iod.setValue (Common.getRegisterValue (this.cpuState.internal, register));
                                this.cpuState.external.ui2.stb = BitState.L;
                            }
                        }
                        else if (this.step == CommandStep.STEP3)
                        {
                            if (this.cpuState.external.ui2.rdy == BitState.H)
                            {
                                this.step = CommandStep.STEP4;
                                this.cpuState.external.ui2.stb = BitState.H;
                            }
                        }
                        else if (this.step == CommandStep.STEP4)
                        {
                            this.step = CommandStep.STEP1;
                            this.cpuState.external.ui2.ereq = BitState.L;
                            this.cpuState.external.ui2.dir = BitState.L;
                            this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;
/*
                    //реализация комманды SELI0
                    case "SELI0":
                        if (this.cpuState.external.clk == BitState.H)
                        {
                            //Common.setUIis(this.cpuState.external, 0, binaryARG0R);
                            this.cpuState.external.ui0.da.setValue (binaryARG0R);
                        }
                        else
                        {
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;
*/
                    //реализация комманды SELI1
                    case "SELI1":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.clk == BitState.H)
                            {
                                this.cpuState.external.ui1.da.setValue (this.cpuState.internal.arg0r.getValue());
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                            this.step = CommandStep.STEP1;
                        }
                        break;

                    case "SELI2":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.clk == BitState.H)
                            {
                                this.cpuState.external.ui2.da.setValue (this.cpuState.internal.arg0r.getValue());
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                            this.step = CommandStep.STEP1;
                        }
                        break;
/*
                    //реализация комманды SEL
                    case "SEL":
                        if (this.cpuState.external.clk == BitState.H)
                        {
                            String selBinaryOptions = Common.getRegisterValue(this.cpuState, arg0);
                            //Common.setUIis(this.cpuState.external, 0, selBinaryOptions);
                            this.cpuState.external.ui0.da.setValue (selBinaryOptions);
                        }
                        else
                        {
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;
*/
/*
                    //реализация комманды MOV
                    case "MOV":
                        if (this.cpuState.external.clk == BitState.H)
                        {
                            String movTwoIOD = Common.getRegisterValue(this.cpuState, arg1);
                            Common.setRegisterValue(this.cpuState.internal, arg0, movTwoIOD);
                        }
                        else
                        {
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;
*/
                    case "IN2":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.ui2.stb == BitState.H)
                            {
                                this.cpuState.external.ui2.ereq = BitState.H;
                                this.cpuState.external.ui2.rdy = BitState.L;
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            if (this.cpuState.external.ui2.stb == BitState.L)
                            {
                                this.step = CommandStep.STEP3;
                                String binaryArg0 = this.cpuState.internal.arg0r.getValue();
                                String register = Common.binaryToHexName(binaryArg0);
                                Common.setRegisterValue (this.cpuState.internal, register, this.cpuState.external.ui2.iod.getValue ());
                                this.cpuState.external.ui2.rdy = BitState.H;
                            }
                        }
                        else if (this.step == CommandStep.STEP3)
                        {
                            if (this.cpuState.external.ui2.stb == BitState.H)
                            {
                                this.step = CommandStep.STEP1;
                                this.cpuState.external.ui2.ereq = BitState.L;
                                this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                                this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                            }
                        }
                        break;
                }
            }

            // синхронизация UI0 и UB0
            // коопирование значений DA в шину
            String ui0DaValue = this.cpuState.external.ui0.da.getValue ();
            String ub0DaValue = ub0State.getDa ();
            if (!ub0DaValue.equals (ui0DaValue)) ub0State.setDa (ui0DaValue);

            // копирование DIR в шину
            if (this.cpuState.external.ui0.dir != ub0State.getDir ())
                ub0State.setDir (this.cpuState.external.ui0.dir);

            if (this.cpuState.external.ui0.dir == BitState.L)
            {
                // копирование значений выходных выводов в шину
                if (this.cpuState.external.ui0.ereq != ub0State.getEreq ())
                ub0State.setEreq (this.cpuState.external.ui0.ereq);
                if (this.cpuState.external.ui0.rdy != ub0State.getRdy ())
                ub0State.setRdy (this.cpuState.external.ui0.rdy);
            }
            else
            {
                // копирование значений выходных выводов в шину
                if (this.cpuState.external.ui0.ereq != ub0State.getEreq ())
                ub0State.setEreq (this.cpuState.external.ui0.ereq);

                String ui0IodValue = this.cpuState.external.ui0.iod.getValue ();
                String ub0IodValue = ub0State.getIod ();
                if (!ub0IodValue.equals (ui0IodValue)) ub0State.setIod (ui0IodValue);

                if (this.cpuState.external.ui0.stb != ub0State.getStb ())
                ub0State.setStb (this.cpuState.external.ui0.stb);
            }

            // синхронизация UI1 и UB1
            // коопирование значений DA в шину
            String ui1DaValue = this.cpuState.external.ui1.da.getValue ();
            String ub1DaValue = ub1State.getDa ();
            if (!ub1DaValue.equals (ui1DaValue)) ub1State.setDa (ui1DaValue);

            // копирование DIR в шину
            if (this.cpuState.external.ui1.dir != ub1State.getDir ())
                ub1State.setDir (this.cpuState.external.ui1.dir);

            if (this.cpuState.external.ui1.dir == BitState.L)
            {
                // копирование значений выходных выводов в шину
                if (this.cpuState.external.ui1.ereq != ub1State.getEreq ())
                ub1State.setEreq (this.cpuState.external.ui1.ereq);
                if (this.cpuState.external.ui1.rdy != ub1State.getRdy ())
                ub1State.setRdy (this.cpuState.external.ui1.rdy);
            }
            else if (this.cpuState.external.ui1.dir == BitState.H)
            {
                // копирование значений выходных выводов в шину
                if (this.cpuState.external.ui1.ereq != ub1State.getEreq ())
                ub1State.setEreq (this.cpuState.external.ui1.ereq);

                String ui1IodValue = this.cpuState.external.ui1.iod.getValue ();
                String ub1IodValue = ub1State.getIod ();
                if (!ub1IodValue.equals (ui1IodValue)) ub1State.setIod (ui1IodValue);

                if (this.cpuState.external.ui1.stb != ub1State.getStb ())
                ub1State.setStb (this.cpuState.external.ui1.stb);
            }

            // синхронизация UI2 и UB2
            // коопирование значений DA в шину
            String ui2DaValue = this.cpuState.external.ui2.da.getValue ();
            String ub2DaValue = ub2State.getDa ();
            if (!ub2DaValue.equals (ui2DaValue)) ub2State.setDa (ui2DaValue);

            // копирование DIR в шину
            if (this.cpuState.external.ui2.dir != ub2State.getDir ())
                if (this.cpuState.external.ui2.dir == BitState.H)
                    ub2State.setDir (this.cpuState.external.ui2.dir);

            if (this.cpuState.external.ui2.dir == BitState.L)
            {
                // копирование значений выходных выводов в шину
                if (this.cpuState.external.ui2.ereq != ub2State.getEreq ())
                ub2State.setEreq (this.cpuState.external.ui2.ereq);
                if (this.cpuState.external.ui2.rdy != ub2State.getRdy ())
                ub2State.setRdy (this.cpuState.external.ui2.rdy);
            }
            else if (this.cpuState.external.ui2.dir == BitState.H)
            {
                // копирование значений выходных выводов в шину
                if (this.cpuState.external.ui2.ereq != ub2State.getEreq ())
                ub2State.setEreq (this.cpuState.external.ui2.ereq);

                String ui2IodValue = this.cpuState.external.ui2.iod.getValue ();
                String ub2IodValue = ub2State.getIod ();
                if (!ub2IodValue.equals (ui2IodValue)) ub2State.setIod (ui2IodValue);

                if (this.cpuState.external.ui2.stb != ub2State.getStb ())
                ub2State.setStb (this.cpuState.external.ui2.stb);
            }

            // копирование DIR в шину
            if (this.cpuState.external.ui2.dir != ub2State.getDir ())
                if (this.cpuState.external.ui2.dir == BitState.L)
                    ub2State.setDir (this.cpuState.external.ui2.dir);

            this.cpuStatePrev = this.cpuState.copy();
        }
    }

    private void setStatusText(String cmd, String arg0, String arg1)
    {
        this.statusText = cmd;
        if (!arg0.equals("")) this.statusText += " " + arg0;
        if (!arg1.equals("")) this.statusText += ", " + arg1;
    }

    public String getStatusText()
    {
        if (this.statusText != null) return this.statusText;
        else return "";
    }

    public CPUState getCPUState()
    {
        return this.cpuState;
    }
}