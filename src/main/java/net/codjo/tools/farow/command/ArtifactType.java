package net.codjo.tools.farow.command;
/**
 *
 */
public enum ArtifactType {
    //TODO lib-stabilisation pour libmaven et ontologie
    PLUGIN("plugin", "C:\\Dev\\projects\\codjo-stab\\", "maven-${name}-plugin"),
    LIB("lib", "C:\\Dev\\projects\\codjo-stab\\", "codjo-${name}"),
    LIB_MAVEN("libmaven", "C:\\Dev\\projects\\codjo-stab\\", "codjo-maven-${name}"),
    ONTOLOGIE("lib", "C:\\Dev\\projects\\codjo-stab\\", "codjo-${name}"),
    //    TODO  : le jour ou on gèrera automatiquement les ontology en complétant etc
//            on pourra activer la suite, pour le moment il faut les géréer comme des lib et
//            taper à la main ontology
//    ONTOLOGIE("ontology", "C:\\Dev\\projects\\codjo\\lib\\codjo-${name}-ontology"),
    SUPER_POM("lib pom", "C:\\Dev\\projects\\codjo-stab\\", "codjo-pom");
    private final String path;
    private String fullName;
    private final String command;


    ArtifactType(String command, String path, String fullName) {
        this.command = command;
        this.path = path;
        this.fullName = fullName;
    }


    public String toArtifactPath(String name) {
        return (path + fullName).replaceAll("\\$\\{name}", name);
    }


    public String toArtifactName(String name) {
        return fullName.replaceAll("\\$\\{name}", name);
    }


    public String getCodjoCommand() {
        return command;
    }


    public String getGithubAccount() {
        return "codjo";
    }


    public String getWorkingDirectory() {
        return path;
    }

}
