import GestioneJson.CreatoreJson;
import GestioneJson.LeggiJson;
import client.Client;
import server.resource.ListUser;
import server.resource.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        /*ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 1; i++) {
            service.submit(new Client());
        }
        ListUser listUser = null;
        String pathFile = "C:\\Users\\Kartor\\IdeaProjects\\ProgettoReti\\documentation";
        String nomeFile = "users";
        CreatoreJson.creazioneFile(pathFile,nomeFile);
        Set<String> listTags = new HashSet<>();
        listTags.add("sport");
        listTags.add("video");
        listTags.add("game");
        User user1 = new User("kartor1",listTags,1);
        User user2 = new User("kartor2",listTags,2);
        User user3 = new User("kartor3",listTags,3);
        User user4 = new User("kartor4",listTags,4);
        User user5 = new User("kartor5",listTags,5);
        Set<User> listUserinUser = new HashSet<>();
        listUserinUser.add(user1);
        listUserinUser.add(user2);
        listUserinUser.add(user3);
        listUserinUser.add(user4);
        listUserinUser.add(user5);

        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        listUser = new ListUser(formatDate.format(new Date()),listUserinUser);

        CreatoreJson.AggiornametoFileUsers(pathFile,nomeFile,listUserinUser);
        ListUser listUser1 = null;
        String newDate = "30/12/2021";

        listUser1 = LeggiJson.LetturaFile(pathFile,nomeFile);
        if(listUser1.getListUser() != null)
            for(User u : listUser1.getListUser())
            {
                System.out.println(u.toString());
            }
        System.out.println(listUser1.getTimeStamp());

        if(formatDate.parse(newDate).after(formatDate.parse(listUser1.getTimeStamp())))
        {
            System.out.println(newDate+" is after " + listUser1.getTimeStamp() );
        }
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); // per richiedere all'utente un comando da mandare al server

        String message = null; // messaggio che poi viene letto dall'stdin

        StringBuilder response = new StringBuilder();// uso la stringbuilder così da poterci fare l'append
        System.out.println("[CONSOLE]: Inserisci un comando");
        message = input.readLine(); // leggo qualcosa da tastiera
        String support = null;
        StringTokenizer st = new StringTokenizer(message);
        while(st.hasMoreTokens())
        {
            support = st.nextToken();
            System.out.println(support);
        }

        Set<String> listTags = new HashSet<>();
        listTags.add("sport");
        listTags.add("video");
        listTags.add("game");
        User user1 = new User("kartor1",listTags,1,"hash","seed");

        Map<String,User> map = new HashMap<>();
        map.put("ciao",null);
        for (Map.Entry<String, User> entry : map.entrySet()) { // controllo se trovo lo stesso utente loggato su un altro client
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        map.put("ciao",user1);
        for (Map.Entry<String, User> entry : map.entrySet()) { // controllo se trovo lo stesso utente loggato su un altro client
            System.out.println(entry.getKey() + " " + entry.getValue());
        }*/
    }
}
