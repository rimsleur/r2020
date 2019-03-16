public class Common {
    public static BitState invertPinState(BitState state) {
        if (state == BitState.L) return BitState.H;
        else if (state == BitState.H) return BitState.L;
        return BitState.Z;
    }

    //название команд
    static String[] commandsName = new String[]{"NOP", "LDI", "SELI0", "SEL", "MOV", "OUT0", "SELI1", "OUT1"};

    //16-й код команд
    static String[] commandsHex = new String[]{"0x00", "0x01", "0x02", "0x03", "0x04", "0x05", "0x06", "0x07"};

    private static CommandStep[] countSteps = new CommandStep[]
            {
                    CommandStep.STEP2, // NOP
                    CommandStep.STEP6, // LDI
                    CommandStep.STEP4, // SELI0
                    CommandStep.STEP4, // SEL
                    CommandStep.STEP6, // MOV
                    CommandStep.STEP4, // OUT0
                    CommandStep.STEP4, // SELI1
                    CommandStep.STEP4  // OUT1
            };

    //названия регистров
    static String[] registersName = new String[]{"R0", "R1", "R2"};

    //16-й код регистров
    static String[] registersHex = new String[]{"0x00", "0x01", "0x02"};

    private final static String zeroBinary = "00000000"; //константа для сбрасывания значения диодов

    //возвращает последний шаг для команды
    public static CommandStep isLastStep(String binaryCode)
    {
        for (int i = 0; i < commandsName.length; i++)
        {
            if (binaryCode.equals(hexToBin(commandsHex[i]))) return countSteps[i];
        }

        return null;
    }

    //изменяет регистр IS
    public static void setUIis(I00StateExternal external, Integer numUI, String binaryCode) {
        if (numUI == 0) external.i00StateExternalUI0.is.setValue(binaryCode);
        else external.i00StateExternalUI1.is.setValue(binaryCode);
    }

    //изменяет регистр IOD
    public static void setUIiod(I00StateExternal external, Integer numUI, String binaryCode) {
        if (numUI == 0) external.i00StateExternalUI0.iod.setValue(binaryCode);
        else external.i00StateExternalUI1.iod.setValue(binaryCode);
    }


    //изменяет флаг DIR, EREQ, STB
    public static void setUIFlagExternal(I00StateExternal external, Integer numUI, String nameFlag, BitState bitState) {
        switch (nameFlag) {
            case "DIR":
                if (numUI == 0) external.i00StateExternalUI0.dir = bitState;
                else external.i00StateExternalUI1.dir = bitState;
                break;

            case "EREQ":
                if (numUI == 0) external.i00StateExternalUI0.ereq = bitState;
                else external.i00StateExternalUI1.ereq = bitState;
                break;

            case "STB":
                if (numUI == 0) external.i00StateExternalUI0.stb = bitState;
                else external.i00StateExternalUI1.stb = bitState;
                break;
        }
    }

    public static void setUIFlagBus(Integer numUI, String nameFlag, BitState bitState) {
        switch (nameFlag) {
            case "DIR":
                if (numUI == 0) I00StateUIBus0.getInstance().setDir(bitState);
                else I00StateUIBus1.getInstance().setDir(bitState);
                break;

            case "EREQ":
                if (numUI == 0) I00StateUIBus0.getInstance().setEreq(bitState);
                else I00StateUIBus1.getInstance().setEreq(bitState);
                break;

            case "STB":
                if (numUI == 0) I00StateUIBus0.getInstance().setStb(bitState);
                else I00StateUIBus1.getInstance().setStb(bitState);
                break;
        }
    }

    //изменяет регистр ISB (UIBus)
    public static void setISB(Integer numUI, String binaryCode) {
        if (numUI == 0) I00StateUIBus0.getInstance().setIsb(binaryCode);
        else I00StateUIBus1.getInstance().setIsb(binaryCode);
    }

    //изменяет регистр DB (UIBus)
    public static void setDB(Integer numUI, String binaryCode)
    {
        if (numUI == 0) I00StateUIBus0.getInstance().setDb(binaryCode);
        else I00StateUIBus1.getInstance().setDb(binaryCode);
    }

    //изменяет регистр AO
    public static void setA0(I1FStateA0 a0, String binaryCode) {
        a0.setValue(binaryCode);
    }

    public static void setA0(I20StateA0 a0, String binaryCode) {
        a0.setValue(binaryCode);
    }

    //устанавливает значение для заданого регистра
    public static void setRegisterValue(I00StateInternal internal, String nameRegister, String binaryCode) {
        switch (nameRegister) {
            case "CMDR":
                internal.cmdr.setValue(binaryCode);
                break;
            case "ARG0R":
                internal.arg0r.setValue(binaryCode);
                break;
            case "ARG1R":
                internal.arg1r.setValue(binaryCode);
                break;
            case "R0":
                internal.r0.setValue(binaryCode);
                break;
            case "R1":
                internal.r1.setValue(binaryCode);
                break;
            case "R2":
                internal.r2.setValue(binaryCode);
                break;
        }
    }

    //очистка регистра internal
    public static void clearRegister(I00StateInternal internal, String nameRegister) {
        switch (nameRegister) {
            case "ARG1R":
                internal.arg1r.setValue(zeroBinary);
                break;
            case "R0":
                internal.r0.setValue(zeroBinary);
                break;
            case "R1":
                internal.r1.setValue(zeroBinary);
                break;
            case "R2":
                internal.r2.setValue(zeroBinary);
                break;
        }
    }

    //очистка регистра external
    public static void clearRegister(I00StateExternal external, Integer numUI, String nameRegister) {
        switch (nameRegister) {
            case "IS":
                if (numUI == 0) external.i00StateExternalUI0.is.setValue(zeroBinary);
                else external.i00StateExternalUI1.is.setValue(zeroBinary);
                break;
            case "IOD":
                if (numUI == 0) external.i00StateExternalUI0.iod.setValue(zeroBinary);
                else external.i00StateExternalUI1.iod.setValue(zeroBinary);
                break;
        }
    }

    //возвращает значения регистров UIBus в 2-м коде
    public static String getUIBusRegisterValue(Integer numUI, String nameRegister) {
        String result = null;

        switch (nameRegister) {
            case "ISB":
                if (numUI == 0) result = I00StateUIBus0.getInstance().getIsb();
                else result = I00StateUIBus1.getInstance().getIsb();
                break;
            case "DB":
                if (numUI == 0) result = I00StateUIBus0.getInstance().getDb();
                else result = I00StateUIBus1.getInstance().getDb();
                break;
        }

        return result;
    }

    //возвращает значения регистров в 2-м коде
    public static String getRegisterValue(I00State state, String nameRegister) {
        String result = null;

        switch (nameRegister) {
            case "R0":
                result = state.internal.r0.getValue();
                break;
            case "R1":
                result = state.internal.r1.getValue();
                break;
            case "R2":
                result = state.internal.r2.getValue();
                break;
        }
        return result;
    }

    //возвращает значения регистров External в 2-м коде
    public static String getUIExternalRegisterValue(I00StateExternal external, Integer numUI, String nameRegister) {
        String result = null;

        switch (nameRegister) {
            case "IS":

                if (numUI == 0) result = external.i00StateExternalUI0.is.getValue();
                else result = external.i00StateExternalUI1.is.getValue();
                break;

            case "IOD":
                if (numUI == 0) result = external.i00StateExternalUI0.iod.getValue();
                else result = external.i00StateExternalUI1.iod.getValue();
                break;
        }

        return result;
    }


    //возвращает значения BitState L = 0 или H = 1
    public static BitState getPinValue(char s) {
        if (s == '0') return BitState.L;
        else return BitState.H;
    }

    //возращает 0 или 1 в зависимости от BitState
    public static String getPinState(BitState bitState) {
        if (bitState == BitState.L) return "0";
        else return "1";
    }

    //возвращает 2-й код команды
    public static String getCommandToBinary(String command) {
        for (int i = 0; i < commandsName.length; i++) {
            if (commandsName[i].equals(command)) {
                String s = commandsHex[i];
                return hexToBin(s);
            }
        }

        return null;
    }

    //возвращает 2-й код регистра
    public static String getRegisterToBinary(String register) {

        for (int i = 0; i < commandsName.length; i++) {
            if (registersName[i].equals(register)) {
                String s = registersHex[i];

                return hexToBin(s);
            }
        }

        return null;
    }

    //возвращает 16-й код буквы в десятичном формате
    public static Integer hexNumberToDec(char s) {
        Integer result = 0;

        switch (s) {
            case 'A':
                result = 10;
                break;

            case 'B':
                result = 11;
                break;

            case 'C':
                result = 12;
                break;

            case 'D':
                result = 13;
                break;

            case 'E':
                result = 14;
                break;

            case 'F':
                result = 15;
                break;
        }

        return result;

    }

    //разбивает и возваращает команду на составляющие
    public static String[] getOptions(String command) {
        String[] options = command.split(" ");

        if (options.length > 2) {
            StringBuffer sb = new StringBuffer(options[1]);
            sb.delete(options.length - 1, options.length);

            options[1] = sb.toString();
        }

        return options;
    }

    public static String binaryToHexName(String binary)
    {
        for(int i = 0; i < registersHex.length; i++)
        {
            if(binary.equals(hexToBin(registersHex[i]))) return registersName[i];
        }

        for(int j = 0; j < commandsHex.length; j++) if (binary.equals(hexToBin(commandsHex[j]))) return commandsName[j];

        return null;
    }
    
    //возвращает 16-й код в 2-м
    public static String hexToBin(String hex)
    {
        final Integer row = 4; //константа, 0xFF -> 1111 1111

        String[] result = new String[2]; //для хранения результата и возврада из функции

        String str = null;

        String[] numbers = hex.split("x"); //разделяю 0x00 на 0x и 00

        if(hex.length() != 2) str = numbers[1]; //из 0x00 сохраняю 00
        else str = hex;

        char left = str.charAt(0); //сохраняю первый элемент 00
        char right = str.charAt(1); //сохраняю второй элемент 00

        Integer hexLetter = 0; //для хранения значения, если элемент это буква (A,B,C,D,E,F)

        if(Character.isDigit(left))
        {

            result[0] = Integer.toBinaryString(Integer.valueOf(String.valueOf(left)));
        }
        else {
            hexLetter = hexNumberToDec(left);
            result[0] = Integer.toBinaryString(hexLetter);
        }

        if(Character.isDigit(right))
        {

            result[1] = Integer.toBinaryString(Integer.valueOf(String.valueOf(right)));
        }
        else {
            hexLetter = hexNumberToDec(right);
            result[1] = Integer.toBinaryString(hexLetter);
        }

        for (int i = 0; i < result.length; i++) {
            if (result[i].length() < row ) {
                for (int x = row - result[i].length(); x != 0; x--) {
                    String s = result[i];
                    result[i] = "0" + s;
                }
            }
        }

        return result[0] + result[1];
    }
}