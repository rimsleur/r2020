public class I00
{
    public I00State i00State;

    private I00State i00StatePrev;

    private String commandPrev; //переменная для записи прошлой комманды

    private Integer iteratorCommand; //переменная для итерации команд

    private String statusText;

    private CommandStep step;

    private String cmd; //первый параметр
    private String arg0; //второй параметр
    private String arg1; //третий параметр

    private String binaryCMDR; //двоичный код первого параметра
    private String binaryARG0R; //двоичный код второго параметра
    private String binaryARG1R; //двоичный код третьего параметра

    //private String[] commandList = new String[]{"LDI R0, 0xFF"};
    //private String[] commandList = new String[]{"SELI 0xFF"};
    //private String[] commandList = new String[]{"SEL R0"};
    //private String[] commandList = new String[]{"MOV R0, R2"};
    //private String[] commandList = new String[]{"LDI R1, 0x02", "OUT R1"};
    //private String[] commandList = new String[]{"SELI0 0x1F", "LDI R1, 0x02", "OUT0 R1"};

    private String[] commandList = new String[]{"SELI1 0x1F", "LDI R1, 0x02", "OUT1 R1"}; //Массив, в котором хранятся все команды


    public I00()
    {
        this.i00State = new I00State(true);
        this.i00StatePrev = this.i00State.copy();
        this.iteratorCommand = 0;
        this.commandPrev = null;

        this.cmd = "";
        this.arg0 = "";
        this.arg1 = "";
        this.binaryCMDR = "";
        this.binaryARG0R = "";
        this.binaryARG1R = "";

        this.step = CommandStep.STEP1;
    }

    public void calculate(Integer times)
    {
        for (int i = 1; i <= times; i++)
        {
            this.i00State.external.clk = Common.invertPinState (this.i00State.external.clk);

            I00StateUIBus0 i00StateUIBus0 = I00StateUIBus0.getInstance ();
            this.i00State.external.i00StateExternalUI0.irdy = i00StateUIBus0.getIrdy ();
            this.i00State.external.i00StateExternalUI0.iod.setValue (i00StateUIBus0.getDb ());

            I00StateUIBus1 i00StateUIBus1 = I00StateUIBus1.getInstance ();
            this.i00State.external.i00StateExternalUI1.irdy = i00StateUIBus1.getIrdy();
            this.i00State.external.i00StateExternalUI1.iod.setValue (i00StateUIBus1.getDb ());

            if (this.i00State.external.i00StateExternalUI0.irdy == BitState.H && this.i00State.external.i00StateExternalUI0.irdy != this.i00StatePrev.external.i00StateExternalUI0.irdy)
            {
                this.i00State.external.i00StateExternalUI0.dir = BitState.L;
                this.i00State.external.i00StateExternalUI0.ereq = BitState.L;
                this.i00State.external.i00StateExternalUI0.stb = BitState.L;
            }

            if (this.i00State.external.i00StateExternalUI1.irdy == BitState.H && this.i00State.external.i00StateExternalUI1.irdy != this.i00StatePrev.external.i00StateExternalUI1.irdy)
            {
                this.i00State.external.i00StateExternalUI1.dir = BitState.L;
                this.i00State.external.i00StateExternalUI1.ereq = BitState.L;
                this.i00State.external.i00StateExternalUI1.stb = BitState.L;
            }

            if (this.i00State.internal.ctlr1.ui0md == BitState.H)
            {
                if (iteratorCommand >= commandList.length)
                {
                    iteratorCommand = 0;
                }

                if (this.i00State.external.clk == BitState.H)
                {
                    if (this.i00State.internal.ctlr1.cmdrun == BitState.L && this.i00State.internal.ctlr1.cmdrdy == BitState.L)
                    {
                        String[] options = Common.getOptions(commandList[iteratorCommand++]);

                        cmd = options[0];
                        if (options.length > 1) arg0 = options[1];
                        else arg0 = "";
                        if (options.length > 2) arg1 = options[2];
                        else arg1 = "";

                        binaryCMDR = Common.getCommandToBinary(cmd);
                        Common.setRegisterValue(this.i00State.internal, "CMDR", binaryCMDR);

                        this.step = CommandStep.STEP1;

                        if (!arg0.equals(""))
                        {
                            if (arg0.substring(0, 2).equals("0x"))
                                binaryARG0R = Common.hexToBin(arg0); //16-й первого параметра в 2-й
                            else
                                binaryARG0R = Common.getRegisterToBinary(arg0); //16-й первого параметра в 2-й
                            Common.setRegisterValue(this.i00State.internal, "ARG0R", binaryARG0R);
                        } else Common.clearRegister(this.i00State.internal, "ARG0R");

                        if (!arg1.equals(""))
                        {
                            if (arg1.substring(0, 2).equals("0x"))
                                binaryARG1R = Common.hexToBin(arg1);
                            else
                                binaryARG1R = Common.getRegisterToBinary(arg1);
                            Common.setRegisterValue(this.i00State.internal, "ARG1R", binaryARG1R);
                        } else Common.clearRegister(this.i00State.internal, "ARG1R");

                        setStatusText(cmd, arg0, arg1);
                        this.i00State.internal.ctlr1.cmdrdy = BitState.H;
                        this.commandPrev = cmd;
                    }
                    else
                    {
                        cmd = this.commandPrev;
                    }
                }
                else
                {
                    this.i00State.internal.ctlr1.cmdrdy = BitState.L;
                    cmd = this.commandPrev;
                }
            }
            else
            {
                if (this.i00State.internal.ctlr1.cmdrun == BitState.L && this.i00State.internal.ctlr1.cmdrdy == BitState.L)
                {
                    switch (this.step)
                    {
                        case STEP1:
                            if (this.i00State.external.clk == BitState.H && this.i00State.external.i00StateExternalUI0.irdy == BitState.H)
                            {
                                Common.setUIis(this.i00State.external, 0, "00100000");
                                Common.setUIFlagExternal(this.i00State.external, 0, "EREQ", BitState.H);
                                this.step = CommandStep.STEP2;
                            }
                            break;

                        case STEP2:
                            if (this.i00State.external.i00StateExternalUI0.irdy == BitState.L)
                            {
                                this.i00State.external.i00StateExternalUI0.stb = BitState.H;
                                String binaryUI0iod = Common.getUIExternalRegisterValue(this.i00State.external, 0, "IOD");
                                Common.setRegisterValue(this.i00State.internal, "CMDR", binaryUI0iod);

                                if (this.step == Common.isLastStep (this.i00State.internal.cmdr.getValue()))
                                {
                                    Common.setRegisterValue(this.i00State.internal, "ARG0R", "00000000");
                                    Common.setRegisterValue(this.i00State.internal, "ARG1R", "00000000");
                                    this.i00State.internal.ctlr1.cmdrdy = BitState.H;
                                    this.step = CommandStep.STEP1;
                                }
                                else
                                {
                                    this.step = CommandStep.STEP3;
                                }
                            }
                            break;

                        case STEP3:
                            if (this.i00State.external.i00StateExternalUI0.irdy == BitState.H)
                            {
                                Common.setUIFlagExternal(this.i00State.external, 0, "EREQ", BitState.H);
                                this.step = CommandStep.STEP4;
                            }
                            break;

                        case STEP4:
                            if (this.i00State.external.i00StateExternalUI0.irdy == BitState.L)
                            {
                                this.i00State.external.i00StateExternalUI0.stb = BitState.H;
                                String binaryUI0iod = Common.getUIExternalRegisterValue(this.i00State.external, 0, "IOD");
                                Common.setRegisterValue(this.i00State.internal, "ARG0R", binaryUI0iod);

                                if (this.step == Common.isLastStep (this.i00State.internal.cmdr.getValue()))
                                {
                                    Common.setRegisterValue(this.i00State.internal, "ARG1R", "00000000");
                                    this.i00State.internal.ctlr1.cmdrdy = BitState.H;
                                    this.step = CommandStep.STEP1;
                                }
                                else
                                {
                                    this.step = CommandStep.STEP5;
                                }
                            }
                            break;

                        case STEP5:
                            if (this.i00State.external.i00StateExternalUI0.irdy == BitState.H)
                            {
                                Common.setUIFlagExternal(this.i00State.external, 0, "EREQ", BitState.H);
                                this.step = CommandStep.STEP6;
                            }
                            break;

                        case STEP6:
                            if (this.i00State.external.i00StateExternalUI0.irdy == BitState.L)
                            {
                                this.i00State.external.i00StateExternalUI0.stb = BitState.H;
                                String binaryUI0iod = Common.getUIExternalRegisterValue(this.i00State.external, 0, "IOD");
                                Common.setRegisterValue(this.i00State.internal, "ARG1R", binaryUI0iod);

                                if (this.step == Common.isLastStep (this.i00State.internal.cmdr.getValue()))
                                {
                                    this.i00State.internal.ctlr1.cmdrdy = BitState.H;
                                    this.step = CommandStep.STEP1;
                                }
                                else
                                {
                                    //this.step = CommandStep.STEP7;
                                }
                            }
                            break;
                    }
                }
            }

            if (this.i00State.internal.ctlr1.cmdrdy == BitState.H || this.i00State.internal.ctlr1.cmdrun == BitState.H)
            {
                this.i00State.internal.ctlr1.cmdrun = BitState.H;

                switch (this.i00State.internal.cmdr.getValue())
                {
                    //реализация комманды NOP
                    case "00000000":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.i00State.external.clk == BitState.H)
                            {
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            this.i00State.internal.ctlr1.cmdrdy = BitState.L;
                            this.step = CommandStep.STEP1;
                            this.i00State.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    //реализация комманды LDI
                    case "00000001":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.i00State.external.clk == BitState.H)
                            {
                                String binaryArg0 = this.i00State.internal.arg0r.getValue();
                                String register = Common.binaryToHexName(binaryArg0);

                                if(register != null)
                                {
                                    Common.setRegisterValue(this.i00State.internal, register, this.i00State.internal.arg1r.getValue());
                                }
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            this.i00State.internal.ctlr1.cmdrdy = BitState.L;
                            this.step = CommandStep.STEP1;
                            this.i00State.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    //реализация комманды OUT0
                    case "OUT0":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.i00State.external.i00StateExternalUI0.irdy == BitState.H) {
                                Common.setUIFlagExternal(this.i00State.external, 0, "DIR", BitState.H);
                                String outBinaryOptions = Common.getRegisterValue(this.i00State, arg0);
                                Common.setUIiod(this.i00State.external, 0, outBinaryOptions);
                                Common.setUIFlagExternal(this.i00State.external, 0, "EREQ", BitState.H);
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            if (this.i00State.external.i00StateExternalUI0.irdy == BitState.L)
                            {
                                this.i00State.external.i00StateExternalUI0.stb = BitState.H;
                                this.i00State.internal.ctlr1.cmdrun = BitState.L;
                            }
                        }
                        break;

                    //реализация комманды OUT1
                    case "00000111":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.i00State.external.i00StateExternalUI1.irdy == BitState.H)
                            {
                                Common.setUIFlagExternal(this.i00State.external, 1, "DIR", BitState.H);
                                String binaryArg0 = this.i00State.internal.arg0r.getValue();
                                String register = Common.binaryToHexName(binaryArg0);
                                Common.setUIiod(this.i00State.external, 1, Common.getRegisterValue(this.i00State, register));
                                Common.setUIFlagExternal(this.i00State.external, 1, "EREQ", BitState.H);
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            if (this.i00State.external.i00StateExternalUI1.irdy == BitState.L)
                            {
                                this.i00State.external.i00StateExternalUI1.stb = BitState.H;
                                this.i00State.internal.ctlr1.cmdrdy = BitState.L;
                                this.step = CommandStep.STEP1;
                                this.i00State.internal.ctlr1.cmdrun = BitState.L;
                            }
                        }
                        break;

                    //реализация комманды SELI0
                    case "SELI0":
                        if (this.i00State.external.clk == BitState.H)
                        {
                            Common.setUIis(this.i00State.external, 0, binaryARG0R);
                        }
                        else
                        {
                            this.i00State.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    //реализация комманды SELI1
                    case "00000110":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.i00State.external.clk == BitState.H)
                            {
                                Common.setUIis(this.i00State.external, 1, this.i00State.internal.arg0r.getValue());
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            this.i00State.internal.ctlr1.cmdrdy = BitState.L;
                            this.step = CommandStep.STEP1;
                            this.i00State.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    //реализация комманды SEL
                    case "SEL":
                        if (this.i00State.external.clk == BitState.H)
                        {
                            String selBinaryOptions = Common.getRegisterValue(this.i00State, arg0);
                            Common.setUIis(this.i00State.external, 0, selBinaryOptions);
                        }
                        else
                        {
                            this.i00State.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    //реализация комманды MOV
                    case "MOV":
                        if (this.i00State.external.clk == BitState.H)
                        {
                            String movTwoIOD = Common.getRegisterValue(this.i00State, arg1);
                            Common.setRegisterValue(this.i00State.internal, arg0, movTwoIOD);
                        }
                        else
                        {
                            this.i00State.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;
                }
            }

            //если значение IS не совпадает с ISB, копирую
            String uI0ValueIS = Common.getUIExternalRegisterValue(this.i00State.external, 0, "IS");
            String uI0ValueISB = Common.getUIBusRegisterValue(0, "ISB");
            if (!uI0ValueIS.equals(uI0ValueISB)) i00StateUIBus0.setIsb(uI0ValueIS);

            String uI1ValueIS = Common.getUIExternalRegisterValue(this.i00State.external, 1, "IS");
            String uI1ValueISB = Common.getUIBusRegisterValue(1, "ISB");
            if (!uI1ValueIS.equals(uI1ValueISB)) i00StateUIBus1.setIsb(uI1ValueIS);

            //проверка флагов для UIBus0
            if (this.i00State.external.i00StateExternalUI0.dir != i00StateUIBus0.getDir())
                i00StateUIBus0.setDir(this.i00State.external.i00StateExternalUI0.dir);
            if (this.i00State.external.i00StateExternalUI0.ereq != i00StateUIBus0.getEreq())
                i00StateUIBus0.setEreq(this.i00State.external.i00StateExternalUI0.ereq);
            if (this.i00State.external.i00StateExternalUI0.stb != i00StateUIBus0.getStb())
                i00StateUIBus0.setStb(this.i00State.external.i00StateExternalUI0.stb);

            //проверка флагов для UIBus1
            if (this.i00State.external.i00StateExternalUI1.dir != i00StateUIBus1.getDir())
                i00StateUIBus1.setDir(this.i00State.external.i00StateExternalUI1.dir);
            if (this.i00State.external.i00StateExternalUI1.ereq != i00StateUIBus1.getEreq())
                i00StateUIBus1.setEreq(this.i00State.external.i00StateExternalUI1.ereq);
            if (this.i00State.external.i00StateExternalUI1.stb != i00StateUIBus1.getStb())
                i00StateUIBus1.setStb(this.i00State.external.i00StateExternalUI1.stb);

            //если значение IOD не совпадает с DB, копирую
            if (i00StateUIBus0.getDir() == BitState.H)
            {
                String uI0ValueIOD = Common.getUIExternalRegisterValue(this.i00State.external, 0, "IOD");
                String uI0ValueDB = Common.getUIBusRegisterValue(0, "DB");
                if (!uI0ValueIOD.equals(uI0ValueDB)) i00StateUIBus0.setDb(uI0ValueIOD);
            }

            if (i00StateUIBus1.getDir() == BitState.H)
            {
                String uI1ValueIOD = Common.getUIExternalRegisterValue(this.i00State.external, 1, "IOD");
                String uI1ValueDB = Common.getUIBusRegisterValue(1, "DB");
                if (!uI1ValueIOD.equals(uI1ValueDB)) i00StateUIBus1.setDb(uI1ValueIOD);
            }

            this.i00StatePrev = this.i00State.copy();
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

    public I00State getI00State()
    {
        return this.i00State;
    }
}