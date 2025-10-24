package com.portfolioproject.project;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.logs.ReadOutputFile;
import com.githubActions.gitlogs.BuildLogs;
import com.githubActions.gitlogs.BuildUpdate;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String index() {
		return "index.html";
	}
	
	@RequestMapping("/result")
	public String result(Model model){
		
		BuildUpdate build = new BuildUpdate();
		
		List<String> buildList = build.getBuildLog();
		
//		BuildLogs log = new BuildLogs();
//		String buildlog="Log Empty";
//		
//		buildlog = log.buildLog();
////		try {
////			buildlog = log.mainMethod();
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		String logDate=(String) buildlog.subSequence(1, 11);
//		System.out.println(logDate);
//		String st[] = buildlog.split(logDate);
//		Set<String> build = new HashSet<String>();
//		for(String s : st) {
//			s=logDate+s;
//			build.add(s);
//		}
		
		
		Set<String> buildSet = new HashSet<String>(); 
		
		buildSet.addAll(buildList);
		
		model.addAttribute("buildLog", buildList);
		
		ReadOutputFile console = new ReadOutputFile();
		
		List<String> consoleLog = console.consoleLog();
		System.out.println(consoleLog);
		model.addAttribute("console",consoleLog);
		
		return "result.html";
	}
	

	    @RequestMapping("/downloadResults")
	    public ResponseEntity<Resource> downloadResultFile() throws IOException {
	    	
//	    	Output result = new Output();
//	    	
//	    	String updatedLogStatus = result.updateOutputLogs();
//	    	
//	    	System.out.println(updatedLogStatus);
	        // Path to the file you want to serve
	    	
	        Path path = Paths.get("./src/main/resources/static/logs/extracted-artifact/src/test/resources/Output.xlsx");
	        Resource resource = new UrlResource(path.toUri());

	        if (!resource.exists()) {
	            throw new FileNotFoundException("File not found");
	        }

	        // Set the content type and attachment header
	        return ResponseEntity.ok()
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .header(HttpHeaders.CONTENT_DISPOSITION,
	                        "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
	}
	    
	    @RequestMapping("/downloadLog4j")
	    public ResponseEntity<Resource> downloadLogFile() throws IOException {
	    	
//	    	Log4J log = new Log4J();
//	    	
//	    	String updatedLogStatus = log.updateOutputLogs();
//	    	
//	    	System.out.println(updatedLogStatus);
	        // Path to the file you want to serve
	    	
	        Path path = Paths.get("./src/main/resources/static/logs/extracted-artifact/logs/automation.log");
	        Resource resource = new UrlResource(path.toUri());

	        if (!resource.exists()) {
	            throw new FileNotFoundException("File not found");
	        }

	        // Set the content type and attachment header
	        return ResponseEntity.ok()
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .header(HttpHeaders.CONTENT_DISPOSITION,
	                        "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
	}
	    
	    @RequestMapping("/downloadReport")
	    public ResponseEntity<Resource> downloadReportFile() throws IOException {
	    	
//	    	ExtendedReport report = new ExtendedReport();
//	    	
//	    	String updatedLogStatus = report.updateOutputLogs();
//	    	
//	    	System.out.println(updatedLogStatus);
	        // Path to the file you want to serve
	    	
	        Path path = Paths.get("./src/main/resources/static/logs/extracted-artifact/reports/Test-Report.html");
	        Resource resource = new UrlResource(path.toUri());

	        if (!resource.exists()) {
	            throw new FileNotFoundException("File not found");
	        }

	        // Set the content type and attachment header
	        return ResponseEntity.ok()
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .header(HttpHeaders.CONTENT_DISPOSITION,
	                        "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
	    	}
	    
	    

}
