import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Helper class containing auxiliary functions.
 */
public class Utility
{
    public static void safePrintln(String message)
    {
        synchronized (System.out)
        {
            System.out.println(message);
        }
    }

    public static void safeDebugPrintln(String message)
    {
        synchronized (System.err)
        {
            System.err.println(message);
        }
    }

    public static void sendPacket(DataOutputStream outputStream, String payload) throws IOException
    {
        // Debug output
        safeDebugPrintln("Sending '" + payload + "'");

        // Encode payload
        byte[] payloadEncoded = payload.getBytes();

        // Write packet length
        outputStream.writeInt(payloadEncoded.length);

        // Write payload
        outputStream.write(payloadEncoded);
    }

    public static String receivePacket(DataInputStream inputStream) throws IOException
    {
        // Prepare payload buffer
        byte[] payloadEncoded = new byte[inputStream.readInt()];
        inputStream.readFully(payloadEncoded);

        // Decode payload
        String payload = new String(payloadEncoded);
        safeDebugPrintln("Received '" + payload + "'");
        return payload;
    }
}
