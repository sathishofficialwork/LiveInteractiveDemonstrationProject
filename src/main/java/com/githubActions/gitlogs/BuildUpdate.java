package com.githubActions.gitlogs;

import okhttp3.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.github.execution.PreRequiredData;

public class BuildUpdate extends PreRequiredData{
	
	public BuildUpdate() {
		super();
	}

    public List<String> getBuildLog() {
//        String repoOwner = "sathishofficialwork";
//        String repoName = "PortfolioProject";
//        String latestRunId = "15954633809";  // Replace with your actual runId
//        String token = "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";

//    	PreRequiredData data = new PreRequiredData();
    	
        List<String> buildLogList = new ArrayList<String>();
        
        OkHttpClient client = new OkHttpClient();

        String url = String.format("https://api.github.com/repos/%s/%s/actions/runs/%s/logs", repoOwner, repoName, latestRunId);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "token " + token)
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // Save ZIP to a temp file
                Path tempZip = Files.createTempFile("github-action-logs-", ".zip");
                Files.write(tempZip, response.body().bytes());

                // Unzip and read each file
                try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(tempZip.toFile()))) {
                    ZipEntry entry;
                    while ((entry = zipIn.getNextEntry()) != null) {
                        System.out.println("\n==== Log File: " + entry.getName() + " ====");

                        BufferedReader reader = new BufferedReader(new InputStreamReader(zipIn));
                        String line;
                        while ((line = reader.readLine()) != null) {
//                            System.out.println(line);
                            buildLogList.add(line);
                        }

                        zipIn.closeEntry();
                    }
                }
                
				Files.deleteIfExists(tempZip);
				
                System.out.println(buildLogList.size());

            } else {
                System.out.println("Failed to fetch logs. HTTP Code: " + response.code());
            }
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        return buildLogList;
    }
    
}

