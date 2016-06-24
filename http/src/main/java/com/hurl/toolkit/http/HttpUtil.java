package com.hurl.toolkit.http;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	public static String post(String host, String path, Map<String, String> params, String body) {
		URI uri = buildURI(host, path, params);
		HttpPost put = new HttpPost(uri);
		if(StringUtils.isNotEmpty(body)) {
			StringEntity entity = new StringEntity(body, ContentType.create("text/plain", "utf-8"));
			put.setEntity(entity);
		}
		return request(put);
	}
	public static String put(String host, String path, Map<String, String> params, String body) {
		URI uri = buildURI(host, path, params);
		HttpPut put = new HttpPut(uri);
		if(StringUtils.isNotEmpty(body)) {
			StringEntity entity = new StringEntity(body, ContentType.create("text/plain", "utf-8"));
			put.setEntity(entity);
		}
		return request(put);
	}
	public static String get(String host, String path, Map<String, String> params) {
		URI uri = buildURI(host, path, params);
		HttpGet get = new HttpGet(uri);
		return request(get);
	}
	
	private static String request(HttpUriRequest request) {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			return client.execute(request, new StringResponseHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static URI buildURI(String host, String path, Map<String, String> params) {
		URIBuilder builder = new URIBuilder().setScheme("http").setHost(host).setPath(path);
		if(params != null && params.size() > 0){
			for(String key : params.keySet()){
				builder.addParameter(key, params.get(key));
			}
		}
		try{
			return builder.build();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	private static class StringResponseHandler implements ResponseHandler<String>{
		@Override
		public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			String body = "";
			HttpEntity entity = response.getEntity();
			if(entity != null){
				body = EntityUtils.toString(entity);
			}
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException(response.getStatusLine() + body);
			} else {
				return body;
			}
		}
		
	}
}
