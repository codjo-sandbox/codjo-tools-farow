package net.codjo.tools.farow.command;
import java.io.IOException;
import net.codjo.tools.farow.Display;
/**
 *
 */
public class GithubDeleteRepoCommand extends Command {
    private String[] commands;


    public GithubDeleteRepoCommand(ArtifactType artifactType, String libName) {
        super("execute github delete repo commands ", artifactType, libName);
        this.commands = new String[]{"gh", "delete", artifactType.toArtifactName(libName)};
    }


    public String[] getCommands() {
        return commands;
    }


    @Override
    public void execute(Display display) throws IOException {
        executeItInteractif(display, commands, "y");

        if (buildFailure) {
            throw new IOException("delete repo en erreur !");
        }
    }


    @Override
    protected void processLine(String line) {
        if (line.contains("ERROR")
            || line.contains("error") || (line.contains("fatal:"))
            || line.contains("Exception:")) {
            buildFailure = true;
        }
    }
}
