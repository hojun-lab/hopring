package com.rojojun.hopringframework.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlResource implements Resource {
    private final URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    public UrlResource(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = this.url.openStream();
        if (inputStream == null) {
            throw new MalformedURLException("This url is empty");
        }
        return inputStream;
    }
}
