package net.codjo.tools.farow.command;
import java.io.IOException;
import net.codjo.test.common.XmlUtil;
import net.codjo.util.file.FileUtil;
import org.junit.Test;
/**
 *
 */
public class PrepareAPomToLoadSuperPomDependenciesCommandTest {

    @Test
    public void test_addPomTypeToCodjoSegmentationDatagenArtifact() {
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

        XmlUtil.assertEquivalent(result,
                                 PrepareAPomToLoadSuperPomDependenciesCommand.addTypeToDependency(stringToProcess,
                                                                                                  "net.codjo.segmentation",
                                                                                                  "codjo-segmentation-datagen",
                                                                                                  "pom"));
    }


    @Test
    public void test_fixPom() throws IOException {
        String inputPom = FileUtil.loadContent(getClass().getResource("pomToProceed.xml"));
        String etalon = FileUtil.loadContent(getClass().getResource("pomEtalon.xml"));

        String result = PrepareAPomToLoadSuperPomDependenciesCommand.fixPom(inputPom, "2.24");

        XmlUtil.assertEquivalent(etalon, result);
    }
}
