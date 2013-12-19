package net.codjo.tools.farow.command;
import java.io.IOException;
import net.codjo.tools.farow.Display;
import net.codjo.tools.farow.util.GitConfigUtil;
/**
 *
 */
public class GithubPublishCommand extends Command {
    private String[] commands;


    public GithubPublishCommand(ArtifactType artifactType, String libName, GitConfigUtil gitConfigUtil) {
        super("execute github publish commands ", artifactType, libName);

        String githubAccount = gitConfigUtil.getGithubAccount();
        String githubPassword = gitConfigUtil.getGithubPassword();

        this.commands = new String[]{
              "copy", "/Y", "%USERPROFILE%\\_netrc", "%USERPROFILE%\\_netrc_backup", "&",
              "echo", "machine", "github.com", "login", githubAccount, "password", githubPassword, ">",
              "%USERPROFILE%\\_netrc", "&",
              artifactType.getCodjoCommand(), name, artifactType.getGithubAccount(), artifactType.getWorkingDirectory(),
              "&",
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
