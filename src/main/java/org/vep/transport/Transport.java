package org.vep.transport;

import java.io.File;

/**
 * The Transport interface defines functionalities required for secure communication with remote server.
 */
public interface Transport {

    interface TransportBuilder {
        void addTunnel(Tunnel tunnel);
        Transport build() throws Exception;
    }

    void putFile(File localFile, File remoteFile) throws Exception;
    void getFile(File remoteFile, File localFile) throws Exception;

    boolean remoteFileExists(File remoteFile) throws Exception;
    
    void close();
}
