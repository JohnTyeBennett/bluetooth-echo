package org.verdeterre.bluetooth;

import javax.bluetooth.*;
import javax.microedition.io.*;
import java.io.*;

public class EchoServer {
    
    private static final String SERVICE_NAME        = "EchoServer";
    private static final String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC9";
    private static final UUID SERVICE_UUID          = new UUID(SERVICE_UUID_STRING, false);

    private LocalDevice localDevice;

    public void init() throws BluetoothStateException {
        localDevice = LocalDevice.getLocalDevice();
    }

    public void start() throws BluetoothStateException, IOException {
        String connUrl = "btspp://localhost:" + SERVICE_UUID_STRING + ";" + "name=" + SERVICE_NAME;
        StreamConnectionNotifier scn = (StreamConnectionNotifier) Connector.open(connUrl);

        System.out.println("Ready to accept connections");

        while (true) {
            StreamConnection sc = scn.acceptAndOpen();

            RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(sc);
            String remoteAddress = remoteDevice.getBluetoothAddress();

            System.out.println("Connection from " + remoteAddress);

            String remoteName = "???";
            try {
                remoteName = remoteDevice.getFriendlyName(false);
            }
            catch (IOException e) {
                System.err.println("Unable to get remote device name");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(sc.openDataInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(sc.openDataOutputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(remoteName + " (" + remoteAddress + "): " + line);
                writer.println(line);
                writer.flush();
            }

            sc.close();

            System.out.println("Connection from " + remoteAddress + " closed");
        }
    }

    public static void main(String[] args) throws BluetoothStateException, IOException {
        EchoServer server = new EchoServer();
        server.init();
        server.start();
    }

}
