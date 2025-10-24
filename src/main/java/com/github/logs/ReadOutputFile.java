package com.github.logs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadOutputFile {
	
	public List<String> consoleLog() {
//	public static void main(String[] args) {	
		
		String path = "./src/main/resources/static/logs/extracted-artifact/src/test/resources/consoleOutput.txt";
		File file = new File(path);
		
		List<String> str = new ArrayList<>();
		
		try (Scanner sc = new Scanner(file)) {
			
			while(sc.hasNextLine()) {
				str.add(sc.nextLine());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return str;
//		System.out.println(str);
	}
	
	

}
