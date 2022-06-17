import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ConnectionThread implements Runnable
{
    /**
     * The underlying incoming socket.
     */
    private final Socket _inputSocket;

    /**
     * The underlying outgoing socket.
     */
    private final Socket _outputSocket;

    /**
     * The input stream of the socket.
     */
    private DataInputStream _socketInputStream;

    /**
     * The output stream of the other socket.
     */
    private DataOutputStream _socketOutputStream;

    private final String _name;

    public ConnectionThread(Socket inSocket, Socket outSocket, String name)
    {
        _inputSocket = inSocket;
        _outputSocket = outSocket;
        _name = name;
    }

    @Override
    public void run()
    {
        Utility.safeDebugPrintln(_name + " thread started on port " + _inputSocket.getLocalPort() + ".");

        try
        {
            // Get send and receive streams
            _socketInputStream = new DataInputStream(_inputSocket.getInputStream());
            _socketOutputStream = new DataOutputStream(_outputSocket.getOutputStream());

            while (!_inputSocket.isClosed())
            {
                // receive packet
                String packet = Utility.receivePacket(_socketInputStream).trim();
                Utility.safePrintln(_name + " sent '" + packet + "'");

                if(packet.equals("dummy,1")){
                    packet = "group,1";
                }
                Utility.safePrintln("MIM" + " sent '" + packet + "'");

                // send
                Utility.sendPacket(_socketOutputStream, packet);
            }
        }
        catch (EOFException e)
        {
            // Socket was closed
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            Utility.safeDebugPrintln("Doing cleanup...");
            try
            {
                // Clean up resources
                _socketInputStream.close();
                _socketOutputStream.close();
                _inputSocket.close();
                _outputSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            Utility.safeDebugPrintln("Cleanup complete.");
        }
    }
}
