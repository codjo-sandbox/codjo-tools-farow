package net.codjo.tools.farow.command;
import java.util.Properties;
import net.codjo.tools.farow.Display;
import net.codjo.tools.farow.command.nexus.NexusApi;
/**
 *
 */
public class RebuildNexusPomMetaDataCommand extends Command {
    private NexusApi nexusApi;


    public RebuildNexusPomMetaDataCommand(Properties properties) {
        super("Reconstruction des metadatas du super-pom sur Nexus");
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
        display.line("début de traitement de rebuild des metadatas");
        String nexusTaskId = nexusApi.getScheduledTaskId("RebuildCodjoPomMetadata");
        nexusApi.runScheduledTask(nexusTaskId);
        display.line("fin de traitement de rebuild des metadatas");
    }
}
