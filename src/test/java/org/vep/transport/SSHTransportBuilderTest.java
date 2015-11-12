package org.vep.transport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SSHTransportBuilderTest extends TransportTest {
    @Override
    protected void makeTransportBuilder() {
        SSHTransportBuilder tb = new SSHTransportBuilder("localhost", "guest");
        tb.setAuthByPassword("x220");
        transportBuilder = tb;
    }
}