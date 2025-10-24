package com.github.execution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetLatestSuccessRunID extends PreRequiredData {
	
	public GetLatestSuccessRunID() {
		super("runId");
	}

//	private static final String REPO_OWNER = "sathishofficialwork";
//	private static final String REPO_NAME = "PortfolioProject";
//	private static String workflowRunId = null;
//	private static final String TOKEN = "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";
	
	@SuppressWarnings({ "deprecation" })
	public String getWorkFlowID() {

		String workflowRunId = null;
		long latestRunId = 0;
//		String apiUrl = "https://api.github.com/repos/" + REPO_OWNER + "/" + REPO_NAME + "/actions/runs";
		
		String apiUrl = String.format(
                "https://api.github.com/repos/%s/%s/actions/runs?status=Success",
                repoOwner, repoName);
		try {
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + token);
			conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;

				while ((line = in.readLine()) != null) {
					response.append(line);
				}
				in.close();
//				System.out.println();

				JSONObject jsonResponse = new JSONObject(response.toString());
				JSONArray runsArray = jsonResponse.getJSONArray("workflow_runs");

				if (runsArray.length() > 0) {
					JSONObject latestRun = runsArray.getJSONObject(0); // Get the first run (latest one)
					latestRunId = latestRun.getLong("id");
					System.out.println("Latest Workflow Run ID: " + latestRunId);
				} else {
					System.out.println("No workflow runs found.");
				}
			} else {
				System.out.println("Failed to fetch data. HTTP Response Code: " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		workflowRunId = Long.toString(latestRunId);

		return workflowRunId;
	}
	
}
