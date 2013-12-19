package net.codjo.tools.farow.util;
import java.io.File;
import java.util.Properties;
import net.codjo.test.common.fixture.DirectoryFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.codjo.test.common.matcher.JUnitMatchers.*;
import static net.codjo.tools.farow.util.GitConfigUtil.removeDuplicatesBackSlashes;
/**
 *
 */
public class GitConfigUtilTest {
    DirectoryFixture fixture = DirectoryFixture.newTemporaryDirectoryFixture("userHome");


    @Before
    public void setup() throws Exception {
        fixture.doSetUp();
    }


    @After
    public void tearDown() throws Exception {
        fixture.doTearDown();
    }


    @Test
    public void test_gitConfig() throws Exception {
        GitConfigUtil gitConfig = new GitConfigUtil(new File(getClass().getResource(".gitconfig").toURI()),
                                                    new Properties());

        assertThat(gitConfig.getProxyUserName(), equalTo("GROUPE\\MARCONA"));
        assertThat(gitConfig.getProxyPassword(), equalTo("GLOUGLOU"));
        assertThat(gitConfig.getProxyHost(), equalTo("ehttp1"));
        assertThat(gitConfig.getProxyPort(), equalTo(80));
    }


    @Test
    public void test_removeDuplicates() throws Exception {
        assertThat(removeDuplicatesBackSlashes("GROUPE\\\\\\MARCONA"), equalTo("GROUPE\\MARCONA"));
        assertThat(removeDuplicatesBackSlashes("GROUPE\\\\\\\\\\MARCONA\\"), equalTo("GROUPE\\MARCONA"));
    }
}
