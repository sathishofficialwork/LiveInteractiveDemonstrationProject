package com.github.execution;

import com.utility.ReadFiles;

public class PreRequiredData {
	
	public static String repoOwner;
	public static String repoName;
	public static String token;
	public static String latestRunId;
	public static String latestSuccessRunId;
	
	
	public  PreRequiredData() {
		
		ReadFiles file = new ReadFiles();
		GetLatestRunID latRunId = new GetLatestRunID();
		GetLatestSuccessRunID latsusRunId = new GetLatestSuccessRunID();
		
		repoOwner = file.getValues("repoOwner");
		repoName = file.getValues("repoName");
		token = file.getValues("token");
		latestRunId = latRunId.toGetLatestRunID();
		latestSuccessRunId = latsusRunId.getWorkFlowID();
		
		
	}
	
	public PreRequiredData(String str) {
		
		ReadFiles file = new ReadFiles();
		
		repoOwner = file.getValues("repoOwner");
		repoName = file.getValues("repoName");
		token = file.getValues("token");
		
		
	}
	
	
	
//	public static void main(String[] args) {
//		ReadFiles files = new ReadFiles();
//		String value = files.getValues("token");
//		System.out.println(value);
//		
//		
//	}
	

}
