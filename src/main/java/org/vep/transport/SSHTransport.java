package org.vep.transport;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Provides secure transport for ports, files, etc over SSH.
 *
 */
public class SSHTransport implements Transport {

    protected SSHTransport(SSHTransportBuilder builder) {
        this.builder = builder;
        this.session = builder.getSession();
        this.logger = Logger.getLogger("org.vep.transport.SSHTransport");
    }

    public void getFile(File remoteFile, File localFile) throws TransportException {
        // TODO
        logger.warning("not implemented");
    }

    public void putFile(File localFile, File remoteFile) throws TransportException {
        // TODO
        logger.warning("not implemented");
    }

    private final SSHTransportBuilder builder;
    private final Session session;
    private final Logger logger;

}
