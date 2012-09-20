package net.codjo.tools.farow.command;
import junit.framework.TestCase;
import net.codjo.test.common.AssertUtil;
import org.junit.Test;
/**
 *
 */
public class GithubDeleteRepoCommandTest extends TestCase {
    @Test
    public void test_construction() throws Exception {
        GithubDeleteRepoCommand githubDeleteRepoCommand = new GithubDeleteRepoCommand(ArtifactType.LIB, "mine");

        AssertUtil.assertEquals(new String[]{"gh", "delete", "codjo-mine"}, githubDeleteRepoCommand.getCommands());
    }
}
