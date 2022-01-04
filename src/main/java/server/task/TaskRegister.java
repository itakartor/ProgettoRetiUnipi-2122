package server.task;

import server.resource.ListUser;
import server.resource.User;
import util.Generators;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

public class TaskRegister implements Callable<Boolean> {
    private final String comando;
    private final ListUser listUser;
    /*private final ReentrantLock lock;*/

    public TaskRegister(String comando, ListUser listUser) {
        this.comando = comando;
        this.listUser = listUser;
        /*this.lock = this.listUser.getLock();*/
    }

    @Override // in caso che la lista sia null per vari problemi di file andrebbe cancellato il file e ricreato, ma non so se sia possibile
    public Boolean call() {
        // faccio i controlli se esiste l'username
        // se esiste la password
        // se il numero dei tags è corretto
        String username = null;
        String password = null;
        Set<String> listTags = new HashSet<>();
        boolean ok = true; // all'inizio sono ottimista che il comando si stato scritto bene
        StringTokenizer st = new StringTokenizer(comando);

        // controlli preliminari
        if(st.countTokens()>8) // ho troppi argomenti
        {
            System.out.println("[ERROR]: ha messo troppi argomenti");
            System.out.println("[CONSOLE]: min 1 tag and max 5 tag");
            ok = false;
        }else if(st.countTokens()<4)
        {
            System.out.println("[ERROR]: ha messo pochi argomenti");
            ok = false;
        }
        if(ok)// in caso che uno dei controlli iniziali non vadano a buon fine non faccio la registrazione
        {
            // parso gli argomenti e li controllo
            int i = 0;
            String token = st.nextToken();// dovrebbe avere la stringa "register"
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                i++;
                if(i == 1)// prendo l'username e potrei fare il controllo su di esso se esiste gia
                {
                    username = token;
                }
                if(i == 2)// devo fare i controlli del caso
                {
                    password = token;
                }
                if(i>2)// prendo tutti i tags e li metto in minuscolo nella lista
                {
                    listTags.add(token.toLowerCase(Locale.ROOT));
                }
            }
            // ho preso tutti gli argomenti

            // se tutti i controlli specifici sui campi sono andati a buon fine

            /*this.lock.lock();*/
            for(User u : this.listUser.getListUser()) // controllo se l'utente è gia registato
            {
                if (u.getUsername().equals(username)) {
                    ok = false;
                    break;
                }
            }
            if(password != null)
            {
                if(password.length()<6) // controllo i requisiti minimi
                {
                    ok = false;
                }
            }

            /*if(!ok)
                this.lock.unlock();*/
        }
        if(ok)
        {
            String seed = Generators.getRandomString();
            User newUser = new User(username,listTags, this.listUser.getListUser().size()+1, Generators.sha256(password + seed), seed);
            this.listUser.addUser(newUser);
            this.listUser.setModified(true); // dico al sistema che ho modificato la lista degli utenti
            System.out.println("[SERVER]: Registrazione avvenuta con successo: "+ newUser);
            /*this.lock.unlock();*/
        }
        else
            System.out.println("[CONSOLE]: usage- register <USERNAME> <PASSWORD> <TAG1> [<TAG2>] [<TAG3>] [<TAG4>] [<TAG5>]");

        return ok; // registrazione a buon fine
    }
}
