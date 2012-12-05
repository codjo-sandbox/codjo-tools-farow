package net.codjo.tools.farow.command.nexus;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.codjo.tools.farow.command.nexus.bean.Repository;
import net.codjo.tools.farow.util.JettyFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;

import static net.codjo.test.common.matcher.JUnitMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
/**
 *
 */
public class NexusApiTest {
    private JettyFixture jettyFixture;
    private NexusApi nexusApi;
    public static final int PORT = 7777;
    public static final String APPLICATION_XML = "application/xml";


    @Before
    public void setup() throws Exception {
        jettyFixture = new JettyFixture(PORT) {
            @Override
            protected void handleHttpRequest(String target, HttpServletRequest request, HttpServletResponse response) {
                NexusApiTest.handleHttpRequest(this, target, request, response);
            }


            @Override
            protected String getRealmPropertyFile() {
                return "/net/codjo/tools/farow/command/nexus/jettyRealm.properties";
            }


            @Override
            protected String[] getRoles() {
                return new String[]{"user", "admin", "moderator"};
            }
        };
        jettyFixture.doSetUp();
        nexusApi = new NexusApi("http://localhost:" + PORT + "/", "admin", "admin");
    }


    @After
    public void tearDown() throws Exception {
        jettyFixture.doTearDown();
    }


    @Test
    public void test_proxyWithhttpClient() throws Exception {
        String repositoryId = "marcona";

        Repository initialRepository = nexusApi.getRepository("marcona");
        assertNull(initialRepository.getData().getRemoteStorage().getHttpProxySettings());

        Repository repositoryWithProxy = nexusApi.setProxySettings(repositoryId, "GROUPE\\MARCONNET", "coucou");
        assertNotNull(repositoryWithProxy.getData().getRemoteStorage().getHttpProxySettings());

        Repository withoutProxyRepository = nexusApi.removeProxySettings(repositoryId);
        assertNull(withoutProxyRepository.getData().getRemoteStorage().getHttpProxySettings());
    }


    @Test
    public void test_runScheduledTask() throws Exception {
        assertThat(nexusApi.runScheduledTask("41").contains("<status>SUBMITTED</status>"), is(true));
    }


    @Test
    public void test_getScheduledTaskByName() throws Exception {
        assertThat(nexusApi.getScheduledTaskId("RebuildCodjoPomMetadata"), is("41"));
    }


    @Test
    public void test_getScheduledTaskByNameNotFound() throws Exception {
        try {
            assertThat(nexusApi.getScheduledTaskId("patate"), nullValue());
            fail();
        }
        catch (Exception e) {
            assertThat(e.getMessage(), is("Task patate not found in Nexus"));
        }
    }


    private static String getMockedResponse(String repositoryId) {

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


    private static String getMockResultForRunSchedule() {
        return " <schedule-service-status>\n"
               + " <data>\n"
               + " <resource>\n"
               + "  <id>41</id> \n"
               + "  <name>RebuildCodjoPomMetadata</name> \n"
               + "  <enabled>true</enabled> \n"
               + "  <typeId>RebuildMavenMetadataTask</typeId> \n"
               + "  <schedule>manual</schedule> \n"
               + " <properties>\n"
               + " <scheduled-task-property>\n"
               + "  <key>resourceStorePath</key> \n"
               + "  <value>/net/codjo/pom</value> \n"
               + "  </scheduled-task-property>\n"
               + " <scheduled-task-property>\n"
               + "  <key>repositoryId</key> \n"
               + "  <value>codjo-inhouse</value> \n"
               + "  </scheduled-task-property>\n"
               + "  </properties>\n"
               + "  </resource>\n"
               + "  <resourceURI>http://a7wa008:8080/nexus/service/local/schedule_run/41/41</resourceURI> \n"
               + "  <status>SUBMITTED</status> \n"
               + "  <readableStatus>Waiting</readableStatus> \n"
               + "  <nextRunTime>n/a</nextRunTime> \n"
               + "  <lastRunTime>Wed Nov 07 16:15:20 CET 2012</lastRunTime> \n"
               + "  <lastRunResult>Ok</lastRunResult> \n"
               + "  <created>Thu Oct 25 17:35:13 CEST 2012</created> \n"
               + "  </data>\n"
               + "  </schedule-service-status>";
    }


    private static String getMockScheduleList() {
        return "<schedules-list>\n"
               + "  <data>\n"
               + "    <schedules-list-item>\n"
               + "      <resourceURI>http://a7wa008:8080/nexus/service/local/schedules/41</resourceURI>\n"
               + "      <enabled>true</enabled>\n"
               + "      <name>RebuildCodjoPomMetadata</name>\n"
               + "      <id>41</id>\n"
               + "      <typeId>RebuildMavenMetadataTask</typeId>\n"
               + "      <typeName>Rebuild Maven Metadata Files</typeName>\n"
               + "      <status>SUBMITTED</status>\n"
               + "      <readableStatus>Waiting</readableStatus>\n"
               + "      <schedule>manual</schedule>\n"
               + "      <nextRunTime>n/a</nextRunTime>\n"
               + "      <lastRunTime>Wed Nov 07 17:27:47 CET 2012</lastRunTime>\n"
               + "      <lastRunResult>Ok [0s]</lastRunResult>\n"
               + "      <created>Thu Oct 25 17:35:13 CEST 2012</created>\n"
               + "    </schedules-list-item>\n"
               + "    <schedules-list-item>\n"
               + "      <resourceURI>http://a7wa008:8080/nexus/service/local/schedules/1</resourceURI>\n"
               + "      <id>1</id>\n"
               + "      <enabled>true</enabled>\n"
               + "      <name>Download Indexes Central</name>\n"
               + "      <typeId>DownloadIndexesTask</typeId>\n"
               + "      <typeName>Download Indexes</typeName>\n"
               + "      <status>SUBMITTED</status>\n"
               + "      <readableStatus>Waiting</readableStatus>\n"
               + "      <schedule>manual</schedule>\n"
               + "      <nextRunTime>n/a</nextRunTime>\n"
               + "      <lastRunTime>n/a</lastRunTime>\n"
               + "      <lastRunResult>n/a</lastRunResult>\n"
               + "      <created>Thu Oct 25 17:35:13 CEST 2012</created>\n"
               + "    </schedules-list-item>\n"
               + "    <schedules-list-item>\n"
               + "      <resourceURI>http://a7wa008:8080/nexus/service/local/schedules/4</resourceURI>\n"
               + "      <id>4</id>\n"
               + "      <enabled>true</enabled>\n"
               + "      <name>Repair index ZaideExternal</name>\n"
               + "      <typeId>RepairIndexTask</typeId>\n"
               + "      <typeName>Repair Repositories Index</typeName>\n"
               + "      <status>SUBMITTED</status>\n"
               + "      <readableStatus>Waiting</readableStatus>\n"
               + "      <schedule>manual</schedule>\n"
               + "      <nextRunTime>n/a</nextRunTime>\n"
               + "      <lastRunTime>n/a</lastRunTime>\n"
               + "      <lastRunResult>n/a</lastRunResult>\n"
               + "      <created>Thu Oct 25 17:35:13 CEST 2012</created>\n"
               + "    </schedules-list-item>\n"
               + "    <schedules-list-item>\n"
               + "      <resourceURI>http://a7wa008:8080/nexus/service/local/schedules/5</resourceURI>\n"
               + "      <id>5</id>\n"
               + "      <enabled>true</enabled>\n"
               + "      <name>Repair index ZaideExternal</name>\n"
               + "      <typeId>ExpireCacheTask</typeId>\n"
               + "      <typeName>Expire Repository Caches</typeName>\n"
               + "      <status>SUBMITTED</status>\n"
               + "      <readableStatus>Waiting</readableStatus>\n"
               + "      <schedule>manual</schedule>\n"
               + "      <nextRunTime>n/a</nextRunTime>\n"
               + "      <lastRunTime>n/a</lastRunTime>\n"
               + "      <lastRunResult>n/a</lastRunResult>\n"
               + "      <created>Thu Oct 25 17:35:13 CEST 2012</created>\n"
               + "    </schedules-list-item>\n"
               + "  </data>\n"
               + "</schedules-list>";
    }


    private static void handleHttpRequest(JettyFixture jettyFixture, String target,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {

        String[] urls = target.split("/");
        String repositoryId = urls[urls.length - 1];
        int status = ((Response)response).getStatus();
        if (status == 200) {
            try {
                if ("GET".equals(request.getMethod())) {
                    String mockedResponse = "";
                    if (target.contains("repositories")) {
                        mockedResponse = getMockedResponse(repositoryId);
                    }
                    if (target.contains("schedule_run")) {
                        mockedResponse = getMockResultForRunSchedule();
                    }
                    if (target.contains("schedules")) {
                        mockedResponse = getMockScheduleList();
                    }
                    response.getWriter().print(mockedResponse);
                    response.getWriter().close();
                }
                if ("PUT".equals(request.getMethod())) {
                    response.getWriter().print(jettyFixture.decode(request));
                    response.getWriter().close();
                }
                if ("DELETE".equals(request.getMethod())) {
                    response.getWriter().print(jettyFixture.decode(request));
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
}
