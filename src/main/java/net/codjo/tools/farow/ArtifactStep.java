package net.codjo.tools.farow;
import net.codjo.tools.farow.command.ArtifactType;
import net.codjo.tools.farow.command.CommandPlayer;
public abstract class ArtifactStep extends Step {
    static final String REMOTE_CODJO = "-Dremote=codjo";
    private ArtifactType type;


    protected ArtifactStep(ArtifactType artifactType, String name, CommandPlayer player) {
        super(name, player);
        type = artifactType;
    }


    public String exportAsString() {
        return String.valueOf(getType()).toLowerCase() + " " + getName();
    }


    static String getGitScmAdditionalParameter(ArtifactType type, String name) {
        if (type.equals(ArtifactType.LIB) || type.equals(ArtifactType.SUPER_POM)) {
            return "-DconnectionUrl=scm:git:file:///" + type.toArtifactPath(name) + "\\.git";
        }
        return "";
    }


    static String getCodjoDeploymentParameter() {
        return "-Darguments='" + REMOTE_CODJO + "'";
    }


    @Override
    protected State getFinishingState() {
        if (isGithubAware(getType(), getName())) {
            return State.TO_BE_PUBLISHED;
        }
        return super.getFinishingState();
    }


    public ArtifactType getType() {
        return type;
    }


    static boolean isGithubAware(ArtifactType type, String name) {
        return !"".equals(getGitScmAdditionalParameter(type, name));
    }


    protected abstract String getAuditMessage();
}