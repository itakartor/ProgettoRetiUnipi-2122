package GestioneJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.resource.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LeggiJson {
    public static Set<User> LetturaFile(String pathFile,String nameFile) throws IOException {
        FileInputStream fin = new FileInputStream( pathFile + "/" +nameFile + ".json");
        FileChannel fc = fin.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate((int)fc.size());
        //System.out.println("size del file: "+fc.size());
        int n = fc.read(buffer);
        //System.out.println(buffer.toString());
        buffer.flip();
        String inputString = StandardCharsets.UTF_8.decode(buffer).toString();
        //System.out.println(inputString);
        return JsonString_correctDeserializing(inputString);
    }

    public static Set<User> JsonString_correctDeserializing(String inputString) {
        Type listOfMyClassObject = new TypeToken<ArrayList<User>>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(inputString, listOfMyClassObject);
    }
}
