package net.codjo.tools.farow.command;
import org.junit.Assert;
import org.junit.Test;
/**
 *
 */
public class MavenCommandTest {
    @Test
    public void test_hackCodjoMavenPlugin() throws Exception {
        Assert.assertEquals("net.codjo.maven.mojo:maven-codjo-plugin:2.23:toto",
                            MavenCommand.buildMavenPhase("codjo:toto", "net.codjo.maven.mojo:maven-codjo-plugin:2.23"));

        Assert.assertEquals("net.codjo.maven.mojo:maven-codjo-plugin:2.23:switch-to-parent-release",
                            MavenCommand.buildMavenPhase("codjo:switch-to-parent-release",
                                                         "net.codjo.maven.mojo:maven-codjo-plugin:2.23"));

        Assert.assertEquals("codjo:toto",
                            MavenCommand.buildMavenPhase("codjo:toto", "net.codjo.maven.mojo:maven-codjo-plugin:x.xx"));

        Assert.assertEquals("codjo:toto",
                            MavenCommand.buildMavenPhase("codjo:toto", ""));

        Assert.assertEquals("anythingElse",
                            MavenCommand.buildMavenPhase("anythingElse",
                                                         "net.codjo.maven.mojo:maven-codjo-plugin:x.xx"));
    }
}
