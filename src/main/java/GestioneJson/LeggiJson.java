package GestioneJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import server.resource.ListPostLight;
import server.resource.ListUserLight;


import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class LeggiJson {
    public static ListUserLight LetturaFileUsers(String pathFile, String nameFile) throws IOException {
        FileInputStream fin = new FileInputStream( pathFile + "/" +nameFile + ".json");
        FileChannel fc = fin.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate((int)fc.size());
        //System.out.println("size del file: "+fc.size());
        int n = fc.read(buffer);
        //System.out.println(buffer.toString());
        buffer.flip();
        String inputString = StandardCharsets.UTF_8.decode(buffer).toString();
        //System.out.println(inputString);
        return JsonString_correctDeserializing_Users(inputString);
    }

    public static ListUserLight JsonString_correctDeserializing_Users(String inputString) {
        Type listOfMyClassObject = new TypeToken<ListUserLight>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(inputString, listOfMyClassObject);
    }

    public static ListPostLight LetturaFilePost(String pathFile, String nameFile) throws IOException {
        FileInputStream fin = new FileInputStream( pathFile + "/" +nameFile + ".json");
        FileChannel fc = fin.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate((int)fc.size());
        //System.out.println("size del file: "+fc.size());
        int n = fc.read(buffer);
        //System.out.println(buffer.toString());
        buffer.flip();
        String inputString = StandardCharsets.UTF_8.decode(buffer).toString();
        //System.out.println(inputString);
        return JsonString_correctDeserializing_Posts(inputString);
    }

    public static ListPostLight JsonString_correctDeserializing_Posts(String inputString) {
        Type listOfMyClassObject = new TypeToken<ListPostLight>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(inputString, listOfMyClassObject);
    }
}
