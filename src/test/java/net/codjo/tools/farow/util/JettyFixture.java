package net.codjo.tools.farow.util;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.codjo.test.common.fixture.Fixture;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;

public abstract class JettyFixture implements Fixture {
    private Server server;
    private int webPort;


    protected JettyFixture(int webPort) {
        this.webPort = webPort;
    }


    public void doSetUp() throws Exception {
        server = buildJettyServer(webPort);
        server.start();
    }


    public void doTearDown() throws Exception {
        server.stop();
        server.join();
    }


    public String decode(HttpServletRequest httpServletRequest) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = httpServletRequest.getReader();
        String line;
        do {
            line = reader.readLine();
            if (line != null) {
                sb.append(line);
            }
        }
        while (line != null);
        return sb.toString();
    }


    protected Server getServer() {
        return server;
    }


    protected abstract void handleHttpRequest(String target, HttpServletRequest request, HttpServletResponse response);


    private void addSecurityContext(Server jettyServer, String[] roles, String realmPath) throws IOException {
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(roles);
        constraint.setAuthenticate(true);

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("/*");

        SecurityHandler sh = new SecurityHandler();
        HashUserRealm myRealm = new HashUserRealm("MyRealm", realmPath);
        sh.setUserRealm(myRealm);
        sh.setConstraintMappings(new ConstraintMapping[]{cm});

        jettyServer.addHandler(sh);
    }


    private Server buildJettyServer(int port) throws IOException {
        Server newServer = new Server();
        SocketConnector connector = new SocketConnector();

        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(port);
        newServer.setConnectors(new Connector[]{connector});

        String[] roles = getRoles();
        if (roles != null && roles.length != 0) {
            addSecurityContext(newServer, roles,
                               getClass().getResource(getRealmPropertyFile()).getPath());
        }
        newServer.addHandler(new AbstractHandler() {
            public void handle(String target,
                               HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse,
                               int i)
                  throws IOException, ServletException {
                handleHttpRequest(target, httpServletRequest, httpServletResponse);
            }
        });
        return newServer;
    }


    protected String getRealmPropertyFile() {
        return "jettyRealm.properties";
    }


    protected String[] getRoles() {
        return null;
    }
}
