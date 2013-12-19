package net.codjo.tools.farow.command;

import java.io.IOException;
import net.codjo.tools.farow.Display;
import net.codjo.tools.farow.util.GitConfigUtil;
public class IdeaCommand extends Command {
    private String temporaryLocalRepository;
    private GitConfigUtil gitConfigUtil;


    public IdeaCommand(ArtifactType artifactType, String name, String temporaryLocalRepository, GitConfigUtil gitConfigUtil) {
        super("Idea command dans " + name, artifactType, name);
        this.temporaryLocalRepository = temporaryLocalRepository;
        this.gitConfigUtil = gitConfigUtil;
    }


    @Override
    public void execute(Display display) throws Exception {
        executeIt(display, artifactType.getCodjoCommand(), name, gitConfigUtil.getGithubAccount(), artifactType.getWorkingDirectory(),
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




