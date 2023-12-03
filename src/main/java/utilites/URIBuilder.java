package utilites;

import java.util.HashMap;

public class URIBuilder {
    private String host;
    private String path;
    private int port;
    private HashMap<String, String> parameters;

    public URIBuilder() {
    }

    public URIBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public URIBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public URIBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public URIBuilder addParameter(String key, String value) {
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }

        parameters.put(key, value);
        return this;
    }

    public String build() {
        String uri = "http://" + host + ":" + port + "/" + (path.charAt(0) == '/' ? path.substring(1) : path);

        if (parameters != null) {
            uri += "?";

            for (String key : parameters.keySet()) {
                uri += key + "=" + parameters.get(key) + "&";
            }

            // remove last ampersand
            uri = uri.substring(0, uri.length() - 1);
        }

        return uri;
    }
}