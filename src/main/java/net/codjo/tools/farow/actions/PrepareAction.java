package net.codjo.tools.farow.actions;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import net.codjo.tools.farow.ReleaseForm;
import net.codjo.tools.farow.command.ArtifactType;
import net.codjo.tools.farow.command.CleanUpDirectoryCommand;
import net.codjo.tools.farow.command.CommandPlayer;
import net.codjo.tools.farow.command.GetItCommand;
import net.codjo.tools.farow.command.ImportArtifactsCommand;
import net.codjo.tools.farow.command.MavenCommand;
import net.codjo.tools.farow.step.ArtifactStep;

public class PrepareAction extends AbstractAction {
    private final ReleaseForm releaseForm;


    public PrepareAction(ReleaseForm releaseForm) {
        this.releaseForm = releaseForm;
        putValue(Action.NAME, "Préparation du super-pom");
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
    }


    public void actionPerformed(ActionEvent e) {
        final int result = JOptionPane.showConfirmDialog(releaseForm.getMainPanel(),
                                                         "Les étapes ci-dessous ont-elles été faites :   \n"
                                                         + "- ajout de la \"pool request\" du super-pom,  \n"
                                                         + "- validation de toutes les \"pool request\"?  \n\n",
                                                         "Github - Pool Request",
                                                         JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == 0) {
            setEnabled(false);
            CommandPlayer player = new CommandPlayer();

            player.add(new CleanUpDirectoryCommand(ArtifactType.SUPER_POM, ArtifactType.SUPER_POM.name()));
            player.add(new GetItCommand(ArtifactType.SUPER_POM, "", releaseForm.getGitConfig()));

            player.add(new MavenCommand(ArtifactType.SUPER_POM,
                                        "",
                                        releaseForm.getGitConfig(),
                                        "codjo:send-announcement-to-teams",
                                        "-DisStarting=true"));
            player.add(new MavenCommand(ArtifactType.SUPER_POM,
                                        "",
                                        releaseForm.getGitConfig(),
                                        "codjo:update-confluence-before-release"));

            player.add(new MavenCommand(ArtifactType.SUPER_POM, "", releaseForm.getGitConfig(), "codjo:release",
                                        "-DstabilisationFileName=" + releaseForm.getBuildListFile().getAbsolutePath()));
            player.add(new ImportArtifactsCommand(releaseForm, releaseForm.getBuildListFile()));
            player.add(new MavenCommand(ArtifactType.SUPER_POM, "", releaseForm.getGitConfig(), "release:prepare",
                                        "-Ddocumentation=disabled"));
            player.add(new MavenCommand(ArtifactType.SUPER_POM, "", releaseForm.getGitConfig(), "release:perform",
                                        "-Ddocumentation=disabled",
                                        ArtifactStep.getGitScmAdditionalParameter(ArtifactType.SUPER_POM, "unused"),
                                        ArtifactStep.getCodjoDeploymentParameter()));

            releaseForm.createAndStartStepInvoker("Premières étapes", player);
        }
    }
}