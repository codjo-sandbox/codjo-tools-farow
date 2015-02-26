package net.codjo.tools.farow.command;
import java.io.File;
import net.codjo.tools.farow.Display;
import net.codjo.tools.farow.ReleaseForm;
/**
 *
 */
public class ImportArtifactsCommand extends Command {
    private final ReleaseForm releaseForm;
    private final File listFile;


    public ImportArtifactsCommand(ReleaseForm releaseForm, File listFile) {
        super("Importation des artifacts à stabiliser");
        this.releaseForm = releaseForm;
        this.listFile = listFile;
    }


    @Override
    public void execute(Display display) throws Exception {
        //trie
        releaseForm.loadBuildListFile(listFile);
    }
}
