package net.codjo.tools.farow.command.nexus;
import junit.framework.TestCase;
import net.codjo.test.common.XmlUtil;
import net.codjo.tools.farow.command.nexus.bean.Authentication;
import net.codjo.tools.farow.command.nexus.bean.Data;
import net.codjo.tools.farow.command.nexus.bean.HttpProxySettings;
import net.codjo.tools.farow.command.nexus.bean.RemoteStorage;
import net.codjo.tools.farow.command.nexus.bean.Repository;
import org.junit.Test;
/**
 *
 */
public class XmlCodecTest extends TestCase {

    @Test
    public void test_toXml() throws Exception {
        XmlCodec codec = new XmlCodec();

        String expected = "<repository>\n"
                          + "  <data>\n"
                          + "    <id>dataId</id>\n"
                          + "    <remoteStorage>\n"
                          + "      <httpProxySettings>\n"
                          + "        <proxyPort>80</proxyPort>\n"
                          + "        <authentication>\n"
                          + "          <username>arno</username>\n"
                          + "          <password>marconnet</password>\n"
                          + "        </authentication>\n"
                          + "        <nonProxyHosts>nonProxyHost</nonProxyHosts>\n"
                          + "      </httpProxySettings>\n"
                          + "    </remoteStorage>\n"
                          + "    <exposed>false</exposed>\n"
                          + "    <browseable>false</browseable>\n"
                          + "    <indexable>false</indexable>\n"
                          + "    <downloadRemoteIndexes>false</downloadRemoteIndexes>\n"
                          + "    <fileTypeValidation>false</fileTypeValidation>\n"
                          + "    <artifactMaxAge>0</artifactMaxAge>\n"
                          + "    <metadataMaxAge>0</metadataMaxAge>\n"
                          + "    <autoBlockActive>false</autoBlockActive>\n"
                          + "  </data>\n"
                          + "</repository>";

        assertTrue(XmlUtil.areEquivalent(expected, codec.toXml(buildRepository("dataId", "nonProxyHost", 80, "arno",
                                                                               "marconnet"))));
    }


    private Repository buildRepository(String dataId,
                                       String nonProxyHost,
                                       int proxyPort,
                                       String proxyUserName, String proxyPassword) {
        Repository repository = new Repository();
        Data data = new Data();
        repository.setData(data);
        data.setId(dataId);
        RemoteStorage remoteStorage = new RemoteStorage();
        HttpProxySettings httpProxySettings = new HttpProxySettings();
        httpProxySettings.setNonProxyHosts(nonProxyHost);
        httpProxySettings.setProxyPort(proxyPort);
        httpProxySettings.setAuthentication(new Authentication(proxyUserName, proxyPassword));
        remoteStorage.setHttpProxySettings(httpProxySettings);
        data.setRemoteStorage(remoteStorage);
        return repository;
    }
}
