package com.hackathon18;

import java.io.File;

import java.io.FileReader;

import java.io.IOException;

import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.commons.csv.CSVFormat;

import org.apache.commons.csv.CSVRecord;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


public class TestParser {
	
	static CharsetEncoder asciiEncoder =
		      Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

	public static boolean isPureAscii(String v) {
		    return asciiEncoder.canEncode(v);
		  }
	
	public static void main(String[] args) throws IOException {
	
		int count = 0;
		
		String srcFile = "comments_timh.csv";
		
		File f = new File(srcFile);
		
		long ff = FileUtils.sizeOf(f);
		
		System.out.println(ff);
		
		
		Reader in = new FileReader(srcFile);
		
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		
		for (CSVRecord record : records) {
		
		    String rating = StringUtils.normalizeSpace(record.get(3));//rating
		
		    String comment_text = record.get(4);//comment_text
		    
		    String bug_flag = record.get(5);//bug_flag
		    
		    String crash_flag = record.get(6);//crash_flag
		    if(StringUtils.isNotBlank(comment_text) && TestParser.isPureAscii(comment_text) == true) {
			    
		    	String fileName = "LDA-DATA";
			    
			    if(StringUtils.isBlank(rating) || rating.equalsIgnoreCase("NULL")) {
			    	FileUtils.write(new File(fileName+"/test/_test_"+count+"_"+bug_flag+"_"+crash_flag+".txt"), comment_text, "UTF-8", false);
			    }
			    else {
			    	FileUtils.write(new File(fileName+"/training/_training_"+count+"_"+bug_flag+"_"+crash_flag+"_rating_"+rating+".txt"), comment_text, "UTF-8", false);
			    }
			    count++;
		    }
		}
		
		
		//String label = "æ¯Žå›žå‡ºå¼µå…ˆã‚’è¨˜å…¥ã—ãªã„ã¨ã„ã‘ãªã„ã€‚ä¸€åº¦è¨˜å…¥ã—ãŸã‚‚ã®ã¯ã€ãƒ‡ãƒ•ã‚©ãƒ«ãƒˆã§æ®‹ã—ã¦ãŠã„ã¦ã»ã—ã„ã€‚æ¥­å‹™å†…å®¹ã¯ãã®ã‚ˆã†ã«ãªã£ã¦ã„ã¾ã™ãŒã€‚";
		//Charset charset = Charset.forName("UTF-8");
		//label = charset.decode(charset.encode(label)).toString();
		//System.out.println(label);
		//String label = "abc 12";
		
	   // System.out.println(" isPureAscii() : " + TestParser.isPureAscii(label));
	  
	    //System.out.println(" isPureAscii() : " + TestParser.isPureAscii(label));
	
	}

}