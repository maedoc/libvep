import org.vep.DAO;
import org.vep.transport.SSHTransportBuilder;
import org.vep.transport.Transport;
import org.vep.transport.Tunnel;

import java.util.logging.Logger;

/**
 * A convenience class for MATLAB.
 */
public final class VEP {

    public final Transport transport;
    public final DAO dao;

    public VEP(String host, String user, String password) throws Exception
    {
        logger = Logger.getLogger(this.getClass().getName());

        logger.info("connecting via SSH " + user + "@" + host);
        SSHTransportBuilder tb = new SSHTransportBuilder(host, user);
        tb.setAuthByPassword(password);

        // tunnel PG
        tb.addTunnel(new Tunnel(5432));

        transport = tb.build();

        logger.info("building the DAO");
        try {
            dao = new org.vep.DAO();
        } catch (Exception e) {
            logger.warning("creating DAO failed, closing transport");
            transport.close();
            throw e;
        }

        logger.info("ready");
    }

    public void close()
    {
        dao.close();
        dao.closeEMF();
        transport.close();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    private final Logger logger;
}
