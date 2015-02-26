package net.codjo.tools.farow.actions;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import net.codjo.tools.farow.ReleaseForm;
import net.codjo.tools.farow.command.ArtifactType;
import net.codjo.tools.farow.command.CleanInhouseSnapshot;
import net.codjo.tools.farow.command.CleanUpDirectoryCommand;
import net.codjo.tools.farow.command.CommandPlayer;
import net.codjo.tools.farow.command.CopyPomCommand;
import net.codjo.tools.farow.command.IdeaCommand;
import net.codjo.tools.farow.command.MavenCommand;
import net.codjo.tools.farow.command.PrepareAPomToLoadSuperPomDependenciesCommand;
import net.codjo.tools.farow.command.SetNexusProxySettingsCommand;
import net.codjo.tools.farow.step.ArtifactStep;
public class UpdateArtifactManagerAction extends AbstractAction {
    private final ReleaseForm releaseForm;


    public UpdateArtifactManagerAction(ReleaseForm releaseForm) {
        this.releaseForm = releaseForm;
        putValue(Action.NAME, "Artifact manager update");
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
    }


    public void actionPerformed(ActionEvent e) {
        String frameworkVersion = JOptionPane.showInputDialog("Merci de preciser le numéro de version du framework");
        if (frameworkVersion == null || frameworkVersion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(releaseForm.getMainPanel(),
                                          "Le numéro de version du framework ne peut etre null ou vide",
                                          "Impossible de finaliser la Stabilisation",
                                          JOptionPane.ERROR_MESSAGE);
        }
        else {
            CommandPlayer player = new CommandPlayer();
            player.add(new CleanUpDirectoryCommand("C:\\Dev\\platform\\tools\\maven\\local\\maven2\\net\\codjo\\pom"));
            player.add(new CleanInhouseSnapshot());
            player.add(new MavenCommand(ArtifactType.SUPER_POM, "", releaseForm.getGitConfig(), "deploy"));
            player.add(new MavenCommand(ArtifactType.SUPER_POM,
                                        "",
                                        releaseForm.getGitConfig(),
                                        "deploy",
                                        ArtifactStep.REMOTE_CODJO));
            // Rapatriement des librairies postées sur repo.codjo.net avec maven et nexus
            player.add(new SetNexusProxySettingsCommand(releaseForm.getGitConfig().getProxyUserName(),
                                                        releaseForm.getGitConfig().getProxyPassword(),
                                                        releaseForm.getProperties()));

            String codjoPomAllDepsDirectory = "pom-alldeps";
            String codjoPomAllDepsPath = ArtifactType.LIB.toArtifactPath(codjoPomAllDepsDirectory);
            String temporaryLocalRepository = ArtifactType.LIB.toArtifactPath("repo");

            player.add(new CleanUpDirectoryCommand(codjoPomAllDepsPath));
            player.add(new CopyPomCommand(ArtifactType.SUPER_POM, "", codjoPomAllDepsPath, releaseForm.getGitConfig()));
            player.add(new PrepareAPomToLoadSuperPomDependenciesCommand(codjoPomAllDepsPath, frameworkVersion));

            player.add(new CleanUpDirectoryCommand(temporaryLocalRepository));
            player.add(new IdeaCommand(ArtifactType.LIB,
                                       codjoPomAllDepsDirectory,
                                       temporaryLocalRepository,
                                       releaseForm.getGitConfig()));

            player.add(new SetNexusProxySettingsCommand(null, null, releaseForm.getProperties()));

            releaseForm.createAndStartStepInvoker("Mise à jour du gestionnaire d'artifact", player);
        }
    }
}
