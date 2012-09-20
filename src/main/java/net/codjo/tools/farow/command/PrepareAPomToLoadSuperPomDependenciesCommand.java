package net.codjo.tools.farow.command;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.codjo.util.file.FileUtil;
/**
 *
 */
public class PrepareAPomToLoadSuperPomDependenciesCommand extends Command {
    private String destinationDirectory;
    private String frameworkVersion;


    public PrepareAPomToLoadSuperPomDependenciesCommand(String destinationDirectory, String frameworkVersion) {
        super("Fabrication d'un pom avec toutes les dépendances du super-pom");
        this.destinationDirectory = destinationDirectory;
        this.frameworkVersion = frameworkVersion;
    }


    @Override
    public void execute(Display display) throws Exception {
        File file = new File(destinationDirectory + "\\pom.xml");
        String pom = FileUtil.loadContent(file);

        pom = fixPom(pom, frameworkVersion);

        FileUtil.saveContent(file, pom);
    }


    static String fixPom(String pom, String frameworkVersion) {
        pom = pom.replace("<dependencyManagement>", "").replace("</dependencyManagement>", "");
        pom = pom.replace("<artifactId>codjo-pom</artifactId>", "<artifactId>codjo-pom-alldeps</artifactId>");
        pom = pom.replace("<dependencies>", "<dependencies><dependency>\n"
                                            + "            <groupId>net.codjo.pom</groupId>\n"
                                            + "            <artifactId>codjo-pom</artifactId>\n"
                                            + "            <version>" + frameworkVersion + "</version>\n"
                                            + "            <type>pom</type>\n"
                                            + "        </dependency>\n"
                                            + "        <dependency>\n"
                                            + "            <groupId>net.codjo.pom</groupId>\n"
                                            + "            <artifactId>codjo-pom-agif</artifactId>\n"
                                            + "            <version>" + frameworkVersion + "</version>\n"
                                            + "            <type>pom</type>\n"
                                            + "        </dependency>\n"
                                            + "        <dependency>\n"
                                            + "            <groupId>net.codjo.pom</groupId>\n"
                                            + "            <artifactId>codjo-pom-application</artifactId>\n"
                                            + "            <version>" + frameworkVersion + "</version>\n"
                                            + "            <type>pom</type>\n"
                                            + "        </dependency>\n"
                                            + "        <dependency>\n"
                                            + "            <groupId>net.codjo.pom</groupId>\n"
                                            + "            <artifactId>codjo-pom-library</artifactId>\n"
                                            + "            <version>" + frameworkVersion + "</version>\n"
                                            + "            <type>pom</type>\n"
                                            + "        </dependency>\n"
                                            + "        <dependency>\n"
                                            + "            <groupId>net.codjo.pom</groupId>\n"
                                            + "            <artifactId>codjo-pom-plugin</artifactId>\n"
                                            + "            <version>" + frameworkVersion + "</version>\n"
                                            + "            <type>pom</type>\n"
                                            + "        </dependency>\n"
                                            + "        <dependency>\n"
                                            + "            <groupId>net.codjo.pom</groupId>\n"
                                            + "            <artifactId>codjo-pom-external</artifactId>\n"
                                            + "            <version>" + frameworkVersion + "</version>\n"
                                            + "            <type>pom</type>\n"
                                            + "        </dependency>");
        pom = pom.replace("<classifier>datagen</classifier>", "<classifier>datagen</classifier><type>xml</type>");
        pom = pom.replace("<classifier>datagen-selector</classifier>",
                          "<classifier>datagen-selector</classifier><type>xml</type>");

        pom = pom.replace("<classifier>sql</classifier>", "<classifier>sql</classifier><type>zip</type>");

        pom = addTypeToDependency(pom, "net.codjo.imports", "codjo-imports-plugin-filter", "pom");
        pom = addTypeToDependency(pom, "net.codjo.segmentation", "codjo-segmentation-datagen", "pom");

        pom = pom.replace("<groupId>commons-jexl</groupId>", "<groupId>org.apache.commons</groupId>");
        return pom;
    }


    static String addTypeToDependency(String xmlInput, String groupId, String artifactId, String type) {
        String debut
              = "<dependency><groupId>" + groupId + "</groupId><artifactId>" + artifactId + "</artifactId><version>";
        String fin = "</version></dependency>";

        String pattern = debut + "([0-9]*\\.[0-9]*)" + fin;

        String removeWhiteSpaces = xmlInput.replaceAll(">\\s+<", "><");

        Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(removeWhiteSpaces);
        return matcher.replaceFirst(debut + "$1" + "</version><type>" + type + "</type></dependency>");
    }


    static String removeDependency(String xmlInput, String groupId, String artifactId) {
        String debut
              = "<dependency><groupId>" + groupId + "</groupId><artifactId>" + artifactId + "</artifactId><version>";
        String fin = "</version></dependency>";

        String pattern = debut + "([0-9]*\\.[0-9]*)" + fin;

        String removeWhiteSpaces = xmlInput.replaceAll(">\\s+<", "><");

        Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(removeWhiteSpaces);
        return matcher.replaceAll("");
    }
}
