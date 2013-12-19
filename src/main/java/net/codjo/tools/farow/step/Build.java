package net.codjo.tools.farow.step;
import net.codjo.tools.farow.command.ArtifactType;
import net.codjo.tools.farow.command.CleanUpDirectoryCommand;
import net.codjo.tools.farow.command.CommandPlayer;
import net.codjo.tools.farow.command.GetItCommand;
import net.codjo.tools.farow.command.MavenCommand;
import net.codjo.tools.farow.util.GitConfigUtil;
/**
 *
 */
public class Build extends ArtifactStep {

    public Build(ArtifactType artifactType, String name, GitConfigUtil gitConfigUtil) {
        super(artifactType, name, createPlayer(artifactType, name, gitConfigUtil));
    }


    @Override
    public String getAuditMessage() {
        return "Stabilisation";
    }


    protected static CommandPlayer createPlayer(ArtifactType type, String name, GitConfigUtil gitConfigUtil) {
        CommandPlayer player = new CommandPlayer();

        player.add(new CleanUpDirectoryCommand(type, name));
        player.add(new GetItCommand(type, name, gitConfigUtil));

//        player.add(new MavenCommand(type, name, gitConfigUtil, "clean"));
//        TODO : à activer lors d'une stab
        player.add(new MavenCommand(type, name, gitConfigUtil, "codjo:switch-to-parent-release"));
        player.add(new MavenCommand(type, name, gitConfigUtil, "release:prepare"));
        player.add(new MavenCommand(type, name, gitConfigUtil, "release:perform",
                                    getGitScmAdditionalParameter(type, name), getCodjoDeploymentParameter()));
        player.add(new MavenCommand(type, name, gitConfigUtil, "codjo:switch-to-parent-snapshot"));

        return player;
    }
}
