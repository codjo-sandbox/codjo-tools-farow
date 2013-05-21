package net.codjo.tools.farow.command;
import java.io.File;
import java.io.IOException;
import net.codjo.tools.farow.Display;

public class CleanUpDirectoryCommand extends Command {
    private String filePath;


    public CleanUpDirectoryCommand(String filePath) {
        super("Suppression du répertoire " + filePath);
        this.filePath = filePath;
    }


    public CleanUpDirectoryCommand(ArtifactType artifactType, String name) {
        this(artifactType.toArtifactPath(name));
    }


    @Override
    public void execute(Display display) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            display.line("Répertoire inexistant. Inutile de le supprimer");
            return;
        }
        executeIt(display, "rd", "/S", "/Q", filePath);

        if (file.exists()) {
            throw new IOException("Suppression non effective");
        }
    }
}
