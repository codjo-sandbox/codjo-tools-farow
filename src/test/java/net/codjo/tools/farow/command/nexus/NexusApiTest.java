package net.codjo.tools.farow.command.nexus;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.codjo.tools.farow.command.nexus.bean.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Request;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
/**
 *
 */
public class NexusApiTest extends JettyFixture {

    @Before
    public void setup() throws Exception {
        super.doSetup();
    }


    @After
    public void tearDown() throws Exception {
        super.doTearDown();
    }


    @Test
    public void test_proxyWithhttpClient() throws Exception {
        String repositoryId = "marcona";

        NexusApi nexusApi = new NexusApi("http://localhost:" + PORT + "/");

        Repository initialRepository = nexusApi.getRepository("marcona", "admin", "admin");
        assertNull(initialRepository.getData().getRemoteStorage().getHttpProxySettings());

        Repository repositoryWithProxy = nexusApi.setProxySettings(repositoryId,
                                                                   "admin",
                                                                   "admin",
                                                                   "GROUPE\\MARCONNET",
                                                                   "coucou");
        assertNotNull(repositoryWithProxy.getData().getRemoteStorage().getHttpProxySettings());

        Repository withoutProxyRepository = nexusApi.removeProxySettings(repositoryId, "admin", "admin");
        assertNull(withoutProxyRepository.getData().getRemoteStorage().getHttpProxySettings());
    }


    private String getMockedResponse(String repositoryId) {

        return "<repository>\n"
               + " <data>\n"
               + "  <contentResourceURI>http://a7wa008:" + PORT
               + "/nexus/content/repositories/marcona</contentResourceURI> \n"
               + "  <id>" + repositoryId + "</id> \n"
               + "  <name>marcona-name</name> \n"
               + "  <provider>maven2</provider> \n"
               + "  <providerRole>org.sonatype.nexus.proxy.repository.Repository</providerRole> \n"
               + "  <format>maven2</format> \n"
               + "  <repoType>proxy</repoType> \n"
               + "  <exposed>true</exposed> \n"
               + "  <writePolicy>READ_ONLY</writePolicy> \n"
               + "  <browseable>true</browseable> \n"
               + "  <indexable>true</indexable> \n"
               + "  <notFoundCacheTTL>1440</notFoundCacheTTL> \n"
               + "  <repoPolicy>RELEASE</repoPolicy> \n"
               + "  <checksumPolicy>WARN</checksumPolicy> \n"
               + "  <downloadRemoteIndexes>true</downloadRemoteIndexes> \n"
               + "  <defaultLocalStorageUrl>file:/c:/dev/user/tools/nexus/storage/marcona/</defaultLocalStorageUrl> \n"
               + "  <remoteStorage>\n"
               + "  <remoteStorageUrl>http://marconnet.homedns.org/</remoteStorageUrl> \n"
               + "  </remoteStorage>\n"
               + "  <fileTypeValidation>false</fileTypeValidation> \n"
               + "  <artifactMaxAge>-1</artifactMaxAge> \n"
               + "  <metadataMaxAge>1440</metadataMaxAge> \n"
               + "  <autoBlockActive>true</autoBlockActive> \n"
               + "  </data>\n"
               + "  </repository>";
    }


    @Override
    protected void handleHttpRequest(String target,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {

        String[] urls = target.split("/");
        String repositoryId = urls[urls.length - 1];
        try {
            if ("GET".equals(request.getMethod())) {
                response.getWriter().print(getMockedResponse(repositoryId));
                response.getWriter().close();
            }
            if ("PUT".equals(request.getMethod())) {
                response.getWriter().print(decode(request));
                response.getWriter().close();
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.setContentType(APPLICATION_XML);
        ((Request)request).setHandled(true);
    }
}
