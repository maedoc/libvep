package org.vep.transport;

/**
 * Created by mw on 11/10/15.
 */

public class SSHConnection {
    private final int port;
    private final String host, user;

    public SSHConnection(int port, String host, String user) {
        this.port = port;
        this.host = host;
        this.user = user;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }
}

