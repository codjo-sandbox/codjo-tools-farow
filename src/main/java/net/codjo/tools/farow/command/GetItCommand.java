package net.codjo.tools.farow.command;
import java.io.File;
import java.io.IOException;
/**
 *
 */
public class GetItCommand extends Command {
    private boolean fileAlreadyExist = false;


    public GetItCommand(ArtifactType type, String name) {
        super("Récupération de " + type + " " + name, type, name);
    }


    @Override
    public void execute(Display display) throws Exception {
        fileAlreadyExist = new File(toArtifactPath()).exists();
        if (fileAlreadyExist) {
            display.line("Le répertoire existe déjà.");
            throw new IOException("Le répertoire existe déjà.");
        }

        executeItInteractif(display, new String[]{artifactType.getCodjoCommand(), name}, "y");

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
