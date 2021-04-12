package com.homework.common.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.List;

public class HttpClientUtils {
	private static  int CONN_REQ_TIMEOUT = 30000 ;
	private static  int SOCKET_TIMEOUT = 30000 ;
	private static  int CONN_TIMEOUT = 30000 ;
	
	private static String KEYSTORE_PATH;
	private static String KEYSTORE_PASSWORD;

	
	public static String sendGet(String url, String charSet) throws Exception {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONN_REQ_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONN_TIMEOUT).build();// 设置请求和传输超时时间
			get.setConfig(requestConfig);
			
			CloseableHttpResponse resp = client.execute(get);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = convertStreamToString(resp, charSet);// 获取响应报文
				return result;
			} else {
				throw new Exception("响应异常"
						+ resp.getStatusLine().getStatusCode() + ":"
						+ resp.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			get.releaseConnection();
		}
	}

	/**
	 * 发送Post:json请求
	 * 
	 * @param url
	 * @param json
	 *            对象
	 * @return
	 * @throws Exception
	 */
	public static String sendPostJson(String url, String json, String charSet) throws Exception {
		CloseableHttpClient client = null ;
	   	   SSLConnectionSocketFactory socketFactory = null;
			if(url.startsWith("https")) { // https处理方式
				socketFactory = enableSSL();
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().
						register("http", PlainConnectionSocketFactory.INSTANCE).
						register("https", socketFactory).build();	
				PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
				client = HttpClients.custom().setConnectionManager(connectionManager) .build();
			} else {
				client = HttpClientBuilder.create().build();
			}
			HttpPost post = new HttpPost(url);
		ContentType APPLICATION_JSON  = ContentType.APPLICATION_JSON.withCharset("UTF-8");
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONN_REQ_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONN_TIMEOUT).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig);
			if(null != charSet && "".equals(charSet)) {
				APPLICATION_JSON = APPLICATION_JSON.withCharset(charSet);
			}
			StringEntity myEntity = new StringEntity(json, APPLICATION_JSON);
			post.setEntity(myEntity); 
			CloseableHttpResponse resp = client.execute(post);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = convertStreamToString(resp, charSet);// 获取响应报文
				return result;
			} else {
				throw new Exception("响应异常"
						+ resp.getStatusLine().getStatusCode() + ":"
						+ resp.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			post.releaseConnection();
		}
	}
	
	/**
	 * 发送Post:json请求
	 * 
	 * @param url
	 * @param json
	 *            对象
	 * @return
	 * @throws Exception
	 */
	public static String sendPostJsonAndSetHeader(String url, String json, String charSet) throws Exception {
		CloseableHttpClient client = null ;
	   	   SSLConnectionSocketFactory socketFactory = null;
			if(url.startsWith("https")) { // https处理方式
				socketFactory = enableSSL();
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().
						register("http", PlainConnectionSocketFactory.INSTANCE).
						register("https", socketFactory).build();	
				PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
				client = HttpClients.custom().setConnectionManager(connectionManager) .build();
			} else {
				client = HttpClientBuilder.create().build();
			}
			HttpPost post = new HttpPost(url);
		ContentType APPLICATION_JSON  = ContentType.APPLICATION_JSON.withCharset("UTF-8");
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONN_REQ_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONN_TIMEOUT).build();// 设置请求和传输超时时间
			post.setHeader("Content_Type", "application/json;charset=UTF-8");
			post.setHeader("Accept", "application/json;charset=UTF-8");
			post.setConfig(requestConfig);
			if(null != charSet && "".equals(charSet)) {
				APPLICATION_JSON = APPLICATION_JSON.withCharset(charSet);
			}
			StringEntity myEntity = new StringEntity(json, APPLICATION_JSON);
			post.setEntity(myEntity); 
			CloseableHttpResponse resp = client.execute(post);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = convertStreamToString(resp, charSet);// 获取响应报文
				return result;
			} else {
				throw new Exception("响应异常"
						+ resp.getStatusLine().getStatusCode() + ":"
						+ resp.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			post.releaseConnection();
		}
	}
	
	/**
	 * 发送Post:json请求
	 * 
	 * @param url
	 * @param json
	 *            对象
	 * @return
	 * @throws Exception
	 */
	public static String sendPostJson(String url, String json, String charSet, Header[] headers) throws Exception {
		CloseableHttpClient client = null ;
	   	   SSLConnectionSocketFactory socketFactory = null;
			if(url.startsWith("https")) { // https处理方式
				socketFactory = enableSSL();
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().
						register("http", PlainConnectionSocketFactory.INSTANCE).
						register("https", socketFactory).build();	
				PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
				client = HttpClients.custom().setConnectionManager(connectionManager) .build();
			} else {
				client = HttpClientBuilder.create().build();
			}
			HttpPost post = new HttpPost(url);
		ContentType APPLICATION_JSON  = ContentType.APPLICATION_JSON.withCharset("UTF-8");
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONN_REQ_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONN_TIMEOUT).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig);
			if(null != charSet && "".equals(charSet)) {
				APPLICATION_JSON = APPLICATION_JSON.withCharset(charSet);
			}
			StringEntity myEntity = new StringEntity(json, APPLICATION_JSON);
			post.setEntity(myEntity); 
			if(null != headers && headers.length > 0) {
				post.setHeaders(headers);
			}
			CloseableHttpResponse resp = client.execute(post);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = convertStreamToString(resp, charSet);// 获取响应报文
				return result;
			} else {
				throw new Exception("响应异常"
						+ resp.getStatusLine().getStatusCode() + ":"
						+ resp.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			post.releaseConnection();
		}
	}
	
	/**
	 * 发送Post:xml请求
	 * @param url
	 * @param xmlStr
	 * @param charSet
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static String sendPostXML(String url, String xmlStr, String charSet, Header[] headers) throws Exception {
		CloseableHttpClient client = null ;
   	   SSLConnectionSocketFactory socketFactory = null;
		if(url.startsWith("https")) { // https处理方式
			socketFactory = enableSSL();
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().
					register("http", PlainConnectionSocketFactory.INSTANCE).
					register("https", socketFactory).build();	
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			client = HttpClients.custom().setConnectionManager(connectionManager) .build();
		} else {
			client = HttpClientBuilder.create().build();
		}
		HttpPost post = new HttpPost(url);
		ContentType  APPLICATION_XML  = ContentType.APPLICATION_XML.withCharset("UTF-8");

		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONN_REQ_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONN_TIMEOUT).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig); 
			if(null != charSet && "".equals(charSet)) {
				APPLICATION_XML = APPLICATION_XML.withCharset(charSet);
			}
			StringEntity myEntity = new StringEntity(xmlStr, APPLICATION_XML); 
			post.setEntity(myEntity);
			if(null != headers && headers.length > 0) {
				post.setHeaders(headers);
			}
			CloseableHttpResponse resp = client.execute(post);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = convertStreamToString(resp,charSet);// 获取响应报文
				return result;
			} else {
				throw new Exception("响应异常"
						+ resp.getStatusLine().getStatusCode() + ":"
						+ resp.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			post.releaseConnection();
		}
	}
	
	/**
	 * 发送Post:xml请求
	 * @param url
	 * @param textStr
	 * @param charSet
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static String sendPostText(String url, String textStr, String charSet, Header[] headers) throws Exception {
		CloseableHttpClient client = null ;
	   	SSLConnectionSocketFactory socketFactory = null;
		if(url.startsWith("https")) { // https处理方式
			socketFactory = enableSSL();
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().
					register("http", PlainConnectionSocketFactory.INSTANCE).
					register("https", socketFactory).build();	
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			client = HttpClients.custom().setConnectionManager(connectionManager) .build();
		} else {
			client = HttpClientBuilder.create().build();
		}
		HttpPost post = new HttpPost(url);
		ContentType  APPLICATION_TEXT = ContentType.TEXT_PLAIN.withCharset("UTF-8");

		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONN_REQ_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONN_TIMEOUT).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig); 
			if(null != charSet && "".equals(charSet)) {
				APPLICATION_TEXT = APPLICATION_TEXT.withCharset(charSet);
			}
			StringEntity myEntity = new StringEntity(textStr, APPLICATION_TEXT); 
			post.setEntity(myEntity);
			if(null != headers && headers.length > 0) {
				post.setHeaders(headers);
			}
			CloseableHttpResponse resp = client.execute(post);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = convertStreamToString(resp,charSet);// 获取响应报文
				return result;
			} else {
				throw new Exception("响应异常"
						+ resp.getStatusLine().getStatusCode() + ":"
						+ resp.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			post.releaseConnection();
		}
	}	

	/**
	 * 发送Post:表单请求
	 * 
	 * @param url
	 * @param nvps
	 *            表单数据
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPostForm(String url, List<NameValuePair> nvps, String charSet)
			throws Exception {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONN_REQ_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONN_TIMEOUT).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig);
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			CloseableHttpResponse resp = client.execute(post);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = convertStreamToString(resp,charSet);// 获取响应报文
				return result;
			} else {
				throw new Exception("响应异常"
						+ resp.getStatusLine().getStatusCode() + ":"
						+ resp.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			post.releaseConnection();
		}
	}
	
	
	public static byte[] sendRequest(String url, String textStr, String charSet, Header[] headers) throws Exception {
		CloseableHttpClient client = null ;
	   	SSLConnectionSocketFactory socketFactory = null;		
		if(url.startsWith("https")) { // https处理方式
			socketFactory = enableSSL();
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().
					register("http", PlainConnectionSocketFactory.INSTANCE).
					register("https", socketFactory).build();	
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			client = HttpClients.custom().setConnectionManager(connectionManager) .build();
		} else {
			client = HttpClientBuilder.create().build();
		}
		HttpPost post = new HttpPost(url);
		ContentType  APPLICATION_TEXT = ContentType.TEXT_PLAIN.withCharset("UTF-8");

		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONN_REQ_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONN_TIMEOUT).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig); 
			if(null != charSet && "".equals(charSet)) {
				APPLICATION_TEXT = APPLICATION_TEXT.withCharset(charSet);
			}
			StringEntity myEntity = new StringEntity(textStr, APPLICATION_TEXT); 
			post.setEntity(myEntity);
			if(null != headers && headers.length > 0) {
				post.setHeaders(headers);
			}
			CloseableHttpResponse resp = client.execute(post);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream ir =resp.getEntity().getContent();
				int len = 0;
				byte[] b = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((len = ir.read(b,0, b.length)) != -1) {                    
				    baos.write(b, 0, len);
				}
				return baos.toByteArray();
			} else {
				throw new Exception("响应异常"
						+ resp.getStatusLine().getStatusCode() + ":"
						+ resp.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			post.releaseConnection();
		}
	}	
	

	private static String convertStreamToString(CloseableHttpResponse resp, String charSet)
			throws UnsupportedEncodingException, IOException {
		InputStreamReader ir = null;
		StringBuffer sb = new StringBuffer("");
		try {
			HttpEntity entity = resp.getEntity();
			ir = new InputStreamReader(entity.getContent(),charSet);
			BufferedReader br = new BufferedReader(ir);
			String t = null;
			while ((t = br.readLine()) != null) {
				sb.append(t);
			}
		} finally {
			resp.close();
			if (ir != null) {
				ir.close();
			}
		}
		System.out.println("响应报文[{"+sb+"}]");
		return sb.toString();
	}
	
	 /** 
     * 访问https的网站 
     * @param httpclient 
     */  
    private static SSLConnectionSocketFactory enableSSL(){  
        //调用ssl  
         try {  
        	 SSLConnectionSocketFactory socketFactory = null;
                SSLContext sslcontext = SSLContext.getInstance("TLS");  
                sslcontext.init(null, new TrustManager[] { trustAllManager }, null);  
                socketFactory = new SSLConnectionSocketFactory(sslcontext,NoopHostnameVerifier.INSTANCE);    
                return socketFactory;
            } catch (Exception e) {  
                e.printStackTrace();  
                return null;
            }  
    }  
    
    
    /** 
     * 重写验证方法，取消检测ssl 
     */  
    private static TrustManager trustAllManager = new X509TrustManager(){  
  
        public void checkClientTrusted(  
                java.security.cert.X509Certificate[] arg0, String arg1)  
                throws CertificateException {  
            // TODO Auto-generated method stub  
              
        }  
  
        public void checkServerTrusted(  
                java.security.cert.X509Certificate[] arg0, String arg1)  
                throws CertificateException {  
            // TODO Auto-generated method stub  
              
        }  
  
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
            // TODO Auto-generated method stub  
            return null;  
        }  
          
    }; 	
    
//    private static SSLConnectionSocketFactory socketFactory;
    
    private static SSLConnectionSocketFactory enableSSLWithCerticate(){  
        //调用ssl  
         try { 
        	 SSLConnectionSocketFactory socketFactory = null;
        	 SSLContext sslContext = null; 
        	 KeyStore kstore = KeyStore.getInstance("jks"); 
        	 kstore.load(new FileInputStream(KEYSTORE_PATH), 
        	 KEYSTORE_PASSWORD.toCharArray()); 
        	 KeyManagerFactory keyFactory = KeyManagerFactory 
        	 .getInstance("sunx509"); 
        	 keyFactory.init(kstore, KEYSTORE_PASSWORD.toCharArray()); 
        	 KeyStore tstore = KeyStore.getInstance("jks"); 
        	 tstore.load(new FileInputStream(KEYSTORE_PATH), 
        			 KEYSTORE_PASSWORD.toCharArray()); 
        	 TrustManager[] tm; 
        	 TrustManagerFactory tmf = TrustManagerFactory 
        	 .getInstance("sunx509"); 
        	 tmf.init(tstore); 
        	 tm = tmf.getTrustManagers(); 
        	 sslContext = SSLContext.getInstance("SSL"); 
        	 sslContext.init(keyFactory.getKeyManagers(), tm, null); 
        	 socketFactory = new SSLConnectionSocketFactory(sslContext,NoopHostnameVerifier.INSTANCE); 
        	 return socketFactory;
            } catch (Exception e) {  
                e.printStackTrace();
                return null;
            }  
    } 
   
    
	/**
	 * 发送Post:xml请求
	 * @param url
	 * @param xmlStr
	 * @param charSet
	 * @param headers
	 * @param needCertificate 是否需要证书
	 * @return
	 * @throws Exception
	 */
	public static String sendPostXML(String url, String xmlStr, String charSet, Header[] headers, boolean needCertificate) throws Exception {
		CloseableHttpClient client = null ;
   	    SSLConnectionSocketFactory socketFactory = null;
		if(url.startsWith("https")) { // https处理方式
           if(!needCertificate) {
        	   socketFactory = enableSSL();
           } else {
        	   socketFactory = enableSSLWithCerticate();
           }
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().
					register("http", PlainConnectionSocketFactory.INSTANCE).
					register("https", socketFactory).build();	
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			client = HttpClients.custom().setConnectionManager(connectionManager) .build();
		} else {
			client = HttpClientBuilder.create().build();
		}
		HttpPost post = new HttpPost(url);
		ContentType  APPLICATION_XML  = ContentType.APPLICATION_XML.withCharset("UTF-8");

		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONN_REQ_TIMEOUT)
					.setSocketTimeout(SOCKET_TIMEOUT)
					.setConnectTimeout(CONN_TIMEOUT).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig); 
			if(null != charSet && "".equals(charSet)) {
				APPLICATION_XML = APPLICATION_XML.withCharset(charSet);
			}
			StringEntity myEntity = new StringEntity(xmlStr, APPLICATION_XML); 
			post.setEntity(myEntity);
			if(null != headers && headers.length > 0) {
				post.setHeaders(headers);
			}
			CloseableHttpResponse resp = client.execute(post);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = convertStreamToString(resp,charSet);// 获取响应报文
				return result;
			} else {
				throw new Exception("响应异常"
						+ resp.getStatusLine().getStatusCode() + ":"
						+ resp.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			post.releaseConnection();
		}
	}
	public static String getWebInfoPath(String subPath)
	{ 
		if(null != subPath && "".equals(subPath)) {
			return "";
		}
		String path=Thread.currentThread().getContextClassLoader().getResource("").toString();
		if("\\".equals(File.separator)) { // windows系统
			path=path.replace('/', '\\'); // 将/换成\
			path=path.replace("file:", ""); //去掉file:
			path=path.replace("classes\\", ""); //去掉class\
			path=path.substring(1); // 
			path=path+subPath.replace("/", "\\");
		} else { // linux系统
			path=path.replace("file:", ""); //去掉file:
			path=path.replace("classes/", ""); //去掉class\  
			path=path+subPath.replace("\\", "/");
		}
		return path;
	}     

}
