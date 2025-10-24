package com.github.execution;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class GetLatestRunID extends PreRequiredData {
	
	public GetLatestRunID() {
		super("runid");
	}

    public String toGetLatestRunID() {
//        String owner = "sathishofficialwork";
//        String repo = "PortfolioProject";
//        String token = "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";

        OkHttpClient client = new OkHttpClient();
        
        long runId = 0;

        String url = String.format("https://api.github.com/repos/%s/%s/actions/runs?per_page=1", repoOwner, repoName);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "token " + token)
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);
                JSONArray workflowRuns = json.getJSONArray("workflow_runs");

                if (workflowRuns.length() > 0) {
                    JSONObject latestRun = workflowRuns.getJSONObject(0);
                    runId = latestRun.getLong("id");
                    String workflowName = latestRun.getString("name");
                    System.out.println("Latest Workflow Run ID: " + runId);
                    System.out.println("Workflow Name: " + workflowName);
                } else {
                    System.out.println("No workflow runs found.");
                }
            } else {
                System.out.println("Error: HTTP " + response.code());
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String runIDStr = Long.toString(runId);
        return runIDStr;
    }
}

