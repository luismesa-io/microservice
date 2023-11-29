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

import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.WriterInterceptorContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GZIPWriterInterceptorTest {

    @Mock
    private HttpHeaders httpHeaders;
    @Mock
    private MultivaluedMap<String, String> requestHeaders;

    @Mock
    private WriterInterceptorContext context;
    @Mock
    private MultivaluedMap<String, Object> headers;
    @Mock
    private OutputStream outputStream;
    @Captor
    ArgumentCaptor<OutputStream> outputStreamCaptor;

    @InjectMocks
    private GZIPWriterInterceptor gzipWriterInterceptor;

    public GZIPWriterInterceptorTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        doAnswer((Answer<MultivaluedMap<String, String>>) (InvocationOnMock invocation) -> {
            return requestHeaders;
        }).when(httpHeaders).getRequestHeaders();

        doAnswer((Answer<MultivaluedMap<String, Object>>) (InvocationOnMock invocation) -> {
            return headers;
        }).when(context).getHeaders();
        doAnswer((Answer<OutputStream>) (InvocationOnMock invocation) -> {
            return outputStream;
        }).when(context).getOutputStream();
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of aroundWriteTo method, of class GZIPWriterInterceptor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAroundWriteTo() throws Exception {
        doAnswer((Answer<List<String>>) (InvocationOnMock invocation) -> {
            return List.of("gzip");
        }).when(requestHeaders).get(HttpHeaders.ACCEPT_ENCODING);
        gzipWriterInterceptor.aroundWriteTo(context);
        verify(httpHeaders, times(1)).getRequestHeaders();
        verify(requestHeaders, times(1)).get(HttpHeaders.ACCEPT_ENCODING);
        verify(context, times(1)).getHeaders();
        verify(context, times(1)).setOutputStream(outputStreamCaptor.capture());
        assertTrue(outputStreamCaptor.getValue() instanceof GZIPOutputStream);
        verify(headers, times(1)).add(HttpHeaders.CONTENT_ENCODING, "gzip");
        verify(context, times(1)).proceed();

    }

    /**
     * Test of aroundWriteTo method with exception, of class GZIPWriterInterceptor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAroundWriteToWithException() throws Exception {
        doAnswer((Answer<List<String>>) (InvocationOnMock invocation) -> {
            return List.of("gzip");
        }).when(requestHeaders).get(HttpHeaders.ACCEPT_ENCODING);
        doThrow(RuntimeException.class).when(context).setOutputStream(any(OutputStream.class));
        gzipWriterInterceptor.aroundWriteTo(context);
        verify(httpHeaders, times(1)).getRequestHeaders();
        verify(requestHeaders, times(1)).get(HttpHeaders.ACCEPT_ENCODING);
        verify(context, times(1)).getHeaders();
        verify(context, times(1)).setOutputStream(outputStreamCaptor.capture());
        verify(headers, never()).add(HttpHeaders.CONTENT_ENCODING, "gzip");
        verify(context, times(1)).proceed();
    }

    /**
     * Test of aroundWriteTo method without accept encoding, of class GZIPWriterInterceptor.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testAroundWriteToWithoutAcceptEncoding() throws Exception {
        doAnswer((Answer<List<String>>) (InvocationOnMock invocation) -> {
            return null;
        }).when(requestHeaders).get(HttpHeaders.ACCEPT_ENCODING);
        doThrow(RuntimeException.class).when(context).setOutputStream(any(OutputStream.class));
        gzipWriterInterceptor.aroundWriteTo(context);
        verify(httpHeaders, times(1)).getRequestHeaders();
        verify(requestHeaders, times(1)).get(HttpHeaders.ACCEPT_ENCODING);
        verify(context, never()).getHeaders();
        verify(context, never()).setOutputStream(any(OutputStream.class));
        verify(headers, never()).add(HttpHeaders.CONTENT_ENCODING, "gzip");
        verify(context, times(1)).proceed();

    }

}
