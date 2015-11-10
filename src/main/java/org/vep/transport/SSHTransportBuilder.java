package org.vep.transport;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Build a Transport over SSH, using JSch.
 *
 * TODO auth method by priority
 *
 * - pub key in preferences
 * - pub key in ~/.ssh/
 * - dialog enter pass
 * - dialog create pub key, save to prefs, send to admin
 *
 */

public class SSHTransportBuilder implements Transport.TransportBuilder {

    public SSHTransportBuilder(SSHConnection connection) {
        this.connection = connection;
        jSch = new JSch();
        tunnels = new ArrayList<Tunnel>();
        logger = Logger.getLogger("org.vep.transport.SSHTransportBuilder");
    }

    public SSHTransportBuilder(String host, String user) {
        this(new SSHConnection(22, host, user));
    }

    public void addTunnel(Tunnel tunnel) {
        tunnels.add(tunnel);
    }

    public void addTunnel(int port) {
        tunnels.add(new Tunnel(port));
    }

    public void setAuthByPassword(String password) {
        auth = new AuthPassword(password);
    }

    public void setAuthByKey(String privateKeyPath, String passphrase) {
        auth = new AuthPubKey(privateKeyPath, passphrase);
    }

    // TODO convenience methods for common cases

    public Transport build() throws TransportException {
        try {
            auth.setupJSch(jSch);
            session = this.jSch.getSession(connection.getUser(), connection.getHost(), connection.getPort());
            auth.setupSession(session);
            session.connect();
            for (Tunnel tunnel : tunnels)
                session.setPortForwardingL(tunnel.getLocalPort(), tunnel.getRemoteHost(), tunnel.getRemotePort());
        } catch (Exception e) {
            String msg = "unable to build SSH transport: " + e;
            logger.warning(msg);
            throw new TransportException();
        }
        return new SSHTransport(this);
    }

    private interface Auth {
        void setupJSch(JSch jSch) throws JSchException;
        void setupSession(Session session);
    }

    private JSch jSch;
    private Session session;
    private Auth auth;
    private SSHConnection connection;
    private List<Tunnel> tunnels;
    private Logger logger;

    public Session getSession() {
        return session;
    }

    // TODO add pub key auth http://www.jcraft.com/jsch/examples/UserAuthPubKey.java.html
    private class AuthPassword implements Auth {

        public AuthPassword(String password) {
            this.password = password;
        }

        public void setupJSch(JSch jSch) throws JSchException {

        }

        public void setupSession(Session session) {
            session.setUserInfo(new UserInfo() {
                public String getPassphrase() {
                    return null;
                }

                public String getPassword() {
                    return password;
                }

                public boolean promptPassword(String s) {
                    return false;
                }

                public boolean promptPassphrase(String s) {
                    return false;
                }

                public boolean promptYesNo(String s) {
                    return false;
                }

                public void showMessage(String s) {

                }
            });
        }

        private final String password;
    }

    private class AuthPubKey implements Auth {
        public AuthPubKey(String privateKeyFileName, String keyPassphrase) {
            this.privateKeyFileName = privateKeyFileName;
            this.keyPassphrase = keyPassphrase;
        }

        public void setupJSch(JSch jSch) throws JSchException {
            jSch.addIdentity(privateKeyFileName);
        }

        private final String privateKeyFileName, keyPassphrase;

        public void setupSession(Session session) {
            session.setUserInfo(new UserInfo() {
                public String getPassphrase() {
                    return keyPassphrase;
                }

                public String getPassword() {
                    return null;
                }

                public boolean promptPassword(String s) {
                    return false;
                }

                public boolean promptPassphrase(String s) {
                    return false;
                }

                public boolean promptYesNo(String s) {
                    return false;
                }

                public void showMessage(String s) {

                }
            });
        }
    }
}