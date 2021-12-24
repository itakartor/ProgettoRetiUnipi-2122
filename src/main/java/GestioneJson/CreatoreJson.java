package GestioneJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testcontainers.shaded.com.google.common.io.Files;
import server.resource.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreatoreJson {

    public static Integer creazioneFile(String pathFile, String nameFile) throws IOException {
        /* restituisco 0 se creo il file
         *  restituisco 1 se il file esiste gia
         *  restituisco -1 se la creazione del file ha avuto un errore
         * */

        if (pathFile == null || nameFile == null) {
            System.out.println("[ERROR]: Configurazione nella Creazione file json nulla");
            return -1;
        }
        File fileWithAbsolutePath = new File(pathFile + "/" + nameFile + ".json");
        // System.out.println(path);

        if (fileWithAbsolutePath.exists()) // controllo se esiste in caso restituisco true
        {
            System.out.println("[SERVER]: File " + nameFile + " gia esistente");
            return 1;
        }
        Files.touch(fileWithAbsolutePath); // creo il file sul disco

        assertTrue(fileWithAbsolutePath.exists()); // controllo se è stato creato il file in caso si arresta

        return 0; // ho creato il file
    }
        public static boolean AggiornametoFileUsers(String pathFile, String nameFile, Set<User> listUsers) throws IOException {
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
            /*
            * {
            *   [
            *       {
            *           infoUtente1
            *       },
            *       {
             *           infoUtente2
             *      }
            *   ],
            *   timeStamp
            * }*/
            buffer.put("{".getBytes(StandardCharsets.UTF_8));

            buffer.put("\"listUsers\":[".getBytes(StandardCharsets.UTF_8));
            int i = 0;
            for(User user : listUsers) {
                i++;
                buffer.put(gson.toJson(user).getBytes(StandardCharsets.UTF_8));
                if(i < listUsers.size())
                    buffer.put(",".getBytes(StandardCharsets.UTF_8));
            }
            buffer.put("],".getBytes(StandardCharsets.UTF_8));

            buffer.put("\"timeStamp\":".getBytes(StandardCharsets.UTF_8));
            buffer.put("\"".getBytes(StandardCharsets.UTF_8));
            buffer.put(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).getBytes(StandardCharsets.UTF_8));
            buffer.put("\"".getBytes(StandardCharsets.UTF_8));
            buffer.put("}".getBytes(StandardCharsets.UTF_8));

            buffer.flip();
            fc.write(buffer);
            buffer.clear();
            return true;
        }

}
