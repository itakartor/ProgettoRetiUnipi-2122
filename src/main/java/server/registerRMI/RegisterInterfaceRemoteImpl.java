package server.registerRMI;

import GestioneJson.CreatoreJson;
import GestioneJson.LeggiJson;
import server.resource.User;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.rmi.RemoteException;
import java.util.*;

public class RegisterInterfaceRemoteImpl implements RegisterInterfaceRemote{

    // queste informazioni potrebbero essere messe nel file config
    String pathFile = "C:\\Users\\Kartor\\IdeaProjects\\ProgettoReti\\documentation";
    String nomeFile = "users";
    //listaUtenti

    private Set<User> listUser;
    private Integer counterUsers;
    public RegisterInterfaceRemoteImpl(Set<User> listUser, Integer counterUsers) throws IOException {
        switch (CreatoreJson.creazioneFile(pathFile,nomeFile))
        {
            case 0:
                System.out.println("[SERVER]: File json è stato creato con successo");
                break;
            case 1:
                System.out.println("[SERVER]: File json gia esistente");
                // devo leggere il file
                this.listUser = LeggiJson.LetturaFile(pathFile,nomeFile).getListUser();
                break;
            case -1:
                System.out.println("[ERROR]: Creazione File json fallita");
                break;
        }
        if(this.listUser != null)
            this.counterUsers =this.listUser.size();//counter degli utenti registrati per generare l'id incrementale degli utenti
        else throw(new IOException("[ERROR]: list user è null"));
    }

    //ricevo tutta la stringa del comando e poi controllo se è composta dai giusti parametri
    @Override
    public boolean register(String comando) throws RemoteException {
        //faccio i controlli se esiste l'username
        // se esiste la password
        // se il numero dei tags è corretto
        String username = null;
        String password = null;
        Set<String> listTags = new HashSet<>();
        boolean ok = true; // all'inizio sono ottimista che il comando si stato scritto bene
        StringTokenizer st = new StringTokenizer(comando);

        if(st.countTokens()>8) // ho troppi argomenti
        {
            System.out.println("[ERROR]: ha messo troppi argomenti");
            System.out.println("[CONSOLE]: min 1 tag and max 5 tag");
            ok = false;
        }else if(st.countTokens()<4)
        {
            System.out.println("[ERROR]: ha messo pochi argomenti");
            ok = false;
        }else if(false)//controlli sulla password se è vuota o se non rispetta dei requisiti minimi
        {               //tipo min 8 caratteri, almeno una lettera maiuscola, almeno una lettera minuscola e almeno un carattere speciale

        }
        if(ok)//in caso che uno dei controlli iniziali non vadano a buon fine non faccio la registrazione
        {
            // parso gli argomenti
            int i = 0;
            String token = st.nextToken();//dovrebbe avere la stringa "register"
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                i++;
                if(i == 1)//prendo l'username e potrei fare il controllo su di esso se esiste gia
                {
                    username = token;
                }
                if(i == 2)//devo fare i controlli del caso
                {
                    password = token;
                }
                if(i>2)//prendo tutti i tags e li metto in minuscolo nella lista
                {
                    listTags.add(token.toLowerCase(Locale.ROOT));
                }
            }
            // ho preso tutti gli argomenti
        }
        else
        {
            System.out.println("[CONSOLE]: usege- register <USERNAME> <PASSWORD> <TAG1> [<TAG2>] [<TAG3>] [<TAG4>] [<TAG5>]");
            return ok;
        }

        // controlli specifici prima di registrare l'utente
        Iterator<User> iterator =this.listUser.iterator(); // controllo che l'username è stato gia utilizzato
        while(iterator.hasNext())
        {
            User u = iterator.next();
            if(u.getUsername().equals(username))
            {
                System.out.println("[ERROR]: Username gia utilizzato");
                ok = false;
                return ok;
            }
        }

        // se tutti i controlli specifici sui campi sono andati a buon fine
        this.counterUsers++;
        User newUser = new User(username,listTags, this.counterUsers);
        this.listUser.add(newUser);
        //va aggiunto l'utente al json
        System.out.println("[SERVER]: Registrazione avvenuta con successo: "+ newUser);
        return ok; // registrazione a buon fine
    }

    public Set<User> getListUser() {
        return listUser;
    }
}
