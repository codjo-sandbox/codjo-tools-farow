package net.codjo.tools.farow.command;
import java.util.Properties;
import net.codjo.tools.farow.Display;
import net.codjo.tools.farow.command.nexus.NexusApi;
/**
 *
 */
public class SetNexusProxySettingsCommand extends Command {
    static final String CODJO_INHOUSE = "codjo-inhouse";
    static final String CODJO_EXTERNAL = "codjo-external";

    private String proxyLogin;
    private String proxyPassword;
    private NexusApi nexusApi;


    public SetNexusProxySettingsCommand(String proxyLogin, String proxyPassword, Properties properties) {
        super(proxyLogin == null ?
              "Fermeture des proxies pour les repositories" :
              "Ouverture des proxies pour les repositories");
        this.proxyLogin = proxyLogin;
        this.proxyPassword = proxyPassword;
        initNexusApi(properties);
    }


    private void initNexusApi(Properties properties) {
        String nexusAccount = properties.getProperty("nexusAccount");
        String nexusPassword = properties.getProperty("nexusPassword");
        String nexusHostUrl = properties.getProperty("nexusHostUrl");
        nexusApi = new NexusApi(nexusHostUrl, nexusAccount, nexusPassword);
    }


    @Override
    public void execute(Display display) throws Exception {
        if (proxyLogin == null && proxyPassword == null) {
            display.line("début de traitement de " + CODJO_INHOUSE);
            nexusApi.removeProxySettings(CODJO_INHOUSE);
            display.line("fin de traitement de " + CODJO_INHOUSE);
            display.line("début de traitement de " + CODJO_EXTERNAL);
            nexusApi.removeProxySettings(CODJO_EXTERNAL);
            display.line("fin de traitement de " + CODJO_EXTERNAL);
        }
        else {
            display.line("début de traitement de " + CODJO_INHOUSE);
            nexusApi.setProxySettings(CODJO_INHOUSE, proxyLogin, proxyPassword);
            display.line("fin de traitement de " + CODJO_INHOUSE);
            display.line("début de traitement de " + CODJO_EXTERNAL);
            nexusApi.setProxySettings(CODJO_EXTERNAL, proxyLogin, proxyPassword);
            display.line("fin de traitement de " + CODJO_EXTERNAL);
        }
    }
}
