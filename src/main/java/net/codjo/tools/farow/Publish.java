package net.codjo.tools.farow;
import java.util.Properties;
import net.codjo.tools.farow.command.ArtifactType;
import net.codjo.tools.farow.command.CommandPlayer;
import net.codjo.tools.farow.command.GithubPublishCommand;
/**
 *
 */
public class Publish extends ArtifactStep {


    public Publish(Build build, Properties properties) {
        super(build.getType(), build.getName(), createPlayer(build.getType(), build.getName(), properties));
    }


    protected static CommandPlayer createPlayer(ArtifactType type, String name, Properties properties) {
        CommandPlayer player = new CommandPlayer();

        if (ArtifactStep.isGithubAware(type, name)) {
            player.add(new GithubPublishCommand(type, name, properties));
        }
        return player;
    }


    @Override
    protected State getFinishingState() {
        return State.TO_BE_DELETED;
    }


    @Override
    protected String getAuditMessage() {
        return "Publication";
    }
}
