package net.codjo.tools.farow.step;
import net.codjo.tools.farow.command.ArtifactType;
import net.codjo.tools.farow.command.CommandPlayer;
public abstract class ArtifactStep extends Step {
    public static final String REMOTE_CODJO = "-Dremote=codjo";
    private ArtifactType type;


    protected ArtifactStep(ArtifactType artifactType, String name, CommandPlayer player) {
        super(name, player);
        type = artifactType;
    }


    public String exportAsString() {
        return String.valueOf(getType()).toLowerCase() + " " + getName();
    }


    public ArtifactType getType() {
        return type;
    }


    public abstract String getAuditMessage();


    public static String getGitScmAdditionalParameter(ArtifactType type, String name) {
        // ATTENTION  CODE EXECUTE A L OUVERTURE DE FAROW !!!
        return "-DconnectionUrl=scm:git:file:///" + type.toArtifactPath(name) + "\\.git";
    }


    public static String getCodjoDeploymentParameter() {
        return "-Darguments='" + REMOTE_CODJO + "'";
    }


    static boolean isGithubAware(ArtifactType type, String name) {
        return !"".equals(getGitScmAdditionalParameter(type, name));
    }


    @Override
    protected State getFinishingState() {
        if (isGithubAware(getType(), getName())) {
            return State.TO_BE_PUBLISHED;
        }
        return super.getFinishingState();
    }
}