package net.codjo.tools.farow.command.nexus.bean;
/**
 *
 */
public class HttpProxySettings {
    private String proxyHostname;
    private int proxyPort;
    private Authentication authentication;
    private String nonProxyHosts;


    public String getProxyHostname() {
        return proxyHostname;
    }


    public void setProxyHostname(String proxyHostname) {
        this.proxyHostname = proxyHostname;
    }


    public int getProxyPort() {
        return proxyPort;
    }


    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }


    public Authentication getAuthentication() {
        return authentication;
    }


    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }


    public String getNonProxyHosts() {
        return nonProxyHosts;
    }


    public void setNonProxyHosts(String nonProxyHosts) {
        this.nonProxyHosts = nonProxyHosts;
    }
}
