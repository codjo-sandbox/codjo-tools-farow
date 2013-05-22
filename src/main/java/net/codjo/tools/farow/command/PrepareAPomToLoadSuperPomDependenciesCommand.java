package net.codjo.tools.farow.command;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.codjo.tools.farow.Display;
import net.codjo.util.file.FileUtil;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.Filter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
/**
 *
 */
public class PrepareAPomToLoadSuperPomDependenciesCommand extends Command {
    private static final String LS = System.getProperty("line.separator");
    static final String NET_CODJO_PREFIX = "net.codjo";

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


    static String fixPom(String pom, String frameworkVersion) throws JDOMException, IOException {
        pom = pom.replace("<dependencyManagement>", "").replace("</dependencyManagement>", "");
        pom = pom.replace("<artifactId>codjo-pom</artifactId>", "<artifactId>codjo-pom-alldeps</artifactId>");
        pom = pom.replace("<groupId>commons-jexl</groupId>", "<groupId>org.apache.commons</groupId>");

        pom = completeDependencies(pom, frameworkVersion);

        return pom;
    }


    public static String completeDependencies(String inputXml, final String frameworkVersion)
          throws JDOMException, IOException {
        Document document = new SAXBuilder().build(new StringReader(inputXml));

        document = completeDocument(document, frameworkVersion);

        return documentToString(document);
    }


    public static String documentToString(Document document) throws IOException {
        // rewrite DOM as a string to find differences, since text outside the root element is not tracked
        StringWriter writer = new StringWriter();
        Format format = Format.getPrettyFormat();
        format.setLineSeparator(LS);
        format.setIndent("    ");
        XMLOutputter out = new XMLOutputter(format);
        out.output(document.getRootElement(), writer);

        return writer.toString();
    }


    public static Document completeDocument(Document document, String frameworkVersion) {
        Element rootElement = document.getRootElement();
        Namespace namespace = rootElement.getNamespace();
        Element dependencies = rootElement.getChild("dependencies", namespace);

        addSuperPomModulesAsDependencies(dependencies, frameworkVersion);
        addPluginsAsDependencies(document, dependencies);
        addTypeToClassifierInDependencies(dependencies, "xml", "datagen");
        addTypeToClassifierInDependencies(dependencies, "xml", "datagen-selector");
        addTypeToClassifierInDependencies(dependencies, "zip", "sql");
        addTypeToDependency(dependencies, "pom", "net.codjo.imports", "codjo-imports-plugin-filter", false);
        addTypeToDependency(dependencies, "pom", "net.codjo.segmentation", "codjo-segmentation-datagen", false);

        return document;
    }


    protected static void addSuperPomModulesAsDependencies(Element dependencies, String frameworkVersion) {
        Namespace namespace = dependencies.getNamespace();
        Element dependency = new Element("dependency", namespace);
        Element artifactId = new Element("artifactId", namespace);

        dependency.addContent(new Element("groupId", namespace).setText("net.codjo.pom"));
        dependency.addContent(artifactId.setText("codjo-pom"));
        dependency.addContent(new Element("version", namespace).setText(frameworkVersion));
        dependency.addContent(new Element("type", namespace).setText("pom"));

        artifactId.setText("codjo-pom");
        dependencies.addContent((Content)dependency.clone());

        artifactId.setText("codjo-pom-agif");
        dependencies.addContent((Content)dependency.clone());

        artifactId.setText("codjo-pom-application");
        dependencies.addContent((Content)dependency.clone());

        artifactId.setText("codjo-pom-library");
        dependencies.addContent((Content)dependency.clone());

        artifactId.setText("codjo-pom-plugin");
        dependencies.addContent((Content)dependency.clone());

        artifactId.setText("codjo-pom-external");
        dependencies.addContent((Content)dependency.clone());
    }


    protected static void addPluginsAsDependencies(Document document, Element dependencies) {
        Namespace namespace = dependencies.getNamespace();
        Iterator plugins = document.getDescendants(new CodjoPluginFilter());
        while (plugins.hasNext()) {
            Element element = (Element)plugins.next();
            Element newDependency = new Element("dependency", namespace);
            newDependency.addContent((Content)element.getChild("groupId", namespace).clone());
            newDependency.addContent((Content)element.getChild("artifactId", namespace).clone());
            newDependency.addContent((Content)element.getChild("version", namespace).clone());

            dependencies.addContent(newDependency);
        }
    }


    protected static void addTypeToClassifierInDependencies(Element dependencies,
                                                            String type,
                                                            String classifier) {
        addTypeOnFilter(dependencies, type, new ClassifierDependenciesFilter(classifier));
    }


    protected static void addTypeToDependency(Element dependencies,
                                              String type,
                                              String groupId, String artifactId, boolean withClassifier) {
        addTypeOnFilter(dependencies, type, new DependencyFilter(groupId, artifactId, withClassifier));
    }


    private static void addTypeOnFilter(Element dependencies, String type, Filter filter) {
        Iterator plugins = dependencies.getDescendants(filter);
        List<Element> myDependencies = new ArrayList<Element>();
        while (plugins.hasNext()) {
            myDependencies.add((Element)plugins.next());
        }

        for (Element element : myDependencies) {
            element.addContent(new Element("type", dependencies.getNamespace()).setText(type));
        }
    }


    private static class CodjoPluginFilter implements Filter {
        public boolean matches(Object obj) {
            if (obj instanceof Element) {
                Element element = (Element)obj;

                if (element.getName().matches("plugin")) {
                    Element elementGroupId = element.getChild("groupId", element.getNamespace());
                    if (elementGroupId != null && elementGroupId.getValue().startsWith(NET_CODJO_PREFIX)) {

                        Element parentElement = element.getParentElement();

                        if (parentElement.getName().matches("plugins")) {
                            Element grantParentElement = parentElement.getParentElement();

                            if (grantParentElement.getName().matches("pluginManagement")) {
                                return grantParentElement.getParentElement().getName().matches("build");
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
    private static class ClassifierDependenciesFilter implements Filter {
        private String classifier;


        ClassifierDependenciesFilter(String classifier) {
            this.classifier = classifier;
        }


        public boolean matches(Object obj) {
            if (obj instanceof Element) {
                Element element = (Element)obj;

                if (element.getName().matches("dependency")) {
                    Element elementClassifier = element.getChild("classifier", element.getNamespace());
                    if (elementClassifier != null && elementClassifier.getText().matches(classifier)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    private static class DependencyFilter implements Filter {
        private String groupId;
        private String artifactId;
        private boolean withClassifier;


        DependencyFilter(String groupId, String artifactId, boolean withClassifier) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.withClassifier = withClassifier;
        }


        public boolean matches(Object obj) {
            if (obj instanceof Element) {
                Element element = (Element)obj;

                if (element.getName().matches("dependency")) {
                    Element elementGroupId = element.getChild("groupId", element.getNamespace());
                    Element elementArtifactId = element.getChild("artifactId", element.getNamespace());
                    Element elementClassifier = element.getChild("classifier", element.getNamespace());
                    if (elementGroupId != null && elementGroupId.getText().matches(groupId)
                        && elementArtifactId != null && elementArtifactId.getText().matches(artifactId)
                        && ((elementClassifier != null) == withClassifier)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
