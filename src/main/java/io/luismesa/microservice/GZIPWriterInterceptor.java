/*
 * The MIT License
 *
 * Copyright 2023 Luis Daniel Mesa Velásquez.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.luismesa.microservice;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
@Provider
@Compress
public final class GZIPWriterInterceptor implements WriterInterceptor {

    /**
     * Injected call headers
     *
     */
    @Context
    private HttpHeaders httpHeaders;

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        final MultivaluedMap<String, String> requestHeaders
                = httpHeaders.getRequestHeaders();
        Optional.ofNullable(requestHeaders.get(HttpHeaders.ACCEPT_ENCODING))
                .orElse(new ArrayList<>()).stream().map(o -> String.valueOf(o))
                .filter(h -> h.contains("gzip")).findFirst().ifPresent(g -> {
            MultivaluedMap<String, Object> headers
                    = context.getHeaders();
            final OutputStream outputStream = context.getOutputStream();
            try {
                context.setOutputStream(
                        new GZIPOutputStream(outputStream));
                headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");
            } catch (Throwable ex) {
                Logger.getLogger(GZIPWriterInterceptor.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
        context.proceed();
    }
}
