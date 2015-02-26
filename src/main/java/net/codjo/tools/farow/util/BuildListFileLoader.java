package net.codjo.tools.farow.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.codjo.tools.farow.command.ArtifactType;
import net.codjo.tools.farow.step.Build;
/**
 *
 */
public class BuildListFileLoader {
    public List<Build> load(File file, GitConfigUtil gitConfig) throws IOException {
        final List<Build> builds = new ArrayList<Build>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        while (line != null) {
            if ("".equals(line.trim())) {
                break;
            }

            ArtifactType artifactType = null;
            String[] words = line.split(" ");
            String artifact = words[0];
            if ("lib".equalsIgnoreCase(artifact)) {
                artifactType = ArtifactType.LIB;
            }
            else if ("plugin".equalsIgnoreCase(artifact)) {
                artifactType = ArtifactType.PLUGIN;
            }
            else if ("libmaven".equalsIgnoreCase(artifact)) {
                artifactType = ArtifactType.LIB_MAVEN;
            }
            else if ("ontology".equalsIgnoreCase(artifact)) {
                artifactType = ArtifactType.ONTOLOGIE;
            }

            builds.add(new Build(artifactType, words[1], gitConfig));
            line = bufferedReader.readLine();
        }

        return builds;
    }
}
