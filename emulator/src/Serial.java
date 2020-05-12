import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Serial
{
    static Enumeration<CommPortIdentifier> portList;
    static CommPortIdentifier portId;
    static SerialPort serialPort;
    static OutputStream outputStream;
    static boolean outputBufferEmptyFlag = false;

    static OutputStream out;

    public static class SerialReader implements SerialPortEventListener
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        private UB2State ub2State;

        public SerialReader(InputStream in, UB2State ub2State)
        {           
        	this.ub2State = ub2State;
            this.in = in;           
        }

        @Override
        public void serialEvent(SerialPortEvent ev)
        {
            int data;

            try
            {
                int len = 0;
                while ((data = in.read()) > -1)
                {
                    buffer[len++] = (byte) data;
                    //System.out.println (data);
                    this.ub2State.serialReadCallback (data);
                }
                //System.out.println (new String (buffer, 0, len));
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.exit(-1);
            }
        }       
    }

    public void write (int i)
    {
		byte b;

		try
		{
			if (i < 0 || i > 255)
			{
				System.out.println ("Input number from 0 to 255");
				return;
			}
			b = (byte) i;
			this.out.write (b);
			//System.out.println ("this.out.write(" + b + ")");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
    }

    public Serial (UB2State ub2State)
    {
    	CommPortIdentifier portIdentifier;
    	String portName = "COM3";

        try
        {
            portIdentifier = CommPortIdentifier.getPortIdentifier (portName);

            if (portIdentifier.isCurrentlyOwned ())
            {
                System.err.println ("error: port is currently in use");
                return;
            }

            SerialPort sport = (SerialPort) portIdentifier.open (portName, 3000);
            sport.setSerialPortParams (57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            InputStream in = sport.getInputStream ();
            sport.addEventListener (new SerialReader (in, ub2State));
            sport.notifyOnDataAvailable (true);

            this.out = sport.getOutputStream ();         
        }
        catch (NoSuchPortException e)
        {
            e.printStackTrace();
            return;
        } 
        catch (PortInUseException e)
        {
            e.printStackTrace();
            return;
        }
        catch (UnsupportedCommOperationException e)
        {
            e.printStackTrace();
            return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }        
        catch (TooManyListenersException e)
        {
            e.printStackTrace();
            return;
        }
    }
}