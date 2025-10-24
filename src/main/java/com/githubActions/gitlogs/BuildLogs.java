package com.githubActions.gitlogs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.stream.Stream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.stereotype.Component;

import com.github.execution.GetLatestRunID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class BuildLogs {

	private static final String GITHUB_API_URL = "https://api.github.com/repos/";
	private static final String REPO_OWNER = "sathishofficialwork";
	private static final String REPO_NAME = "PortfolioProject";
	private static String WORKFLOW_RUN_ID = null;
	private static final String TOKEN = "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";
	

	@SuppressWarnings({ "deprecation" })
	public String buildLog(){
		
		String log = new String();

		long latestRunId = 0;
		String apiUrl = "https://api.github.com/repos/" + REPO_OWNER + "/" + REPO_NAME + "/actions/runs";

//		try {
//			URL url = new URL(apiUrl);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setRequestProperty("Authorization", "Bearer " + TOKEN);
//			conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
//
//			int responseCode = conn.getResponseCode();
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//				StringBuilder response = new StringBuilder();
//				String line;
//
//				while ((line = in.readLine()) != null) {
//					response.append(line);
//				}
//				in.close();
////				System.out.println();
//
//				JSONObject jsonResponse = new JSONObject(response.toString());
//				JSONArray runsArray = jsonResponse.getJSONArray("workflow_runs");
//
//				if (runsArray.length() > 0) {
//					JSONObject latestRun = runsArray.getJSONObject(0); // Get the first run (latest one)
//					latestRunId = latestRun.getLong("id");
//					System.out.println("Latest Workflow Run ID: " + latestRunId);
//				} else {
//					System.out.println("No workflow runs found.");
//				}
//			} else {
//				System.out.println("Failed to fetch data. HTTP Response Code: " + responseCode);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

//		WORKFLOW_RUN_ID = Long.toString(latestRunId);
		
		GetLatestRunID latestRun = new GetLatestRunID();
		
		WORKFLOW_RUN_ID = latestRun.toGetLatestRunID();
		
		System.out.println(WORKFLOW_RUN_ID);

		OkHttpClient client = null;
		try {
			client = getUnsafeOkHttpClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Request request = new Request.Builder()
				.url(GITHUB_API_URL + REPO_OWNER + "/" + REPO_NAME + "/actions/runs/" + WORKFLOW_RUN_ID + "/logs")
				.header("Authorization", "Bearer " + TOKEN).build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("Unexpected code " + response);
			}

			// Save the ZIP file to a temporary location
			Path tempZip = Files.createTempFile("github-actions-logs", ".zip");
			Files.write(tempZip, response.body().bytes());
			
//			Stream stm = null;
			
//			String strStm =null;

			// Extract the ZIP file
			try (ZipFile zipFile = new ZipFile(tempZip.toFile())) {
				Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
				while (entries.hasMoreElements()) {
					ZipArchiveEntry entry = entries.nextElement();
					try (InputStream stream = zipFile.getInputStream(entry)) {
						System.out.println("Log file: " + entry.getName());
						log += new String(stream.readAllBytes());
						
//						System.out.println(strStm);
//						log += ";;"+strStm;
						
					}
				}
			}
//			System.out.println("-->>"+log);

			// Delete the temporary ZIP file
			Files.delete(tempZip);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return log;
	
	}

	private static OkHttpClient getUnsafeOkHttpClient() throws Exception {
//         Create a trust manager that does not validate certificate chains
		final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		} };

		// Install the all-trusting trust manager
		final SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

		// Create an ssl socket factory with our all-trusting manager
		final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
		builder.hostnameVerifier((hostname, session) -> true);

		return builder.build();
	}
}
