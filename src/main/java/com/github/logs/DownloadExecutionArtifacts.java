package com.github.logs;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.execution.GetLatestSuccessRunID;
import com.github.execution.PreRequiredData;

public class DownloadExecutionArtifacts extends PreRequiredData{

//	private static final String GITHUB_TOKEN = "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"; 
//	private static final String OWNER = "sathishofficialwork";
//	private static final String REPO = "PortfolioProject";
	// Set in Run time
//	private static String WORKFLOW_RUN_ID = null;
	public static int i =0;
	private static final String ARTIFACT_NAME = "test-logs"; // Artifact name to look for

	private static final HttpClient client = HttpClient.newHttpClient();
	
	public DownloadExecutionArtifacts() {
		super();
	}

	public void replaceUpdatedLogs() {
	
//	public static void main(String[] a) {
		
//		GetLatestSuccessRunID runId = new GetLatestSuccessRunID();
//		
//		WORKFLOW_RUN_ID = runId.getWorkFlowID();
		
		
		try {
			// Fetch the artifact ID from the specified workflow run by name
			String artifactId = Long.toString(findArtifactIdByNameInRun(ARTIFACT_NAME, latestSuccessRunId));
			System.out.println(artifactId);
			if (artifactId.equals(-1)) {
				System.out.println(
						"Artifact \"" + ARTIFACT_NAME + "\" not found in workflow run " + latestSuccessRunId + "!");
				return;
			} else {
				System.out.println("Found artifact \"" + ARTIFACT_NAME + "\" with ID: " + artifactId);
			}

			// Download the artifact's ZIP archive
			String outputFileName = "./src/main/resources/static/ArtifactZipFile/"+ARTIFACT_NAME +".zip";
			downloadArtifactZip(artifactId, outputFileName);
			System.out.println("Artifact downloaded successfully as " + outputFileName);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		unzipArtifact();
	}

	
	private static long findArtifactIdByNameInRun(String artifactName, String workflowRunId)
			throws IOException, InterruptedException {
		// API endpoint to list artifacts for a specific workflow run
		String url = String.format("https://api.github.com/repos/%s/%s/actions/runs/%s/artifacts", repoOwner, repoName,
				workflowRunId);
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				.header("Authorization", "token " + token).header("Accept", "application/vnd.github+json")
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println("Artifacts JSON: " + response.body());

		if (response.statusCode() != 200) {
			throw new RuntimeException("Failed to retrieve artifacts. HTTP error code: " + response.statusCode());
		}

		JSONObject jsonResponse = new JSONObject(response.body());
		JSONArray artifacts = jsonResponse.getJSONArray("artifacts");
		for (int i = 0; i < artifacts.length(); i++) {
			JSONObject artifact = artifacts.getJSONObject(i);
			if (artifactName.equals(artifact.getString("name").trim())) {
				return artifact.getLong("id");
			}
		}
		return -1;
	}

	
	private static void downloadArtifactZip(String artifactId, String outputFileName)
			throws IOException, InterruptedException {
		// Step 1: Request the artifact download URL
		String url = String.format("https://api.github.com/repos/%s/%s/actions/artifacts/%s/zip", repoOwner, repoName,
				artifactId);
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				// Add the authorization header and Accept header as required for the API call
				.header("Authorization", "token " + token).header("Accept", "application/vnd.github+json")
				.build();

		HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(outputFileName)));

		// If we get a 302 response, GitHub is telling us to use the provided Location
		// to fetch the contents.
		if (response.statusCode() == 302) {
			// Get the 'Location' header value
			Optional<String> redirectUrl = response.headers().firstValue("Location");
			if (redirectUrl.isPresent()) {
				System.out.println("Following redirect URL: " + redirectUrl.get());

				// Create a new GET request for the redirect URL.
				// Notice we omit the Authorization header on the redirect because the URL is
				// pre-signed and public.
				HttpRequest redirectRequest = HttpRequest.newBuilder().uri(URI.create(redirectUrl.get())).GET().build();

				// Attempt to get the file from the redirect URL.
				HttpResponse<Path> redirectResponse = client.send(redirectRequest,
						HttpResponse.BodyHandlers.ofFile(Paths.get(outputFileName)));
				if (redirectResponse.statusCode() != 200) {
					throw new RuntimeException("Failed to download artifact after redirect. HTTP error code: "
							+ redirectResponse.statusCode());
				}
			} else {
				throw new RuntimeException("Redirect response received without a 'Location' header.");
			}
		} else if (response.statusCode() != 200) {
			throw new RuntimeException("Failed to download artifact. HTTP error code: " + response.statusCode());
		}
	}
	
	
	public static void unzipArtifact() {
		String zipFilePath = "./src/main/resources/static/ArtifactZipFile/"+ARTIFACT_NAME + ".zip";             // Path to downloaded artifact ZIP
        String destDir = "./src/main/resources/static/logs/extracted-artifact";             // Where to extract the files

        
        
        try {
            UnZipLogFile.tounzipArtifact(zipFilePath, destDir);
            System.out.println("Extraction complete.");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
