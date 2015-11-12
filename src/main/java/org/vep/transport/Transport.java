package org.vep.transport;

import java.io.File;

/**
 * The Transport interface allows for communication with a remote computer by tunneling
 * ports and copying files.
 *
 */
public interface Transport {

    /**
     * A TransportBuilder is used to construct a transport.
     */
    interface TransportBuilder {

        /**
         * Add a tunnel to be set up when transport is built.
         * @param tunnel Tunnel describing port, direction and remote host.
         */
        void addTunnel(Tunnel tunnel);

        /**
         * Build a Transport instance according to builder properties.
         *
         * On successfully returning a Transport instance, the transport is setup and connected, so
         * any tunnels added prior to a call to build are then available, and the Transport
         * interface methods should succeed modulo network interruptions.
         */
        Transport build() throws Exception;
    }

    /**
     * Copy a local file to the remote computer.
     *
     * @param localFile File describing local file to copy
     * @param remoteFile File describing the remote file to create or overwrite
     * @throws Exception
     */
    void putFile(File localFile, File remoteFile) throws Exception;

    /**
     * Copy a remote file to local computer.
     *
     * @param remoteFile File describing remote file to copy
     * @param localFile File describing local file to create or overwrite
     * @throws Exception
     */
    void getFile(File remoteFile, File localFile) throws Exception;

    /**
     * Check the existence of a remote file.
     *
     * @param remoteFile File on remote computer to check existence of
     * @return true if file exists on remote, otherwise false
     * @throws Exception
     */
    boolean remoteFileExists(File remoteFile) throws Exception;

    /**
     * Close the transport for use, freeing resources such as network connections.
     *
     * None of the Transport interface methods should be called after a call to close.
     */
    void close();
}
