package GestioneJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testcontainers.shaded.com.google.common.io.Files;
import server.resource.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreatoreJson {
    /*private static final int ACCOUNTS = 100;
    private static final int PAYMENTS = 100;
    private static final String[] firstNames = { "Jacob", "Helena", "Marco", "Giulia", "Henry", "Sheldon", "Amy", "Howard", "Martha",
                "Giovanni", "Lisa", "Abigale", "Mohamed", "Salvatore", "Ilaria", "Gianluigi", "Paolo",
                "Davide","Susanna", "Pedro", "Rosa", "Sara"};

    private static final String[] lastNames = {  "Johnson", "Jackson", "Ali", "Rossi", "Bianchi", "Pardini", "Salenti",
                "Portobello","Perez","Sustaita", "Treviso","Emiliano", "Verdi", "De Angeli",
                "Solenni", "Pertini", "Frattani", "Colageri", "Cossu", "Marianelli", "Arga", "Filante" };

    private static final String[] causale = {
            "Bonifico", "Accredito", "Bollettino", "F24", "PagoBancomat"
    };*/

    public static Integer creazioneFile(String pathFile, String nameFile) throws IOException {
        /* restituisco 0 se creo il file
        *  restituisco 1 se il file esiste gia
        *  restituisco -1 se la creazione del file ha avuto un errore
        * */


            //FileOutputStream fout = new FileOutputStream(  nameFile + ".json" );  // creazione file
            //File tempDirectory = new File(path);
        if(pathFile == null || nameFile == null)
        {
            System.out.println("[ERROR]: Configurazione nella Creazione file json nulla");
            return -1;
        }
        File fileWithAbsolutePath = new File(pathFile + "/" +nameFile + ".json");
            // System.out.println(path);

        if(fileWithAbsolutePath.exists()) // controllo se esiste in caso restituisco true
        {
            System.out.println("[SERVER]: File " + nameFile + " gia esistente");
            return 1;
        }
        Files.touch(fileWithAbsolutePath); // creo il file sul disco

        assertTrue(fileWithAbsolutePath.exists()); // controllo se è stato creato il file in caso si arresta

        return 0; // ho creato il file
            /*FileChannel fc = fout.getChannel();

            ByteBuffer buffer = ByteBuffer.allocateDirect(500000);

            Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
            buffer.put("[".getBytes(StandardCharsets.UTF_8));
            for(int i = 0; i < accountNumber; i++) {
                ContoCorrente a = createRandomAccount();
                nMovimentiTot += a.getMovimenti().size();
                buffer.put(gson.toJson(a).getBytes(StandardCharsets.UTF_8));
                if(i < accountNumber-1)
                    buffer.put(",".getBytes(StandardCharsets.UTF_8));
            }
            buffer.put("]".getBytes(StandardCharsets.UTF_8));

            buffer.flip();
            fc.write(buffer);
            buffer.clear();
        System.out.println("questo è il numero dei movimenti totali: " + nMovimentiTot);*/
        }
        public static boolean AggiornametoFIle(String pathFile, String nameFile, Set<User> listUsers) throws IOException {
            if(pathFile == null || nameFile == null)
            {
                System.out.println("[ERROR]: Configurazione nella Creazione file json nulla");
                return false;
            }
            FileOutputStream fout = new FileOutputStream( pathFile + "/" +nameFile + ".json");
            FileChannel fc = fout.getChannel();

            ByteBuffer buffer = ByteBuffer.allocateDirect(500000);

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            buffer.put("[".getBytes(StandardCharsets.UTF_8));

            for(User user : listUsers) {
                buffer.put(gson.toJson(user).getBytes(StandardCharsets.UTF_8));
                /*if(i < userCounter-1)
                    buffer.put(",".getBytes(StandardCharsets.UTF_8));*/
            }
            buffer.put("]".getBytes(StandardCharsets.UTF_8));

            buffer.flip();
            fc.write(buffer);
            buffer.clear();
            return true;
        }

        /*private static ContoCorrente createRandomAccount(){
            Random random = new Random();
            String name = firstNames[random.nextInt(firstNames.length)]
                    + " " +
                    lastNames[random.nextInt(lastNames.length)];

            ContoCorrente a = new ContoCorrente(name, new ArrayList<>());
            int payments = random.nextInt(PAYMENTS);

            for(int i = 0; i < payments; i++){
                String date = (1 + random.nextInt(31)) + "/" + (1 + random.nextInt(12)) + "/200" + random.nextInt(4);
                a.getMovimenti().add(new Movimento(date, causale[random.nextInt(5)]));
            }
            return a;
        }*/

}
