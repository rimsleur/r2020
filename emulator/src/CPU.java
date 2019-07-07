public class CPU
{
    public CPUState cpuState;

    private CPUState cpuStatePrev;

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
    }

    public void calculate(Integer times)
    {
        for (int i = 1; i <= times; i++)
        {
            this.cpuState.external.clk = Common.invertPinState (this.cpuState.external.clk);

            UB0State ub0State = UB0State.getInstance ();
            this.cpuState.external.cpuStateExternalUI0.drdy = ub0State.getDrdy ();
            this.cpuState.external.cpuStateExternalUI0.iod.setValue (ub0State.getIod ());

            UB1State ub1State = UB1State.getInstance ();
            this.cpuState.external.cpuStateExternalUI1.drdy = ub1State.getDrdy();
            this.cpuState.external.cpuStateExternalUI1.iod.setValue (ub1State.getIod ());

            if (this.cpuState.external.cpuStateExternalUI0.drdy == BitState.H && this.cpuState.external.cpuStateExternalUI0.drdy != this.cpuStatePrev.external.cpuStateExternalUI0.drdy)
            {
                this.cpuState.external.cpuStateExternalUI0.dir = BitState.L;
                this.cpuState.external.cpuStateExternalUI0.ereq = BitState.L;
                this.cpuState.external.cpuStateExternalUI0.stb = BitState.L;
            }

            if (this.cpuState.external.cpuStateExternalUI1.drdy == BitState.H && this.cpuState.external.cpuStateExternalUI1.drdy != this.cpuStatePrev.external.cpuStateExternalUI1.drdy)
            {
                this.cpuState.external.cpuStateExternalUI1.dir = BitState.L;
                this.cpuState.external.cpuStateExternalUI1.ereq = BitState.L;
                this.cpuState.external.cpuStateExternalUI1.stb = BitState.L;
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
                            if (this.cpuState.external.clk == BitState.H && this.cpuState.external.cpuStateExternalUI0.drdy == BitState.H)
                            {
                                Common.setUIis(this.cpuState.external, 0, "00100000");
                                Common.setUIFlagExternal(this.cpuState.external, 0, "EREQ", BitState.H);
                                this.step = CommandStep.STEP2;
                            }
                            break;

                        case STEP2:
                            if (this.cpuState.external.cpuStateExternalUI0.drdy == BitState.L)
                            {
                                this.cpuState.external.cpuStateExternalUI0.stb = BitState.H;
                                String binaryUI0iod = Common.getUIExternalRegisterValue(this.cpuState.external, 0, "IOD");
                                Common.setRegisterValue(this.cpuState.internal, "CMDR", binaryUI0iod);

                                if (this.step == Common.isLastStep (this.cpuState.internal.cmdr.getValue()))
                                {
                                    Common.setRegisterValue(this.cpuState.internal, "ARG0R", "00000000");
                                    Common.setRegisterValue(this.cpuState.internal, "ARG1R", "00000000");
                                    this.cpuState.internal.ctlr1.cmdrdy = BitState.H;
                                    this.step = CommandStep.STEP1;
                                }
                                else
                                {
                                    this.step = CommandStep.STEP3;
                                }
                            }
                            break;

                        case STEP3:
                            if (this.cpuState.external.cpuStateExternalUI0.drdy == BitState.H)
                            {
                                Common.setUIFlagExternal(this.cpuState.external, 0, "EREQ", BitState.H);
                                this.step = CommandStep.STEP4;
                            }
                            break;

                        case STEP4:
                            if (this.cpuState.external.cpuStateExternalUI0.drdy == BitState.L)
                            {
                                this.cpuState.external.cpuStateExternalUI0.stb = BitState.H;
                                String binaryUI0iod = Common.getUIExternalRegisterValue(this.cpuState.external, 0, "IOD");
                                Common.setRegisterValue(this.cpuState.internal, "ARG0R", binaryUI0iod);

                                if (this.step == Common.isLastStep (this.cpuState.internal.cmdr.getValue()))
                                {
                                    Common.setRegisterValue(this.cpuState.internal, "ARG1R", "00000000");
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
                            if (this.cpuState.external.cpuStateExternalUI0.drdy == BitState.H)
                            {
                                Common.setUIFlagExternal(this.cpuState.external, 0, "EREQ", BitState.H);
                                this.step = CommandStep.STEP6;
                            }
                            break;

                        case STEP6:
                            if (this.cpuState.external.cpuStateExternalUI0.drdy == BitState.L)
                            {
                                this.cpuState.external.cpuStateExternalUI0.stb = BitState.H;
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
                    }
                }
            }

            if (this.cpuState.internal.ctlr1.cmdrdy == BitState.H || this.cpuState.internal.ctlr1.cmdrun == BitState.H)
            {
                this.cpuState.internal.ctlr1.cmdrun = BitState.H;

                switch (this.cpuState.internal.cmdr.getValue())
                {
                    //реализация комманды NOP
                    case "00000000":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.clk == BitState.H)
                            {
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                            this.step = CommandStep.STEP1;
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    //реализация комманды LDI
                    case "00000001":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.clk == BitState.H)
                            {
                                String binaryArg0 = this.cpuState.internal.arg0r.getValue();
                                String register = Common.binaryToHexName(binaryArg0);

                                if(register != null)
                                {
                                    Common.setRegisterValue(this.cpuState.internal, register, this.cpuState.internal.arg1r.getValue());
                                }
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                            this.step = CommandStep.STEP1;
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    //реализация комманды OUT0
                    case "OUT0":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.cpuStateExternalUI0.drdy == BitState.H)
                            {
                                Common.setUIFlagExternal(this.cpuState.external, 0, "DIR", BitState.H);
                                String outBinaryOptions = Common.getRegisterValue(this.cpuState, arg0);
                                Common.setUIiod(this.cpuState.external, 0, outBinaryOptions);
                                Common.setUIFlagExternal(this.cpuState.external, 0, "EREQ", BitState.H);
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            if (this.cpuState.external.cpuStateExternalUI0.drdy == BitState.L)
                            {
                                this.cpuState.external.cpuStateExternalUI0.stb = BitState.H;
                                this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                            }
                        }
                        break;

                    //реализация комманды OUT1
                    case "00000111":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.cpuStateExternalUI1.drdy == BitState.H)
                            {
                                Common.setUIFlagExternal(this.cpuState.external, 1, "DIR", BitState.H);
                                String binaryArg0 = this.cpuState.internal.arg0r.getValue();
                                String register = Common.binaryToHexName(binaryArg0);
                                Common.setUIiod(this.cpuState.external, 1, Common.getRegisterValue(this.cpuState, register));
                                Common.setUIFlagExternal(this.cpuState.external, 1, "EREQ", BitState.H);
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            if (this.cpuState.external.cpuStateExternalUI1.drdy == BitState.L)
                            {
                                this.cpuState.external.cpuStateExternalUI1.stb = BitState.H;
                                this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                                this.step = CommandStep.STEP1;
                                this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                            }
                        }
                        break;

                    //реализация комманды SELI0
                    case "SELI0":
                        if (this.cpuState.external.clk == BitState.H)
                        {
                            Common.setUIis(this.cpuState.external, 0, binaryARG0R);
                        }
                        else
                        {
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    //реализация комманды SELI1
                    case "00000110":
                        if (this.step == CommandStep.STEP1)
                        {
                            if (this.cpuState.external.clk == BitState.H)
                            {
                                Common.setUIis(this.cpuState.external, 1, this.cpuState.internal.arg0r.getValue());
                                this.step = CommandStep.STEP2;
                            }
                        }
                        else if (this.step == CommandStep.STEP2)
                        {
                            this.cpuState.internal.ctlr1.cmdrdy = BitState.L;
                            this.step = CommandStep.STEP1;
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

                    //реализация комманды SEL
                    case "SEL":
                        if (this.cpuState.external.clk == BitState.H)
                        {
                            String selBinaryOptions = Common.getRegisterValue(this.cpuState, arg0);
                            Common.setUIis(this.cpuState.external, 0, selBinaryOptions);
                        }
                        else
                        {
                            this.cpuState.internal.ctlr1.cmdrun = BitState.L;
                        }
                        break;

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
                }
            }

            //если значение IS не совпадает с ISB, копирую
            String uI0ValueIS = Common.getUIExternalRegisterValue(this.cpuState.external, 0, "IS");
            String uI0ValueISB = Common.getUIBusRegisterValue(0, "ISB");
            if (!uI0ValueIS.equals(uI0ValueISB)) ub0State.setDa(uI0ValueIS);

            String uI1ValueIS = Common.getUIExternalRegisterValue(this.cpuState.external, 1, "IS");
            String uI1ValueISB = Common.getUIBusRegisterValue(1, "ISB");
            if (!uI1ValueIS.equals(uI1ValueISB)) ub1State.setDa(uI1ValueIS);

            //проверка флагов для UIBus0
            if (this.cpuState.external.cpuStateExternalUI0.dir != ub0State.getDir())
                ub0State.setDir(this.cpuState.external.cpuStateExternalUI0.dir);
            if (this.cpuState.external.cpuStateExternalUI0.ereq != ub0State.getEreq())
                ub0State.setEreq(this.cpuState.external.cpuStateExternalUI0.ereq);
            if (this.cpuState.external.cpuStateExternalUI0.stb != ub0State.getStb())
                ub0State.setStb(this.cpuState.external.cpuStateExternalUI0.stb);

            //проверка флагов для UIBus1
            if (this.cpuState.external.cpuStateExternalUI1.dir != ub1State.getDir())
                ub1State.setDir(this.cpuState.external.cpuStateExternalUI1.dir);
            if (this.cpuState.external.cpuStateExternalUI1.ereq != ub1State.getEreq())
                ub1State.setEreq(this.cpuState.external.cpuStateExternalUI1.ereq);
            if (this.cpuState.external.cpuStateExternalUI1.stb != ub1State.getStb())
                ub1State.setStb(this.cpuState.external.cpuStateExternalUI1.stb);

            //если значение IOD не совпадает с DB, копирую
            if (ub0State.getDir() == BitState.H)
            {
                String uI0ValueIOD = Common.getUIExternalRegisterValue(this.cpuState.external, 0, "IOD");
                String uI0ValueDB = Common.getUIBusRegisterValue(0, "DB");
                if (!uI0ValueIOD.equals(uI0ValueDB)) ub0State.setIod(uI0ValueIOD);
            }

            if (ub1State.getDir() == BitState.H)
            {
                String uI1ValueIOD = Common.getUIExternalRegisterValue(this.cpuState.external, 1, "IOD");
                String uI1ValueDB = Common.getUIBusRegisterValue(1, "DB");
                if (!uI1ValueIOD.equals(uI1ValueDB)) ub1State.setIod(uI1ValueIOD);
            }

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