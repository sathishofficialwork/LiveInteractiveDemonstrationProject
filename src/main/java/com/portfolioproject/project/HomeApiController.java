package com.portfolioproject.project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.execution.GetExecutionStatus;
import com.github.logs.DownloadExecutionArtifacts;
import com.githubActions.trigger.TriggerWorkFlow;

@RestController
public class HomeApiController {

	 @GetMapping("/api/getMessage")
	    public String getOutput() {
		 
		 GetExecutionStatus statusObject = new GetExecutionStatus();
		 
		 String status = statusObject.togetExecutionStatus();
	      // This can be replaced by any logic or service call.
	      return status;
	    }
	 
	 @GetMapping("/api/startWorkflow")
	    public void startWorkFlow() {
		 
		 TriggerWorkFlow trigger = new TriggerWorkFlow();
		 trigger.toStartWorkFlow();
	      // This can be replaced by any logic or service call.
//	      return null;
	    }
	 
	 @GetMapping("/api/updateLatestLog")
	    public void updateLatestLog() {
		 
		 DownloadExecutionArtifacts artifacts = new DownloadExecutionArtifacts();
		 artifacts.replaceUpdatedLogs();
	      // This can be replaced by any logic or service call.
//	      return null;
	    }
}
