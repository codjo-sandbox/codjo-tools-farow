package net.codjo.tools.farow.command;
import java.io.IOException;
import java.util.Properties;
import net.codjo.tools.farow.Display;
/**
 *
 */
public class GithubPublishCommand extends Command {
    private String[] commands;


    public GithubPublishCommand(ArtifactType artifactType, String libName, Properties properties) {
        super("execute github publish commands ", artifactType, libName);

        String githubAccount = properties.getProperty("githubAccount");
        String githubPassword = properties.getProperty("githubPassword");

        this.commands = new String[]{
              "copy", "/Y", "%USERPROFILE%\\_netrc", "%USERPROFILE%\\_netrc_backup", "&",
              "echo", "machine", "github.com", "login", githubAccount, "password", githubPassword, ">",
              "%USERPROFILE%\\_netrc", "&",
              artifactType.getCodjoCommand(), name, artifactType.getGithubAccount(), artifactType.getWorkingDirectory(), "&",
              "git", "checkout", "master", "&",
              "git", "merge", "integration", "&",
              "git", "gc", "&",
              "push.cmd", "&",
              "git", "push", "--tag", "&",
              "move", "/Y", "%USERPROFILE%\\_netrc_backup", "%USERPROFILE%\\_netrc"};
    }


    @Override
    public void execute(Display display) throws IOException {
        executeIt(display, commands);
        if (buildFailure) {
            throw new IOException("push en erreur !");
        }
    }


    @Override
    protected void processLine(String line) {
        if (line.contains("ERROR")
            || line.contains("error") || (line.contains("fatal:"))) {
            buildFailure = true;
        }
    }
}
