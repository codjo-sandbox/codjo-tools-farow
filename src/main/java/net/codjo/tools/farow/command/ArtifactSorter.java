package net.codjo.tools.farow.command;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import net.codjo.tools.farow.Build;
import net.codjo.util.file.FileUtil;
/**
 *
 */
public class ArtifactSorter {

    private ArtifactSorter() {
    }


    public static Build[] sortBuildList(Build[] builds, URL fileUrl) throws IOException {
        return sortBuildList(builds, getListFromResource(fileUrl));
    }


    private static String[] getListFromResource(URL fileUrl) throws IOException {
        String fileContent = FileUtil.loadContent(fileUrl);

        StringTokenizer stringTokenizer = new StringTokenizer(fileContent, "\n");

        String[] contentAsList = new String[stringTokenizer.countTokens()];

        int cpt = 0;
        while (stringTokenizer.hasMoreElements()) {
            String line = stringTokenizer.nextToken();
            String trimedLine = line.trim();

            if (!"".equals(trimedLine)) {
                contentAsList[cpt] = trimedLine;
                cpt++;
            }
        }

        return contentAsList;
    }


    static Build[] sortBuildList(Build[] builds, String[] completSortedList) throws IOException {
        final Map<String, Integer> artifactMap = new HashMap<String, Integer>(completSortedList.length);

        for (int i = 0; i < completSortedList.length; i++) {
            String artifact = completSortedList[i];
            artifactMap.put(artifact, i);
        }

        Arrays.sort(builds, new Comparator<Build>() {
            public int compare(Build buildOne, Build buildTwo) {
                Integer indexOne = artifactMap.get(buildOne.exportAsString());
                Integer indexTwo = artifactMap.get(buildTwo.exportAsString());
                if (null == indexOne) {
                    throw new ArtifactSorterException("La liste n'a pas pu être triée: '" + buildOne.exportAsString()
                                                      + "' inconnu dans la liste de référence.");
                }
                if (null == indexTwo) {
                    throw new ArtifactSorterException("La liste n'a pas pu être triée: '" + buildTwo.exportAsString()
                                                      + "' inconnu dans la liste de référence.");
                }
                return indexOne.compareTo(indexTwo);
            }
        });

        return builds;
    }


    static class ArtifactSorterException extends RuntimeException {
        ArtifactSorterException(String message) {
            super(message);
        }
    }
}
