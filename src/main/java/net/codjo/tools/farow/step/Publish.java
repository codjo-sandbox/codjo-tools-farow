package net.codjo.tools.farow.step;
import net.codjo.tools.farow.command.ArtifactType;
import net.codjo.tools.farow.command.CommandPlayer;
import net.codjo.tools.farow.command.GithubPublishCommand;
import net.codjo.tools.farow.util.GitConfigUtil;
/**
 *
 */
public class Publish extends ArtifactStep {


    public Publish(Build build, GitConfigUtil gitConfigUtil) {
        super(build.getType(), build.getName(), createPlayer(build.getType(), build.getName(), gitConfigUtil));
    }


    @Override
    public String getAuditMessage() {
        return "Publication";
    }


    protected static CommandPlayer createPlayer(ArtifactType type, String name, GitConfigUtil gitConfigUtil) {
        CommandPlayer player = new CommandPlayer();

        if (ArtifactStep.isGithubAware(type, name)) {
            player.add(new GithubPublishCommand(type, name, gitConfigUtil));
        }
        return player;
    }


    @Override
    protected State getFinishingState() {
        return State.TO_BE_DELETED;
    }
}
