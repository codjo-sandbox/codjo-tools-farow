package net.codjo.tools.farow.command.nexus;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import net.codjo.tools.farow.command.nexus.bean.Authentication;
import net.codjo.tools.farow.command.nexus.bean.Data;
import net.codjo.tools.farow.command.nexus.bean.HttpProxySettings;
import net.codjo.tools.farow.command.nexus.bean.RemoteStorage;
import net.codjo.tools.farow.command.nexus.bean.Repository;
/**
 *
 */
public class XmlCodec {
    private final XStream xStream;


    public XmlCodec() {
        xStream = new XStream(new DomDriver());
        xStream.alias("repository", Repository.class);
        xStream.alias("data", Data.class);
        xStream.alias("httpProxySettings", HttpProxySettings.class);
        xStream.alias("remoteStorage", RemoteStorage.class);
        xStream.alias("authentication", Authentication.class);

        xStream.setMode(XStream.NO_REFERENCES);
    }


    public String toXml(Repository repository) {
        return xStream.toXML(repository);
    }


    public Repository fromXml(String xmlConfig) {
        return (Repository)xStream.fromXML(xmlConfig);
    }
}
