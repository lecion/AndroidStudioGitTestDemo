package com.yliec.breeze;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Lecion
 * @Date 6/20/15
 * @Email lecion@icloud.com
 */
public class HttpUrlConnectionStack implements HttpStack {


    @Override
    public Response performRequest(Request<?> request) {
        HttpURLConnection conn = null;
        try {
            conn = performUrlConnection(request.getUrl());
            addRequestHeaders(conn, request);
            addRequestParams(conn, request);
            return fetchResponse(conn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Response fetchResponse(HttpURLConnection conn) throws IOException {
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        int responseCode = conn.getResponseCode();
        if (responseCode == -1) {
            throw new IOException("Could not retrive response code from HttpUrlConnection");
        }
        StatusLine responseStatus = new BasicStatusLine(protocolVersion, conn.getResponseCode(), conn.getResponseMessage());
        Response response = new Response(responseStatus);
        response.setEntity(entityFromURLConnection(conn));
        addHeadersToResponse(response, conn);
        return response;
    }

    private void addHeadersToResponse(Response response, HttpURLConnection conn) {
        for (Map.Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                Header h = new BasicHeader(header.getKey(), header.getValue().get(0));
                response.addHeader(h);
            }
        }
    }

    private HttpEntity entityFromURLConnection(HttpURLConnection conn) {
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream = null;
        try {
            inputStream = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            inputStream = conn.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength(conn.getContentLength());
        entity.setContentEncoding(conn.getContentEncoding());
        entity.setContentType(conn.getContentType());
        return entity;
    }

    private void addRequestParams(HttpURLConnection conn, Request<?> request) throws IOException {
        conn.setRequestMethod(request.getMethod());
        byte[] body = request.getBody();
        if (body != null) {
            conn.setDoOutput(true);
            conn.addRequestProperty("Content-Type", request.getBodyContentType());
            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
            dataOutputStream.write(body);
            dataOutputStream.close();
        }
    }

    private void addRequestHeaders(HttpURLConnection conn, Request<?> request) {
        Set<String> keys = request.getHeaders().keySet();
        for (String key : keys) {
            conn.addRequestProperty(key, request.getHeaders().get(key));
        }
    }

    private HttpURLConnection performUrlConnection(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setUseCaches(false);
        return (HttpURLConnection) conn;
    }

}
