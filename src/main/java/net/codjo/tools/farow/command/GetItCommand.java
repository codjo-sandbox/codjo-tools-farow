package net.codjo.tools.farow.command;
import java.io.File;
import java.io.IOException;
import net.codjo.tools.farow.Display;
import net.codjo.tools.farow.util.GitConfigUtil;
/**
 *
 */
public class GetItCommand extends Command {
    private boolean fileAlreadyExist = false;
    private GitConfigUtil gitConfigUtil;


    public GetItCommand(ArtifactType type, String name, GitConfigUtil gitConfigUtil) {
        super("Récupération de " + type + " " + name, type, name);
        this.gitConfigUtil = gitConfigUtil;
    }


    @Override
    public void execute(Display display) throws Exception {
        fileAlreadyExist = new File(toArtifactPath()).exists();
        if (fileAlreadyExist) {
            display.line("Le répertoire existe déjà.");
            throw new IOException("Le répertoire existe déjà.");
        }

        executeItInteractif(display,
                            new String[]{artifactType.getCodjoCommand(), name, gitConfigUtil.getGithubAccount(),
                                         artifactType.getWorkingDirectory()},
                            "y");

        if (buildFailure) {
            throw new IOException("Récupération de " + artifactType + " " + name + " en erreur !");
        }
    }


    @Override
    public void reExecute(Display display) throws Exception {
        if (!fileAlreadyExist) {
            try {
                new CleanUpDirectoryCommand(artifactType, name).execute(display);
            }
            catch (IOException e) {
                ;
            }
        }
        super.reExecute(display);
    }


    @Override
    protected void processLine(String line) {
        if (line.startsWith("Le chemin d'accès spécifié est introuvable.")) {
            buildFailure = true;
        }
    }
}
