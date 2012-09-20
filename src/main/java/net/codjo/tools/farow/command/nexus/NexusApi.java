package net.codjo.tools.farow.command.nexus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import net.codjo.tools.farow.command.nexus.bean.Authentication;
import net.codjo.tools.farow.command.nexus.bean.HttpProxySettings;
import net.codjo.tools.farow.command.nexus.bean.Repository;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
/**
 *
 */
public class NexusApi {
    static final String CHARSET = "UTF-8";
    static final String SERVICE_URL = "nexus/service/local/repositories/";
    static final String PROXY_HOST = "ehttp1";
    static final int PROXY_PORT = 80;
    private String url;


    public NexusApi(String hostUrl) {
        this.url = NexusApi.getRepositoryServiceUrl(hostUrl, NexusApi.SERVICE_URL);
    }


    public Repository getRepository(String repositoryId, String apiUserName, String apiPassword)
          throws Exception {
        HttpMethod getRepositoryHttpMethod = NexusApi.buildHttpMethod(url, false, repositoryId);
        HttpMethod httpMethod = NexusApi.executeHttpMethod(apiUserName, apiPassword, getRepositoryHttpMethod);
        return NexusApi.decodeHttpResponse(httpMethod);
    }


    public Repository setProxySettings(String repositoryId,
                                       String nexusAccount, String nexusPassword,
                                       String proxyUserName, String proxyPassword)
          throws Exception {
        HttpProxySettings httpProxySettings = buildProxySettings(proxyUserName, proxyPassword);

        return setProxySettings(repositoryId, nexusAccount, nexusPassword, httpProxySettings);
    }


    public Repository removeProxySettings(String repositoryId, String nexusAccount, String nexusPassword)
          throws Exception {
        return setProxySettings(repositoryId, nexusAccount, nexusPassword, null);
    }


    private static <T> T decodeHttpResponse(HttpMethod httpMethod) throws Exception {
        StringWriter writer = getResponseAsString(httpMethod);
        XmlCodec codec = new XmlCodec();
        return (T)codec.fromXml(writer.toString());
    }


    private static HttpMethod executeHttpMethod(final String apiLogin,
                                                final String apiPassword, HttpMethod httpMethod) throws Exception {
        final HttpClient client = getHttpClient(apiLogin, apiPassword);
        client.getParams().setContentCharset(CHARSET);
        client.executeMethod(httpMethod);
        return httpMethod;
    }


    private static HttpMethod buildHttpMethod(String url, boolean isGetMethod, String repositoryId) {
        final String uri = getRepositoryServiceUrl("http://", getHostByUrl(url + repositoryId));
        return isGetMethod ? new PutMethod(uri) : new GetMethod(uri);
    }


    private static HttpMethod buildHttpPutMethod(String url, boolean post, Repository repository)
          throws UnsupportedEncodingException {
        final HttpMethod method = buildHttpMethod(url, post, repository.getData().getId());
        ((PutMethod)method).setRequestEntity(new StringRequestEntity(new XmlCodec().toXml(repository),
                                                                     "application/xml",
                                                                     CHARSET));
        return method;
    }


    static HttpClient getHttpClient(final String login, final String password) {
        final HttpClient client = new HttpClient();
        client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(login, password));
        return client;
    }


    private static String getHostByUrl(final String url) {
        return url.startsWith("https://") ?
               url.substring(8) :
               url.startsWith("http://") ? url.substring(7) : url.startsWith("git@") ? url.substring(4) : url;
    }


    private static String getRepositoryServiceUrl(String host, String serviceUrl) {
        return host + serviceUrl;
    }


    private static StringWriter getResponseAsString(HttpMethod method) throws IOException {
        final InputStream stream = method.getResponseBodyAsStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringWriter writer = new StringWriter();
        String line;
        while ((line = reader.readLine()) != null) {
            writer.append(line);
        }
        reader.close();
        return writer;
    }


    private Repository setProxySettings(String repositoryId,
                                        String nexusAccount, String nexusPassword, HttpProxySettings httpProxySettings)
          throws Exception {
        Repository repository = getRepository(repositoryId, nexusAccount,
                                              nexusPassword);

        repository.getData().getRemoteStorage().setHttpProxySettings(httpProxySettings);
        HttpMethod putHttpMethod = NexusApi.buildHttpPutMethod(url, true, repository);
        return NexusApi.decodeHttpResponse(NexusApi.executeHttpMethod(nexusAccount, nexusPassword, putHttpMethod));
    }


    private HttpProxySettings buildProxySettings(String proxyUserName, String proxyPassword) {
        HttpProxySettings newSettings = new HttpProxySettings();
        newSettings.setProxyHostname(NexusApi.PROXY_HOST);
        newSettings.setProxyPort(NexusApi.PROXY_PORT);
        newSettings.setAuthentication(new Authentication(proxyUserName, proxyPassword));
        return newSettings;
    }
}
