package com.githubActions.trigger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.github.execution.PreRequiredData;

public class TriggerWorkFlow extends PreRequiredData{
	
	public TriggerWorkFlow() {
		super();
	}

    public void toStartWorkFlow() {
        try {
//            String repo = "sathishofficialwork/PortfolioProject";  // Example: "sathish/MyRepo"
            String workflowFileName = "maven.yml";
//            String token = "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";  // ✅ Your GitHub Personal Access Token (keep secret!)

//            URL url = new URL("https://api.github.com/repos/" + repo + "/actions/workflows/" + workflowFileName + "/dispatches");
            URL url = new URL("https://api.github.com/repos/" + repoOwner+"/"+ repoName + "/actions/workflows/" + workflowFileName + "/dispatches");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Accept", "application/vnd.github+json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"ref\":\"main\"}";  // Replace 'main' with your branch name if needed

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == 204) {
                System.out.println("Workflow triggered successfully!");
            } else {
                System.out.println("Error triggering workflow.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


