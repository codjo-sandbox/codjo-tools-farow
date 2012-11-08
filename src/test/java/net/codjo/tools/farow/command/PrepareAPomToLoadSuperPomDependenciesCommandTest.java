package net.codjo.tools.farow.command;
import java.io.IOException;
import java.io.StringReader;
import net.codjo.test.common.XmlUtil;
import net.codjo.util.file.FileUtil;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;
/**
 *
 */
public class PrepareAPomToLoadSuperPomDependenciesCommandTest {

    @Test
    public void test_addPomTypeToCodjoSegmentationDatagenArtifact() throws JDOMException, IOException {
        String stringToProcess = "<dependencies><dependency>\n"
                                 + "                <groupId>net.codjo.segmentation</groupId>\n"
                                 + "                <artifactId>codjo-segmentation-common</artifactId>\n"
                                 + "                <version>1.74</version>\n"
                                 + "            </dependency>\n"
                                 + "            <dependency>\n"
                                 + "                <groupId>net.codjo.segmentation</groupId>\n"
                                 + "                <artifactId>codjo-segmentation-datagen</artifactId>\n"
                                 + "                <version>1.74</version>\n"
                                 + "            </dependency>\n"
                                 + "            <dependency>\n"
                                 + "                <groupId>net.codjo.segmentation</groupId>\n"
                                 + "                <artifactId>codjo-segmentation-datagen</artifactId>\n"
                                 + "                <version>1.74</version>\n"
                                 + "                <classifier>datagen</classifier>\n"
                                 + "            </dependency>\n"
                                 + "            <dependency>\n"
                                 + "                <groupId>net.codjo.segmentation</groupId>\n"
                                 + "                <artifactId>codjo-segmentation-gui</artifactId>\n"
                                 + "                <version>1.74</version>\n"
                                 + "            </dependency>"
                                 + "</dependencies>";

        String result = "<dependencies><dependency>\n"
                        + "                <groupId>net.codjo.segmentation</groupId>\n"
                        + "                <artifactId>codjo-segmentation-common</artifactId>\n"
                        + "                <version>1.74</version>\n"
                        + "            </dependency>\n"
                        + "            <dependency>\n"
                        + "                <groupId>net.codjo.segmentation</groupId>\n"
                        + "                <artifactId>codjo-segmentation-datagen</artifactId>\n"
                        + "                <version>1.74</version>\n"
                        + "                <type>pom</type>\n"
                        + "            </dependency>\n"
                        + "            <dependency>\n"
                        + "                <groupId>net.codjo.segmentation</groupId>\n"
                        + "                <artifactId>codjo-segmentation-datagen</artifactId>\n"
                        + "                <version>1.74</version>\n"
                        + "                <classifier>datagen</classifier>\n"
                        + "            </dependency>\n"
                        + "            <dependency>\n"
                        + "                <groupId>net.codjo.segmentation</groupId>\n"
                        + "                <artifactId>codjo-segmentation-gui</artifactId>\n"
                        + "                <version>1.74</version>\n"
                        + "            </dependency>"
                        + "</dependencies>";

        Document document = new SAXBuilder().build(new StringReader(stringToProcess));
        PrepareAPomToLoadSuperPomDependenciesCommand.addTypeToDependency(document.getRootElement(),
                                                                         "pom",
                                                                         "net.codjo.segmentation",
                                                                         "codjo-segmentation-datagen",
                                                                         false);
        XmlUtil.assertEquivalent(result,
                                 PrepareAPomToLoadSuperPomDependenciesCommand.documentToString(document));
    }


    @Test
    public void test_fixPom() throws IOException, JDOMException {
        String inputPom = FileUtil.loadContent(getClass().getResource("pomToProceed.xml"));
        String etalon = FileUtil.loadContent(getClass().getResource("pomEtalon.xml"));

        String result = PrepareAPomToLoadSuperPomDependenciesCommand.fixPom(inputPom, "2.24");

        XmlUtil.assertEquivalent(etalon, result);
    }


    @Test
    public void test_addPluginsAsDependencies() throws Exception {
        String beginInput =
              "<project xmlns=\"http://maven.apache.org/POM/4.0.0\">"
              + "    <version>SNAPSHOT</version>\n"
              + "        <dependencies>\n"
              + "            <!-- ******* Externe -->\n"
              + "            <dependency>\n"
              + "                <groupId>jade</groupId>\n"
              + "                <artifactId>jade</artifactId>\n"
              + "                <version>3.5-1</version>\n"
              + "            </dependency>\n"
              + "            <dependency>\n"
              + "                <groupId>jade</groupId>\n"
              + "                <artifactId>http</artifactId>\n"
              + "                <version>3.5-1</version>\n"
              + "            </dependency>";
        String endInput = "        </dependencies>\n"
                          + "    <build>\n"
                          + "        <pluginManagement>\n"
                          + "            <plugins>\n"
                          + "                <plugin>\n"
                          + "                    <groupId>net.codjo.maven.mojo</groupId>\n"
                          + "                    <artifactId>maven-codjo-plugin</artifactId>\n"
                          + "                    <version>1.51</version>\n"
                          + "                    <dependencies>\n"
                          + "                        <dependency>\n"
                          + "                            <groupId>org.apache.maven.scm</groupId>\n"
                          + "                            <artifactId>maven-scm-provider-git-commons</artifactId>\n"
                          + "                            <version>1.0-agf</version>\n"
                          + "                        </dependency>\n"
                          + "                        <dependency>\n"
                          + "                            <groupId>org.apache.maven.scm</groupId>\n"
                          + "                            <artifactId>maven-scm-provider-gitexe</artifactId>\n"
                          + "                            <version>1.0-agf</version>\n"
                          + "                        </dependency>\n"
                          + "                    </dependencies>\n"
                          + "                    <executions>\n"
                          + "                        <execution>\n"
                          + "                            <phase>compile</phase>\n"
                          + "                            <goals>\n"
                          + "                                <goal>javac2</goal>\n"
                          + "                            </goals>\n"
                          + "                        </execution>\n"
                          + "                    </executions>\n"
                          + "                    <configuration>\n"
                          + "                        <fork>true</fork>\n"
                          + "                        <debug>true</debug>\n"
                          + "                        <verbose>true</verbose>\n"
                          + "                        <failOnError>true</failOnError>\n"
                          + "                    </configuration>\n"
                          + "                </plugin>\n"
                          + "            </plugins>\n"
                          + "        </pluginManagement>\n"
                          + "    </build>"
                          + "</project>";
        String inputXml = beginInput + endInput;

        String etalon = beginInput +
                        "        <dependency>"
                        + "            <groupId>net.codjo.pom</groupId>\n"
                        + "            <artifactId>codjo-pom</artifactId>\n"
                        + "            <version>2.24</version>\n"
                        + "            <type>pom</type>\n"
                        + "        </dependency>\n"
                        + "        <dependency>\n"
                        + "            <groupId>net.codjo.pom</groupId>\n"
                        + "            <artifactId>codjo-pom-agif</artifactId>\n"
                        + "            <version>2.24</version>\n"
                        + "            <type>pom</type>\n"
                        + "        </dependency>\n"
                        + "        <dependency>\n"
                        + "            <groupId>net.codjo.pom</groupId>\n"
                        + "            <artifactId>codjo-pom-application</artifactId>\n"
                        + "            <version>2.24</version>\n"
                        + "            <type>pom</type>\n"
                        + "        </dependency>\n"
                        + "        <dependency>\n"
                        + "            <groupId>net.codjo.pom</groupId>\n"
                        + "            <artifactId>codjo-pom-library</artifactId>\n"
                        + "            <version>2.24</version>\n"
                        + "            <type>pom</type>\n"
                        + "        </dependency>\n"
                        + "        <dependency>\n"
                        + "            <groupId>net.codjo.pom</groupId>\n"
                        + "            <artifactId>codjo-pom-plugin</artifactId>\n"
                        + "            <version>2.24</version>\n"
                        + "            <type>pom</type>\n"
                        + "        </dependency>\n"
                        + "        <dependency>\n"
                        + "            <groupId>net.codjo.pom</groupId>\n"
                        + "            <artifactId>codjo-pom-external</artifactId>\n"
                        + "            <version>2.24</version>\n"
                        + "            <type>pom</type>\n"
                        + "        </dependency>"
                        + "                <dependency>\n"
                        + "                    <groupId>net.codjo.maven.mojo</groupId>\n"
                        + "                    <artifactId>maven-codjo-plugin</artifactId>\n"
                        + "                    <version>1.51</version>\n"
                        + "              </dependency>\n"
                        + endInput;

        String actual = PrepareAPomToLoadSuperPomDependenciesCommand.completeDependencies(inputXml, "2.24");

        XmlUtil.assertEquivalent(etalon, actual);
    }
}
