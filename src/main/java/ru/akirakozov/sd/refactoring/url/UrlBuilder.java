package ru.akirakozov.sd.refactoring.url;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class UrlBuilder {

    public URL buildUrl(Config config, List<Parameter> parameters) {
        String url = buildUrlString(config, parameters);
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed url: " + url);
        }
    }

    public URL buildUrl(Config config) {
        return buildUrl(config, List.of());
    }

    private String buildUrlString(Config config, List<Parameter> parameters) {
        StringBuilder url = new StringBuilder();
        url.append("http://")
                .append(config.host)
                .append(":")
                .append(config.port)
                .append("/")
                .append(config.request);

        if (parameters.isEmpty()) return url.toString();

        url.append("?");
        String parameterString = parameters.stream()
                .map(p -> p.key + "=" + p.value)
                .collect(Collectors.joining("&"));
        url.append(parameterString);

        return url.toString();
    }
}
