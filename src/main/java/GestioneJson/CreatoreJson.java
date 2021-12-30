package GestioneJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testcontainers.shaded.com.google.common.io.Files;
import server.resource.*;

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
    public static boolean AggiornametoFileUsers(String pathFile, String nameFile, Set<User> listUser) throws IOException {
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

            ListUserLight listUserLight = new ListUserLight(new SimpleDateFormat("dd/MM/yyyy").format(new Date()),listUser);

            buffer.put(gson.toJson(listUserLight).getBytes(StandardCharsets.UTF_8));

            buffer.flip();
            fc.write(buffer);
            buffer.clear();
            return true;
    }
    public static boolean AggiornametoFilePost(String pathFile, String nameFile, Set<Post> listPost) throws IOException {
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

        ListPostLight listPostLight = new ListPostLight(listPost,new SimpleDateFormat("dd/MM/yyyy").format(new Date()));


        buffer.put(gson.toJson(listPostLight).getBytes(StandardCharsets.UTF_8));

        buffer.flip();
        fc.write(buffer);
        buffer.clear();
        return true;
    }

}
