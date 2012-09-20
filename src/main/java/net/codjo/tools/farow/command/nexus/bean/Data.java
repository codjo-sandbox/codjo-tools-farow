package net.codjo.tools.farow.command.nexus.bean;
/**
 *
 */
public class Data {
    private String id;
    private RemoteStorage remoteStorage;
    String contentResourceURI;
    String name;
    String provider;
    String providerRole;
    String format;
    String repoType;
    boolean exposed;
    String writePolicy;
    boolean browseable;
    boolean indexable;
    String notFoundCacheTTL;
    String repoPolicy;
    String checksumPolicy;
    boolean downloadRemoteIndexes;
    String defaultLocalStorageUrl;
    boolean fileTypeValidation;
    int artifactMaxAge;
    int metadataMaxAge;
    boolean autoBlockActive;


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public RemoteStorage getRemoteStorage() {
        return remoteStorage;
    }


    public void setRemoteStorage(RemoteStorage remoteStorage) {
        this.remoteStorage = remoteStorage;
    }
}
