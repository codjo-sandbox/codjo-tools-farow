package net.codjo.tools.farow.command;

import java.io.IOException;
import net.codjo.tools.farow.Display;
public class IdeaCommand extends Command {
    private String temporaryLocalRepository;


    public IdeaCommand(ArtifactType artifactType, String name, String temporaryLocalRepository) {
        super("Idea command dans " + name, artifactType, name);
        this.temporaryLocalRepository = temporaryLocalRepository;
    }


    @Override
    public void execute(Display display) throws Exception {
        executeIt(display, artifactType.getCodjoCommand(), name, artifactType.getGithubAccount(), artifactType.getWorkingDirectory(),
                  "&", "idea.cmd", "-Dmaven.repo.local=" + temporaryLocalRepository);
        if (buildFailure) {
            throw new IOException("push en erreur !");
        }
    }


    @Override
    protected void processLine(String line) {
        if (line.contains("[WARNING] An error occurred during dependency resolution")) {
            buildFailure = true;
        }
    }
}




