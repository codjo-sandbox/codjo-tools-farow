package net.codjo.tools.farow;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
/**
 *
 */
public class Farow {
    private Farow() {
    }


    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Properties properties = new Properties();
                for (int i = 0; i + 1 < args.length; i += 2) {
                    properties.put(args[i].substring(1), args[i + 1]);
                }
                String version = properties.getProperty("version");
                JFrame frame = new JFrame("F.A.R.O.W. (FAst Release On Wednesday) - Codjo - " + version);
                frame.setContentPane(new ReleaseForm(properties).getMainPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
