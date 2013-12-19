package net.codjo.tools.farow.command;
import java.io.IOException;
import net.codjo.tools.farow.Display;
import net.codjo.tools.farow.util.GitConfigUtil;
/**
 *
 */
public class CopyPomCommand extends Command {
    private String[] commands;


    public CopyPomCommand(ArtifactType artifactType, String libName, String targetDirectory, GitConfigUtil configUtil) {
        super("copy super-pom command ", artifactType, libName);
        this.commands = new String[]{artifactType.getCodjoCommand(), name, configUtil.getGithubAccount(),
                                     artifactType.getWorkingDirectory(),
                                     "&",
                                     "xcopy",
                                     "/E", "/Y", "/I", ".", targetDirectory};
    }


    @Override
    public void execute(Display display) throws IOException {
        executeIt(display, commands);
        if (buildFailure) {
            throw new IOException("push en erreur !");
        }
    }


    @Override
    protected void processLine(String line) {
        if (line.contains("ERROR")
            || line.contains("error") || (line.contains("fatal:"))) {
            buildFailure = true;
        }
    }
}
