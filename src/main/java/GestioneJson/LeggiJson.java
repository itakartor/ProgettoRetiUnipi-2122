package GestioneJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import server.resource.ListPost;
import server.resource.ListUser;
import server.resource.ListWallet;


import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class LeggiJson {
    public static ListUser LetturaFileUsers(String pathFile, String nameFile) throws IOException {
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

    public static ListUser JsonString_correctDeserializing_Users(String inputString) {
        Type listOfMyClassObject = new TypeToken<ListUser>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(inputString, listOfMyClassObject);
    }

    public static ListPost LetturaFilePost(String pathFile, String nameFile) throws IOException {
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

    public static ListPost JsonString_correctDeserializing_Posts(String inputString) {
        Type listOfMyClassObject = new TypeToken<ListPost>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(inputString, listOfMyClassObject);
    }

    public static ListWallet LetturaFileWallets(String pathFile, String nameFile) throws IOException {
        FileInputStream fin = new FileInputStream( pathFile + "/" +nameFile + ".json");
        FileChannel fc = fin.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate((int)fc.size());
        //System.out.println("size del file: "+fc.size());
        int n = fc.read(buffer);
        //System.out.println(buffer.toString());
        buffer.flip();
        String inputString = StandardCharsets.UTF_8.decode(buffer).toString();
        //System.out.println(inputString);
        return JsonString_correctDeserializing_Wallets(inputString);
    }

    public static ListWallet JsonString_correctDeserializing_Wallets(String inputString) {
        Type listOfMyClassObject = new TypeToken<ListWallet>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(inputString, listOfMyClassObject);
    }
}
