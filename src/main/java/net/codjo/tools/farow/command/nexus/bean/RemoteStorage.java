package net.codjo.tools.farow.command.nexus.bean;
/**
 *
 */
public class RemoteStorage {
    String remoteStorageUrl;
    private HttpProxySettings httpProxySettings;


    public HttpProxySettings getHttpProxySettings() {
        return httpProxySettings;
    }


    public void setHttpProxySettings(HttpProxySettings httpProxySettings) {
        this.httpProxySettings = httpProxySettings;
    }
}
