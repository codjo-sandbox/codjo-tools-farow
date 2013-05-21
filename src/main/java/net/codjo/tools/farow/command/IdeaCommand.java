package net.codjo.tools.farow.command;

import net.codjo.tools.farow.Display;
public class IdeaCommand extends Command {
    private String temporaryLocalRepository;


    public IdeaCommand(ArtifactType artifactType, String name, String temporaryLocalRepository) {
        super("Idea command dans " + name, artifactType, name);
        this.temporaryLocalRepository = temporaryLocalRepository;
    }


    @Override
    public void execute(Display display) throws Exception {
        executeIt(display, artifactType.getCodjoCommand(), name,
                  "&", "idea.cmd", "-Dmaven.repo.local=" + temporaryLocalRepository);
    }
}




