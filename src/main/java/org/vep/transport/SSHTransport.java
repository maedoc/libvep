package org.vep.transport;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

import java.io.*;
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

    public void close() {
        session.disconnect();
    }

    public void getFile(File remoteFile, File localFile) throws Exception {
        Channel channel = session.openChannel("exec");
        String command = "scp -f " + remoteFile.getAbsolutePath();
        System.out.println("command = '" + command + "'");
        ((ChannelExec) channel).setCommand(command);
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();
        channel.connect();

        sendNull(out);

        byte[] buf = new byte[1024];
        while (true) {
            int c = scpOK(in);
            if (c != 'C')
                break;

            assert in.read(buf, 0, 5) == 5;

            long fileSize = 0L;
            while (true) {
                if (in.read(buf, 0, 1) < 0)
                    break; // error
                if (buf[0] == ' ')
                    break;
                fileSize = fileSize * 10L + (long) (buf[0] - '0');
            }

            String fileName;
            for (int i=0; ; i++) {
                assert in.read(buf, i, 1) == 1;
                if (buf[i] == (byte) 0x0a) {
                    fileName = new String(buf, 0, i);
                    break;
                }
            }

            logger.info("fileSize = " + fileSize + ", fileName = " + fileName);

            sendNull(out);

            {
                FileOutputStream fos = new FileOutputStream(localFile.getAbsoluteFile());
                int nb;
                long bytesLeft = fileSize;
                while (true) {
                    if (buf.length < fileSize)
                        nb = buf.length;
                    else
                        nb = (int) fileSize;
                    nb = in.read(buf, 0, nb);
                    if (nb < 0)
                        throw new IOException("reading from remote failed");
                    fos.write(buf, 0, nb);
                    bytesLeft -= nb;
                    if (bytesLeft == 0L)
                        break;
                }
                fos.close();
            }
            scpOK(in);
            sendNull(out);
            channel.disconnect();
        }
    }

    static void sendNull(OutputStream os, boolean andFlush) throws IOException {
        byte[] b = new byte[1];
        b[0] = 0;
        os.write(b, 0, 1);
        if (andFlush)
            os.flush();
    }

    static void sendNull(OutputStream os) throws IOException {
        sendNull(os, true);
    }

    /**
     * Copy a local file to remote.
     *
     * - This executes `cat - >> /path/to/remote/file` on the remote server
     * - Based on http://www.jcraft.com/jsch/examples/ScpTo.java.html
     *
     * @param localFile File corresponding to local file
     * @param remoteFile File corresponding to remote file to create or overwrite
     * @throws Exception
     */
    public void putFile(File localFile, File remoteFile) throws Exception {
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand("scp -t " + remoteFile.getAbsolutePath());
        OutputStream out = channel.getOutputStream();
        ((ChannelExec) channel).setErrStream(System.err);
        InputStream in = channel.getInputStream();
        channel.connect();

        out.write(("C0644 " + localFile.length() + " "  + localFile.getName() + "\n").getBytes());
        out.flush();
        scpOK(in);
        {
            byte[] buf = new byte[1024];
            {
                FileInputStream fis = new FileInputStream(localFile);
                while (true) {
                    int nb = fis.read(buf, 0, buf.length);
                    if (nb < 1)
                        break;
                    out.write(buf, 0, nb);
                }
                fis.close();
            }
            buf[0] = 0;
            out.write(buf, 0, 1);
        }
        out.flush();
        scpOK(in);
        out.close();
        channel.disconnect();
    }

    int scpOK(InputStream in) throws IOException {
        int b = in.read();
        if (b==1 || b==2) {
            logger.warning("SCP returned code " + b);
            StringBuilder buf = new StringBuilder();
            int c;
            do {
                c = in.read();
                buf.append((char) c);
            } while(c != '\n');
            throw new IOException(buf.toString());
        }
        logger.info("SCP returned code " + b);
        return b;
    }

    /**
     * Check whether a file exists on the remote.
     *
     * - This executes `stat` on the remote and checks exit code
     * - Base on JSch example http://www.jcraft.com/jsch/examples/Exec.java.html
     *
     * @param remoteFile File with path to remote file to check for existence
     * @return true if the file exists, false otherwise
     * @throws Exception
     */
    public boolean remoteFileExists(File remoteFile) throws Exception {
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand("stat " + remoteFile.getAbsolutePath());
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        InputStream in = channel.getInputStream();
        channel.connect();

        byte[] buf = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(buf, 0, 1024);
                if (i < 0)
                    break;
                // System.out.print(new String(buf, 0, i));
            }
            if (channel.isClosed()) {
                if (in.available() > 0)
                    continue;
                // System.out.println("exit with " + channel.getExitStatus());
                break;
            }
            Thread.sleep(100);
        }
        boolean exists = channel.getExitStatus() == 0;
        channel.disconnect();
        return exists;
    }
    // TODO this could probable be generalized to a remote process

    private final SSHTransportBuilder builder;
    private final Session session;
    private final Logger logger;

}
