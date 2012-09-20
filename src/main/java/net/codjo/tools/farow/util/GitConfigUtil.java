package net.codjo.tools.farow.util;
import java.io.File;
import java.io.IOException;
import net.codjo.util.file.FileUtil;

import static java.lang.System.getProperty;
/**
 *
 */
public class GitConfigUtil {
    private String proxyUserName;
    private String proxyPasword;
    private String proxyHost;
    private int proxyPort;


    public GitConfigUtil() throws IOException {
        this(new File(getUserHome(), ".gitconfig"));
    }


    public GitConfigUtil(File file) throws IOException {
        String[] gitConfigFile = FileUtil.loadContentAsLines(file);
        boolean categoryFound = false;

        for (String configLine : gitConfigFile) {
            if (!categoryFound) {
                categoryFound = "[http]".equalsIgnoreCase(configLine.trim());
            }
            else {
                String lineNospace = configLine.trim().replaceAll(" ", "").replaceAll("\t", "");
                String prefix = "proxy=";
                if (lineNospace.startsWith(prefix)) {
                    int delimiterPosition = lineNospace.indexOf("@");

                    String httpProxyAuthChain = lineNospace.substring(prefix.length(), delimiterPosition);
                    String[] split = httpProxyAuthChain.split(":");
                    proxyUserName = split[0];
                    proxyPasword = split[1];

                    String httpProxyChain = lineNospace.substring(delimiterPosition + 1);
                    split = httpProxyChain.split(":");
                    proxyHost = split[0];
                    proxyPort = Integer.parseInt(split[1]);
                    break;
                }

                if (lineNospace.startsWith("[")) {
                    break;
                }
            }
        }
    }


    public String getProxyUserName() {
        return removeDuplicatesBackSlashes(proxyUserName);
    }


    public String getProxyPassword() {
        return proxyPasword;
    }


    public String getProxyHost() {
        return proxyHost;
    }


    public int getProxyPort() {
        return proxyPort;
    }


    static String removeDuplicatesBackSlashes(String s) {
        StringBuilder noDupes = new StringBuilder();
        String[] split = s.split("\\\\");
        String previous = null;
        for (String str : split) {
            if (!"".equals(str)) {
                noDupes.append(str);
            }
            if (previous != null && !"".equals(previous)) {
                noDupes.append("\\");
            }
            previous = str;
        }
        return noDupes.toString();
    }


    private static File getUserHome() {
        return new File(getProperty("user.home"));
    }
}
