package fr.fuzeblocks.cconomy_database.Manager.Database;

public class DbCredentials {
    private String host;
    private String user;
    private String pass;
    private String databasename;
    private int port;

    public DbCredentials(String host, String user, String pass, String databasename, int port) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.databasename = databasename;
        this.port = port;
    }
    public String toURI() {
        final StringBuilder builder = new StringBuilder();
        builder.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(databasename);

        return builder.toString();
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getDatabasename() {
        return databasename;
    }

    public int getPort() {
        return port;
    }
}
