package org.verdeterre.bluetooth;

import java.io.*;
import javax.bluetooth.*;
import javax.microedition.io.*;

public class EchoClient {

    private static final String SERVICE_UUID_STRING = "5F6C6A6E1CFA49B49C831E0D1C9B9DC9";
    private static final UUID SERVICE_UUID          = new UUID(SERVICE_UUID_STRING, false);

    private LocalDevice localDevice;
    private DiscoveryAgent discoveryAgent;

    public void init() throws BluetoothStateException {
        localDevice = LocalDevice.getLocalDevice();
        discoveryAgent = localDevice.getDiscoveryAgent();
    }

    public void connect() throws IOException {
        String connectionUrl = discoveryAgent.selectService(SERVICE_UUID, ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
        System.out.println("Connecting to " + connectionUrl);

        StreamConnection sc = (StreamConnection) Connector.open(connectionUrl);

        RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(sc);
        String remoteAddress = remoteDevice.getBluetoothAddress();

        String remoteName = "???";
        try {
            remoteName = remoteDevice.getFriendlyName(false);
        }
        catch (IOException e) {
            System.err.println("Unable to get remote device name");
        }

        System.out.println("Connection opened to " + remoteAddress);

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader reader = new BufferedReader(new InputStreamReader(sc.openDataInputStream()));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(sc.openDataOutputStream()));
       
        String line;
        while ((line = consoleReader.readLine()) != null) {
            writer.println(line);
            writer.flush();
            line = reader.readLine();
            System.out.println(remoteName + " (" + remoteAddress + "): " + line);
        }

        sc.close();
    }

    public static void main(String[] args) throws BluetoothStateException, IOException, InterruptedException {
        EchoClient client = new EchoClient();
        client.init();
        client.connect();
    }

}
