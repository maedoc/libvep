package org.vep.transport;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.*;

@Ignore
public abstract class TransportTest {
    protected Transport transport;
    protected Transport.TransportBuilder transportBuilder;
    protected Tunnel tunnel;
    
    protected abstract void makeTransportBuilder();

    @Before
    public void setUp() throws Exception {
        tunnel = new Tunnel(Tunnel.Direction.Forward, 4242, 4243, "localhost");
        makeTransportBuilder();
        transportBuilder.addTunnel(tunnel);

        /* if transport isn't available, don't run the tests */
        boolean available = true;
        try {
            transport = transportBuilder.build();
        } catch (Exception exc) {
            available = false;
        }
        Assume.assumeTrue("transport available", available);
    }

    @After
    public void tearDown() throws Exception {
        transport.close();
    }

    /**
     * Test bidirectional communication over forward tunnel.
     *
     * @throws Exception
     */
    @Test
    public void testForwardTunnel() throws Exception {
        ServerSocket remoteServerSocket = new ServerSocket(tunnel.getRemotePort());
        Socket localClientSocket = new Socket("localhost", tunnel.getLocalPort());
        Socket remoteClientSocket = remoteServerSocket.accept();

        OutputStream remoteClientOutput = remoteClientSocket.getOutputStream();
        InputStream remoteClientInput = remoteClientSocket.getInputStream();
        OutputStream localClientOutput = localClientSocket.getOutputStream();
        InputStream localClientInput = localClientSocket.getInputStream();

        for (int i=0; i<100; i++) {
            localClientOutput.write(i);
            remoteClientOutput.write(remoteClientInput.read());
            Assert.assertEquals(i, localClientInput.read());
        }

        localClientSocket.close();
        remoteClientSocket.close();
        remoteServerSocket.close();
    }

    /**
     * Test that remote files exist or not.
     *
     * @throws Exception
     */
    @Test
    public void testRemoteFileExists() throws Exception {
        File home = new File("/home"), foo = new File("/foo");
        Assert.assertTrue(
                home.getAbsolutePath() + " exists on remote",
                transport.remoteFileExists(home));
        Assert.assertTrue(
                foo.getAbsolutePath() + " does not exist on remote",
                !transport.remoteFileExists(foo));
    }

    /**
     * Test putting files under the assumption that /public is an accessible place for both
     * the user running the test, and the guest user on localhost.
     *
     * @throws Exception
     */
    @Test
    public void testPutFile() throws Exception {
        File localFile, remoteFile;
        localFile = new File("/public/foo");
        remoteFile = new File("/public/bar");
        if (remoteFile.exists())
            if (!remoteFile.delete())
                throw new IOException("could not delete test file");
        transport.putFile(localFile, remoteFile);
        Thread.sleep(100);
        assertFilesIdentical(localFile, remoteFile);
    }

    /**
     * Test getting files under the assumption that /public is an accessible place for both
     * the user running the test, and the guest user on localhost.
     *
     * @throws Exception
     */
    @Test
    public void testGetFile() throws Exception {
        File localFile, remoteFile;
        remoteFile = new File("/public/foo");
        localFile = new File("/public/bar");
        if (localFile.exists())
            if (!localFile.delete())
                throw new IOException("could not delete test file");
        transport.getFile(remoteFile, localFile);
        Thread.sleep(100);
        assertFilesIdentical(localFile, remoteFile);
    }

    /**
     * Asserts that two files exist and are byte-wise equivalent
     * @param file1 first file to compare
     * @param file2 second file to compare
     * @throws Exception
     */
    void assertFilesIdentical(File file1, File file2) throws Exception {

        // this asserts the file was created
        file2 = new File(file2.getAbsolutePath());
        Assert.assertTrue(file2.exists());

        // now assert contents are byte-wise identical
        FileInputStream localFis, remoteFis;
        localFis = new FileInputStream(file1);
        remoteFis = new FileInputStream(file2);

        byte[] localBuf = new byte[32], remoteBuf = new byte[32];
        while (true) {
            int ln = localFis.read(localBuf, 0, localBuf.length);
            int rn = remoteFis.read(remoteBuf, 0, remoteBuf.length);
            Assert.assertEquals(ln, rn);
            Assert.assertArrayEquals(localBuf, remoteBuf);
            if (ln < 1)
                break;
        }
        remoteFis.close();
        localFis.close();
    }


}
