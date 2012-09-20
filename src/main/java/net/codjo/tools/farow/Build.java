package net.codjo.tools.farow;
import net.codjo.tools.farow.command.ArtifactType;
import net.codjo.tools.farow.command.CleanUpDirectoryCommand;
import net.codjo.tools.farow.command.CommandPlayer;
import net.codjo.tools.farow.command.GetItCommand;
import net.codjo.tools.farow.command.MavenCommand;
/**
 *
 */
public class Build extends ArtifactStep {

    public Build(ArtifactType artifactType, String name) {
        super(artifactType, name, createPlayer(artifactType, name));
    }


    protected static CommandPlayer createPlayer(ArtifactType type, String name) {
        CommandPlayer player = new CommandPlayer();

        player.add(new CleanUpDirectoryCommand(type, name));
        player.add(new GetItCommand(type, name));

//        player.add(new MavenCommand(type, name, "clean"));
//        TODO : à activer lors d'une stab
        player.add(new MavenCommand(type, name, "codjo:switch-to-parent-release"));
        player.add(new MavenCommand(type, name, "release:prepare"));
        player.add(new MavenCommand(type, name, "release:perform",
                                    getGitScmAdditionalParameter(type, name),
                                    getCodjoDeploymentParameter(type)));
        player.add(new MavenCommand(type, name, "codjo:switch-to-parent-snapshot"));

        return player;
    }


    @Override
    protected String getAuditMessage() {
        return "Stabilisation";
    }
}
