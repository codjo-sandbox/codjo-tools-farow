package net.codjo.tools.farow.command;
/**
 *
 */
public enum ArtifactType {
    //TODO lib-stabilisation pour libmaven et ontologie
    PLUGIN("plugin", "C:\\Dev\\projects\\codjo\\maven\\plugins\\", "maven-${name}-plugin"),
    LIB("lib-stabilisation", "C:\\Dev\\projects\\codjo-stab\\", "codjo-${name}"),
    LIB_MAVEN("libmaven", "C:\\Dev\\projects\\codjo\\maven\\lib\\", "codjo-maven-${name}"),
    ONTOLOGIE("lib-stabilisation", "C:\\Dev\\projects\\codjo\\lib\\", "codjo-${name}"),
//    TODO  : le jour ou on gèrera automatiquement les ontology en complétant etc
//            on pourra activer la suite, pour le moment il faut les géréer comme des lib et
//            taper à la main ontology
//    ONTOLOGIE("ontology", "C:\\Dev\\projects\\codjo\\lib\\codjo-${name}-ontology"),
    SUPER_POM("lib-stabilisation pom", "C:\\Dev\\projects\\codjo-stab\\", "codjo-pom");
    private final String path;
    private String fullName;
    private final String command;


    ArtifactType(String command, String path, String fullName) {
        this.command = command;
        this.path = path + fullName;
        this.fullName = fullName;
    }


    public String toArtifactPath(String name) {
        return path.replaceAll("\\$\\{name}", name);
    }


    public String toArtifactName(String name) {
        return fullName.replaceAll("\\$\\{name}", name);
    }


    public String getCodjoCommand() {
        return command;
    }
}
