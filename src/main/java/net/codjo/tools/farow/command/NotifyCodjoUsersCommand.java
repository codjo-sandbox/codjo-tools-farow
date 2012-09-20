package net.codjo.tools.farow.command;
import net.codjo.tools.farow.command.teamlab.ProxyInformation;
import net.codjo.tools.farow.command.teamlab.TeamLabApi;
import net.codjo.tools.farow.util.GitConfigUtil;
/**
 *
 */
public class NotifyCodjoUsersCommand extends Command {
    static final String TEAMLAB_HOST_URL = "http://team.codjo.net";
    private GitConfigUtil proxyInformation;
    private String frameworkVersion;
    private String apiUserName;
    private String apiPassword;


    public NotifyCodjoUsersCommand(GitConfigUtil proxyInformation,
                                   String frameworkVersion,
                                   String apiUserName,
                                   String apiPassword) {
        super("Envoi de la notification à l'equipe codjo sur Teamlab");
        this.proxyInformation = proxyInformation;
        this.frameworkVersion = frameworkVersion;
        this.apiUserName = apiUserName;
        this.apiPassword = apiPassword;
    }


    @Override
    public void execute(Display display) throws Exception {
        display.line("Connection to " + TEAMLAB_HOST_URL);
        display.line("with the following parameters: frameworkVersion = " + frameworkVersion + ",  apiUserName = "
                     + apiUserName
                     + ", apiUserPassword = " + apiPassword);

        TeamLabApi api = new TeamLabApi(TEAMLAB_HOST_URL,
                                        apiUserName,
                                        apiPassword,
                                        new ProxyInformation(proxyInformation.getProxyHost(),
                                                             proxyInformation.getProxyPort(),
                                                             proxyInformation.getProxyUserName(),
                                                             proxyInformation.getProxyPassword())
        );

        String content = "Bonjour,\n" +
                         "            la version " + frameworkVersion
                         + " du framework est disponible sur le repository binaire codjo.\n"
                         +
                         "            \n" +
                         "            Cordialement,\n" +
                         "            Codjo Team (du 8eme)";
        display.line("Posting new event in Teamlab");

        String result = api.postEvent("Livraison du framework codjo " + frameworkVersion, "1", content);
        display.line("Teamlab response " + result);
    }
}
