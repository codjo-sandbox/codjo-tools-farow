package net.codjo.tools.farow.command;
import java.io.IOException;
import net.codjo.tools.farow.Display;
/**
 *
 */
public class CopyPomCommand extends Command {
    private String[] commands;


    public CopyPomCommand(ArtifactType artifactType, String libName, String destinationDirectory) {
        super("copy super-pom command ", artifactType, libName);
        this.commands = new String[]{artifactType.getCodjoCommand(), name, "&",
                                     "xcopy",
                                     "/E", "/Y", "/I", ".", destinationDirectory};
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
