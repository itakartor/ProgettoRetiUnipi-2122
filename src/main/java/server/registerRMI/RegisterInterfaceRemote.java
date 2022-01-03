package server.registerRMI;

import java.io.IOException;
import java.rmi.Remote;
import java.util.concurrent.ExecutionException;

public interface RegisterInterfaceRemote extends Remote {
    boolean register(String comando) throws IOException, ExecutionException, InterruptedException;
}
