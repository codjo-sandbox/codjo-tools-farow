package net.codjo.tools.farow.command;
import java.util.Properties;
import net.codjo.tools.farow.command.nexus.NexusApi;
/**
 *
 */
public class SetNexusProxySettingsCommand extends Command {
    static final String CODJO_INHOUSE = "codjo-inhouse";
    static final String CODJO_EXTERNAL = "codjo-external";

    private String proxyLogin;
    private String proxyPassword;
    private String nexusAccount;
    private String nexusPassword;
    private String nexusHostUrl;


    public SetNexusProxySettingsCommand(String proxyLogin, String proxyPassword,  Properties properties) {
        super(proxyLogin == null ?
              "Fermeture des proxies pour les repositories" :
              "Ouverture des proxies pour les repositories");
        this.proxyLogin = proxyLogin;
        this.proxyPassword = proxyPassword;
        nexusAccount = properties.getProperty("nexusAccount");
        nexusPassword = properties.getProperty("nexusPassword");
        nexusHostUrl =  properties.getProperty("nexusHostUrl");
    }


    @Override
    public void execute(Display display) throws Exception {
        NexusApi nexusApi = new NexusApi(nexusHostUrl);
        if (proxyLogin == null && proxyPassword == null) {
            display.line("début de traitement de " + CODJO_INHOUSE);
            nexusApi.removeProxySettings(CODJO_INHOUSE, nexusAccount, nexusPassword);
            display.line("fin de traitement de " + CODJO_INHOUSE);
            display.line("début de traitement de " + CODJO_EXTERNAL);
            nexusApi.removeProxySettings(CODJO_EXTERNAL, nexusAccount, nexusPassword);
            display.line("fin de traitement de " + CODJO_EXTERNAL);
        }
        else {
            display.line("début de traitement de " + CODJO_INHOUSE);
            nexusApi.setProxySettings(CODJO_INHOUSE, nexusAccount, nexusPassword, proxyLogin, proxyPassword);
            display.line("fin de traitement de " + CODJO_INHOUSE);
            display.line("début de traitement de " + CODJO_EXTERNAL);
            nexusApi.setProxySettings(CODJO_EXTERNAL, nexusAccount, nexusPassword, proxyLogin, proxyPassword);
            display.line("fin de traitement de " + CODJO_EXTERNAL);
        }
    }
}
