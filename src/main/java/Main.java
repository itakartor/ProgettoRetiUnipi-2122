import GestioneJson.CreatoreJson;
import client.Client;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        /*ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 1; i++) {
            service.submit(new Client());
        }*/
        String pathFile = "C:\\Users\\Kartor\\IdeaProjects\\ProgettoReti\\documentation";
        String nomeFile = "users";
        CreatoreJson.creazioneFile(pathFile,nomeFile  );
    }
}
