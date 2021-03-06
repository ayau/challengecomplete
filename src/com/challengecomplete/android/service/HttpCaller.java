package com.challengecomplete.android.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.util.Log;

import com.challengecomplete.android.utils.ChallengeComplete;

public class HttpCaller {
	private static final String TAG ="HttpCaller";
	
	public static final String HOST = "http://10.1.10.20:3000/api";
	
	public static String getRequest(Context context, String path) {
		HttpClient httpclient = new DefaultHttpClient();
		
		String url = HOST + path;
				
		String token = ChallengeComplete.getToken(context);
		long since = ChallengeComplete.getLastSynced(context); // only for sync

		// TODO
		// Switch to namevaluepair later
		// Careful we have ?ftoken=xxx in path
		if (token != null){
			url += "?token=" + token;

			// TODO
			// Have APIService pass in parameters
			url += "&since=" + since;		
				
		}
			
		// Prepare a request object
		HttpGet httpget = new HttpGet(url);

		// Execute the request
		HttpResponse response;
		
		// Set timeout
		final HttpParams httpParams = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		
		try {
			response = httpclient.execute(httpget);
			
			// Examine the response status
			String result = new BasicResponseHandler().handleResponse(response);
			
			return result;

		} catch (HttpResponseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private static String convertStreamToString(InputStream is) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
