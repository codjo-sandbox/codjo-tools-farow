package net.codjo.tools.farow.command;
import java.net.URL;
import javax.swing.DefaultListModel;
import net.codjo.tools.farow.step.Build;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
/**
 *
 */
public class ArtifactSorterTest {
    @Test
    public void test_sortBuildList() throws Exception {
        Build[] before = new Build[4];

        before[0] = new Build(ArtifactType.LIB, "fromage");
        before[1] = new Build(ArtifactType.LIB, "dessert");
        before[2] = new Build(ArtifactType.PLUGIN, "salade");
        before[3] = new Build(ArtifactType.LIB, "entree");

        Build[] orderedBuilds = ArtifactSorter.sortBuildList(before,
                                                             new String[]{"plugin apero", "lib entree", "lib viande",
                                                                          "plugin salade", "lib fromage",
                                                                          "lib dessert"});

        assertTrue("lib entree".equals(orderedBuilds[0].exportAsString()));
        assertTrue("plugin salade".equals(orderedBuilds[1].exportAsString()));
        assertTrue("lib fromage".equals(orderedBuilds[2].exportAsString()));
        assertTrue("lib dessert".equals(orderedBuilds[3].exportAsString()));
    }


    @Test
    public void test_sortBuildList_nameNotFound() throws Exception {
        Build[] before = new Build[4];

        before[0] = new Build(ArtifactType.LIB, "fromage");
        before[1] = new Build(ArtifactType.LIB, "dessert");
        before[2] = new Build(ArtifactType.PLUGIN, "salade");
        before[3] = new Build(ArtifactType.LIB, "entree");

        try {
            ArtifactSorter.sortBuildList(before,
                                         new String[]{"plugin apero"});
            fail("Artifact Inconnu : on aurait du avoir une exception");
        }
        catch (ArtifactSorter.ArtifactSorterException e) {
            e.printStackTrace();
            assertTrue(e.getMessage()
                             .contains(
                                   "La liste n'a pas pu être triée: 'lib fromage' inconnu dans la liste de référence."));
        }
    }


    @Test
    public void test_sortBuildList_justOneNameNotFound() throws Exception {
        Build[] before = new Build[1];

        before[0] = new Build(ArtifactType.LIB, "fromage");
        Build[] orderedBuilds = ArtifactSorter.sortBuildList(before, new String[]{"plugin apero"});

        assertTrue("lib fromage".equals(orderedBuilds[0].exportAsString()));
    }


    @Test
    public void test_sortBuildListWithFile() throws Exception {
        Build[] before = new Build[4];

        before[0] = new Build(ArtifactType.LIB, "fromage");
        before[1] = new Build(ArtifactType.LIB, "dessert");
        before[2] = new Build(ArtifactType.PLUGIN, "salade");
        before[3] = new Build(ArtifactType.LIB, "entree");

        URL fileEtalon = getClass().getResource("MonFichierEtalon.txt");
        Build[] orderedBuilds = ArtifactSorter.sortBuildList(before, fileEtalon);

        assertTrue("lib entree".equals(orderedBuilds[0].exportAsString()));
        assertTrue("plugin salade".equals(orderedBuilds[1].exportAsString()));
        assertTrue("lib fromage".equals(orderedBuilds[2].exportAsString()));
        assertTrue("lib dessert".equals(orderedBuilds[3].exportAsString()));
    }


    @Test
    public void test_cast() throws Exception {
        DefaultListModel model = new DefaultListModel();
        model.add(0, new Build(ArtifactType.LIB, "fromage"));

        Build[] tb = new Build[model.size()];
        model.copyInto(tb);
    }
}
