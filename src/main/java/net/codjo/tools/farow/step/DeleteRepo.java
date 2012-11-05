package net.codjo.tools.farow.step;
import net.codjo.tools.farow.command.ArtifactType;
import net.codjo.tools.farow.command.CommandPlayer;
import net.codjo.tools.farow.command.GithubDeleteRepoCommand;
/**
 *
 */
public class DeleteRepo extends ArtifactStep {

    public DeleteRepo(Publish build) {
        super(build.getType(), build.getName(), createPlayer(build.getType(), build.getName()));
    }


    @Override
    public String getAuditMessage() {
        return "Delete repo";
    }


    protected static CommandPlayer createPlayer(ArtifactType type, String name) {
        CommandPlayer player = new CommandPlayer();

        if (ArtifactStep.isGithubAware(type, name)) {
            player.add(new GithubDeleteRepoCommand(type, name));
        }
        return player;
    }


    @Override
    protected State getFinishingState() {
        return State.DONE;
    }
}
