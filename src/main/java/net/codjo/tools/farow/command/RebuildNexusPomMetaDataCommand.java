package net.codjo.tools.farow.command;
import java.util.Properties;
import net.codjo.tools.farow.Display;
import net.codjo.tools.farow.command.nexus.NexusApi;
/**
 *
 */
public class RebuildNexusPomMetaDataCommand extends Command {
    private String nexusAccount;
    private String nexusPassword;
    private String nexusHostUrl;


    public RebuildNexusPomMetaDataCommand(Properties properties) {
        super("Reconstruction des metadatas du super-pom sur Nexus");
        nexusAccount = properties.getProperty("nexusAccount");
        nexusPassword = properties.getProperty("nexusPassword");
        nexusHostUrl = properties.getProperty("nexusHostUrl");
    }


    @Override
    public void execute(Display display) throws Exception {
        NexusApi nexusApi = new NexusApi(nexusHostUrl);
        display.line("début de traitement de rebuild des metadatas");
        String nexusTaskId = nexusApi.getScheduledTaskId("RebuildCodjoPomMetadata", nexusAccount, nexusPassword);
        nexusApi.runScheduledTask(nexusTaskId, nexusAccount, nexusPassword);
        display.line("fin de traitement de rebuild des metadatas");
    }
}
