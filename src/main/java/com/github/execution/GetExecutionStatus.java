package com.github.execution;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetExecutionStatus extends PreRequiredData {

//	private static final String REPO_OWNER = "sathishofficialwork";
//	private static final String REPO_NAME = "PortfolioProject";
//	private static String WORKFLOW_RUN_ID = null;
//	private static final String TOKEN = "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";

	public GetExecutionStatus() {
		super();
	}
	
	public String togetExecutionStatus() {
		
//		GetLatestRunID runId = new GetLatestRunID();
//		
//		WORKFLOW_RUN_ID = runId.toGetLatestRunID();
				
		String status = null;

		try {
            

            // Construct the URI using separate components
            URI uri = new URI(
            		"https://api.github.com/repos/" + repoOwner + "/" + repoName + "/actions/runs/"+ latestRunId
            );
            System.out.println("Constructed URI: " + uri);

            // Use the constructed URI in your HTTP request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/vnd.github+json")
                    .header("Authorization", "Bearer "+token)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            String result = response.body();
            
            if(result.contains("\"status\":\"completed\"")&&result.contains("\"conclusion\":\"success\"")) {
            	System.out.println("Success");
            	status = "Success";
            }
            else if(result.contains("\"status\":\"completed\"")&&result.contains("\"conclusion\":\"failure\"")) {
            	System.out.println("Failure");
            	status = "Failure";
            }
            else {
            	System.out.println("null");
            	status = "null";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return status;
    }
}
