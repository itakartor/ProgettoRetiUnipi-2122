import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
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
/*
        String file ="C:\\Users\\Kartor\\IdeaProjects\\ProgettoReti\\src\\main\\java\\config\\serverConfig.txt";


        List<String> a = UtilFile.readAllLine(file);
        List<String> filteredList = UtilFile.filterLine(a);
        filteredList.forEach(System.out::println);


        ConfigField configField = UtilFile.readConfigurationServer(file);
        System.out.println(configField.toString());*/

        /*String result = " Utenti     |        Tags   \n---------------------------\n";
        System.out.print(result);*/
        /*Scanner key = new Scanner(System.in);
        String input = key.nextLine();
        StringTokenizer st = new StringTokenizer(input, "\"");
        while(st.hasMoreTokens())
        {
            System.out.println(st.nextToken());
        } // post "new post incoming" "content is good"*/

        // ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>();

        /*Set <Integer> penNameSet = ConcurrentHashMap.newKeySet();
        penNameSet.add(0);*/
        /*ReentrantLock lock = new ReentrantLock();
        Set<Integer> listInt = new HashSet<>();//Collections.synchronizedSet(new HashSet<>());
        listInt.add(0);
        ListNuovo listNuovo = new ListNuovo(lock);
        Thread t1 = new Thread(new TaskP(listInt));
        Thread t2 = new Thread(new TaskP(listInt));
        t1.start();
        t2.start();

        Thread.sleep(11000);
        System.out.println(listInt);*/

    }
}
/*

class TaskP implements Runnable{
    private Set<Integer>  listInteger;
    public TaskP(Set<Integer> listInteger) {
        this.listInteger = listInteger;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            Integer var = listInteger.stream().max(Integer::compare).get() + (int)Thread.currentThread().getId();
            var++;
            System.out.println(Thread.currentThread().getName()+" " + var);
            listInteger.add(var);
            System.out.println(Thread.currentThread().getName()+ " " + listInteger);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println(listInteger);
    }
}


class TaskP2 implements Runnable{
    public static ListNuovo listNuovo;
    public TaskP2(ListNuovo listNuovo) {
        TaskP2.listNuovo = listNuovo;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            listNuovo.getLock().lock();
            Integer var = listNuovo.getPenNameSet().stream().min(Integer::compare).get() + i;
            var++;
            System.out.println(Thread.currentThread().getName()+" " + var);
            listNuovo.getPenNameSet().add(var);
            System.out.println(Thread.currentThread().getName()+ " " + listNuovo.getPenNameSet());
            try {
                listNuovo.getLock().unlock();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println(listNuovo.getPenNameSet());
    }
}
class ListNuovo{
    public Set <Integer> penNameSet;
    public ReentrantLock lock;

    public ListNuovo(ReentrantLock lock) {
        this.penNameSet = new HashSet<>();
        this.penNameSet.add(0);
        this.lock = lock;
    }

    public Set<Integer> getPenNameSet() {
        return penNameSet;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}*/
