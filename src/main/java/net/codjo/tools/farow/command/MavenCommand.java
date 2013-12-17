package net.codjo.tools.farow.command;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.codjo.tools.farow.Display;
/**
 *
 */
public class MavenCommand extends Command {
    private String mavenPhase;
    private String[] commands;
    //TODO a changer si on veut stabiliser le plugin codjo
    static final String CODJO_PLUGIN_PREFIX = "net.codjo.maven.mojo:maven-codjo-plugin:x.xx";


    public MavenCommand(ArtifactType artifactType, String libName, String mavenPhase, String... parameters) {
        super("execute mvn " + buildMavenPhase(mavenPhase, CODJO_PLUGIN_PREFIX), artifactType, libName);
        this.mavenPhase = buildMavenPhase(mavenPhase, CODJO_PLUGIN_PREFIX);
        this.commands = new String[]{artifactType.getCodjoCommand(), name, artifactType.getGithubAccount(), artifactType.getWorkingDirectory(),
                                     "&",
                                     "mvn", "--batch-mode", "--fail-fast", this.mavenPhase};
        if (parameters != null) {
            List<String> tempo = new ArrayList<String>();
            tempo.addAll(Arrays.asList(commands));
            tempo.addAll(Arrays.asList(parameters));
            commands = tempo.toArray(new String[tempo.size()]);
        }
    }


    static String buildMavenPhase(String mavenPhase, String codjoPluginPrefix) {
        if (mavenPhase.startsWith("codjo:") && !codjoPluginPrefix.isEmpty() && !codjoPluginPrefix.contains("x.xx")) {
            return codjoPluginPrefix + mavenPhase.substring("codjo:".length() - 1);
        }
        return mavenPhase;
    }


    @Override
    public void execute(Display display) throws IOException {
        executeIt(display, commands);
        if (buildFailure) {
            throw new IOException("mvn " + mavenPhase + " en erreur !");
        }
    }


    @Override
    protected void processLine(String line) {
        if (line.contains("[ERROR] BUILD FAILURE")
            || line.contains("[ERROR] BUILD ERROR")) {
            buildFailure = true;
        }
    }
}
