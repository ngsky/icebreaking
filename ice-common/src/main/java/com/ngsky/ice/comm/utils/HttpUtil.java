package com.ngsky.ice.comm.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import static org.apache.http.Consts.UTF_8;

/**
 * <dl>
 * <dt>HttpUtil</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 4/5/2019 3:01 PM</dd>
 * </dl>
 *
 * @author daxiong
 */
@Slf4j
public class HttpUtil {
    public final static int HTTP_STATUS_OK = 200;
    public final static int HTTP_STATUS_ERROR = 500;
    public final static int HTTP_STATUS_NO_AUTH = 401;
    public final static int HTTP_STATUS_FORBIDDEN = 403;
    public final static int HTTP_STATUS_NO_EXIST = 404;

    public static final String HTTPS = "https";
    public static final String HTTP = "http";
    public static final String COOKIE_DOMAIN = "yuefu68.com";

    public static CloseableHttpClient getHttpClient()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register(HTTP, plainSF);
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        TrustStrategy anyTrustStrategy = new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        };
        SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
        LayeredConnectionSocketFactory sslSF =
                new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        registryBuilder.register(HTTPS, sslSF);
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }

    public static HttpResult doGet(String url) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        if (url.contains(HTTPS)) {
            httpClient = getHttpClient();
        }
        log.info("begin doGet, url is {}", url);
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        int httpCode = httpResponse.getStatusLine().getStatusCode();
        String result = EntityUtils.toString(httpEntity, UTF_8);
        log.info("httpcode : [{}], result : [{}]", httpCode, result);
        HttpResult httpResult = new HttpResult();
        httpResult.setHttpCode(httpCode);
        httpResult.setResult(result);
        return httpResult;
    }

    public static HttpResult doPost(String url, Map<String, String> params) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        if (url.contains(HTTPS)) {
            httpClient = getHttpClient();
        }
        log.info("begin doPost, url is {}, params is {}", url, params);
        HttpPost httpPost = new HttpPost(url);
        if (params != null && params.size() > 0) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            HttpEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList, UTF_8);
            httpPost.setEntity(urlEncodedFormEntity);
        }
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        int httpCode = httpResponse.getStatusLine().getStatusCode();
        String result = EntityUtils.toString(httpEntity, UTF_8);
        Header[] headers = httpResponse.getAllHeaders();
        Map<String, String> headerMap = new HashMap<String,String>(headers.length);
        if (headers != null && headers.length > 0) {
            for (Header header : headers) {
                if (headerMap.containsKey(header.getName())) {
                    String value = headerMap.get(header.getName()) + ";" + header.getValue();
                    headerMap.put(header.getName(), value);
                } else {
                    headerMap.put(header.getName(), header.getValue());
                }
            }
        }
        log.info("httpcode : [{}], result : [{}]", httpCode, result);
        log.info("headers is {}", headerMap);
        HttpResult httpResult = new HttpResult();
        httpResult.setHttpCode(httpCode);
        httpResult.setResult(result);
        httpResult.setHeaders(headerMap);
        return httpResult;
    }

    public static org.apache.http.cookie.Cookie[] getHttpClientCookies(Cookie[] cookies) {

        int numberOfCookies = 0;
        if (cookies != null) {
            numberOfCookies = cookies.length;
        }
        org.apache.http.cookie.Cookie[] httpClientCookies = new org.apache.http.cookie.Cookie[numberOfCookies];
        for (int i = 0; i < numberOfCookies; i++) {
            Cookie c = cookies[i];
            String domain = c.getDomain();
            String name = c.getName();
            String value = c.getValue();
            String path = c.getPath();
            boolean secure = c.getSecure();
            int maxAge = c.getMaxAge();
            org.apache.http.impl.cookie.BasicClientCookie hCookie = new org.apache.http.impl.cookie.BasicClientCookie(name, value);
            hCookie.setDomain(domain);
            hCookie.setPath(path);
            hCookie.setSecure(secure);
            long expireMillion = System.currentTimeMillis() + maxAge * 1000;
            hCookie.setExpiryDate(new Date(expireMillion));
            httpClientCookies[i] = hCookie;
        }
        return httpClientCookies;
    }

    public static HttpResult doGetNotAutoRedirect(String url) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        if (url.contains(HTTPS)) {
            httpClient = getHttpClient();
        }
        log.info("begin doGet, url is {}", url);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(RequestConfig.custom().setRedirectsEnabled(false).build());
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        int httpCode = httpResponse.getStatusLine().getStatusCode();
        String result = EntityUtils.toString(httpEntity, UTF_8);
        Header[] headers = httpResponse.getAllHeaders();
        Map<String, String> headerMap = new HashMap<String, String>(headers.length);
        if (headers != null && headers.length > 0) {
            for (Header header : headers) {
                if (headerMap.containsKey(header.getName())) {
                    String value = headerMap.get(header.getName()) + ";" + header.getValue();
                    headerMap.put(header.getName(), value);
                } else {
                    headerMap.put(header.getName(), header.getValue());
                }
            }
        }
        log.info("httpcode : [{}], result : [{}], headers : [{}]", httpCode, result, headerMap);
        HttpResult httpResult = new HttpResult();
        httpResult.setHttpCode(httpCode);
        httpResult.setResult(result);
        httpResult.setHeaders(headerMap);
        return httpResult;
    }

    public static HttpResponse doPostForFile(String url, Map<String, String> params, List<NameValuePair> paramArray)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        if (url.contains(HTTPS)) {
            httpClient = getHttpClient();
        }
        log.info("begin doPost, url is {}, params is {}", url, params);
        HttpPost httpPost = new HttpPost(url);
        if (params != null && params.size() > 0) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            HttpEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList, UTF_8);
            httpPost.setEntity(urlEncodedFormEntity);
        }
        if (null != paramArray) {
            httpPost.setEntity(new UrlEncodedFormEntity(paramArray, UTF_8));
        }
        return httpClient.execute(httpPost);
    }
}
