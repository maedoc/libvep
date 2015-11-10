package org.vep.transport;

/**
 * Created by mw on 11/10/15.
 */
class Tunnel {

    public enum Direction { Forward, Reverse };

    private final Direction direction;
    private final int localPort, remotePort;
    private final String remoteHost;

    public Tunnel(Direction direction, int localPort, int remotePort, String remoteHost) {
        this.direction = direction;
        this.localPort = localPort;
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
    }

    /**
     * Convenience constructor for port forwarding handling the most common case of forwarding local port to same
     * port on remote localhost.
     *
     * @param port Port number to tunnel from local to remote localhost.
     */
    public Tunnel(int port) {
        this(Direction.Forward, port, port, "localhost");
    }

    public Direction getDirection() {
        return direction;
    }

    public int getLocalPort() {
        return localPort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }
}
