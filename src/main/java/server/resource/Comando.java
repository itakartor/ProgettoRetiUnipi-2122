package server.resource;

import java.util.Set;

public class Comando {
    private final String idClient;
    private final String comando;
    /*private String username;
    private String password;
    private Set<String> tags;
    private id*/

    public Comando(String idClient, String comando) {
        this.idClient = idClient;
        this.comando = comando;
    }

    public String getIdClient() {
        return idClient;
    }

    public String getComando() {
        return comando;
    }
}
