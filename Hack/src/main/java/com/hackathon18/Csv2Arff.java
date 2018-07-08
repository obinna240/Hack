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

public class Csv2Arff {
	static CharsetEncoder asciiEncoder =
		      Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

	public static boolean isPureAscii(String v) {
		    return asciiEncoder.canEncode(v);
		  }
	
	public static void main(String[] args) throws IOException {
	
		int count = 0;
		int countOfBugs = 0;
		int countOfCrashes = 0;
		int countOfBoth = 0;
		int countOfOther = 0;
		
		int countOfPositive = 0;
		int countOfNegative = 0;
		int countOfAverage = 0;
		int countOfNull = 0;
		
		String srcFile = "comments_timh.csv";
		
		File f = new File(srcFile);
		
		long ff = FileUtils.sizeOf(f);
		
		System.out.println(ff);
		
		File bugCrashTraining = new File("bug_crash_training.arff");
		File sentimentTraining = new File("sentiment_training.arff");
		File sentimentNull = new File("null_sentiment.arff");
		File sentimentTest = new File("sentimentTest.txt");
		
		Reader in = new FileReader(srcFile);
		
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		
		for (CSVRecord record : records) {
			String date = StringUtils.normalizeSpace(record.get(0));//date
			String id = StringUtils.normalizeSpace(record.get(1));//company_id
			String company_name = StringUtils.normalizeSpace(record.get(2));//company_name
		
		    String rating = StringUtils.normalizeSpace(record.get(3));//rating
		
		    String comment_text = StringUtils.normalizeSpace(record.get(4));//comment_text
		    
		    String bug_flag = StringUtils.normalizeSpace(record.get(5));//bug_flag
		    
		    String crash_flag = StringUtils.normalizeSpace(record.get(6));//crash_flag
		    if(StringUtils.isNotBlank(comment_text) && TestParser.isPureAscii(comment_text) == true) {
			    
		    	comment_text = StringUtils.removeAll(comment_text, "[\'\"`]");
		    	
		    	if(crash_flag.equals("0") && bug_flag.equals("0")) {
		    		String data = "\'"+comment_text+"\',"+"Other";
		    		FileUtils.writeStringToFile(bugCrashTraining, data, "UTF-8", true);
		    		countOfOther++;
		    	}
		    	else if(crash_flag.equals("1") && bug_flag.equals("1")) {
		    		String data = "\'"+comment_text+"\',"+"Both";
		    		FileUtils.writeStringToFile(bugCrashTraining, data, "UTF-8", true);
		    		countOfBoth++;;

		    	}
		    	else if(crash_flag.equals("0") && bug_flag.equals("1"))  {
		    		String data = "\'"+comment_text+"\',"+"Bug";
		    		FileUtils.writeStringToFile(bugCrashTraining, data, "UTF-8", true);
		    		countOfBugs++;
		    	}
		    	else if(crash_flag.equals("1") && bug_flag.equals("0"))  {
		    		String data = "\'"+comment_text+"\',"+"Crash";
		    		FileUtils.writeStringToFile(bugCrashTraining, data, "UTF-8", true);
		    		countOfCrashes++;
		    	}
		    	
		    	if(rating.equals("5") || rating.equals("4")) {
		    		String data = "\'"+comment_text+"\',"+"Positive";
		    		FileUtils.writeStringToFile(sentimentTraining, data, "UTF-8", true);
		    		countOfPositive++;
		    	}
		    	else if(rating.equals("1") || rating.equals("2")) {
		    		String data = "\'"+comment_text+"\',"+"Negative";
		    		FileUtils.writeStringToFile(sentimentTraining, data, "UTF-8", true);
		    		countOfNegative++;
		    	}
		    	
		    	else if(rating .equals( "3") ) {
		    		String data = "\'"+comment_text+"\',"+"Average";
		    		FileUtils.writeStringToFile(sentimentTraining, data, "UTF-8", true);
		    		countOfAverage++;
		    	}
		    	else if(rating.equals("NULL") ) {
		    		String data = "\'"+comment_text+"\',"+"Average";
		    		String data2 = count+1 +","+date+","+id+","+company_name+","+comment_text+","+bug_flag+","+crash_flag+"?";
		    		FileUtils.writeStringToFile(sentimentTest, data, "UTF-8", true);
		    		FileUtils.writeStringToFile(sentimentNull, data2, "UTF-8", true);
		    		countOfNull++;
		    	}
		    	
		    	//String fileName = "LDA-DATA";
		    	
			    
			   // if(StringUtils.isBlank(rating) || rating.equalsIgnoreCase("NULL")) {
			  //  	FileUtils.write(new File(fileName+"/test/_test_"+count+"_"+bug_flag+"_"+crash_flag+".txt"), comment_text, "UTF-8", false);
			  //  }
			  //  else {
			  //  	FileUtils.write(new File(fileName+"/training/_training_"+count+"_"+bug_flag+"_"+crash_flag+"_rating_"+rating+".txt"), comment_text, "UTF-8", false);
			  //  }
			    count++;
		    }
		}
		
		System.out.println("Total Count === "+count);
		System.out.println("Total Count of Null data === "+countOfNull);
		System.out.println("Total Count of Average === "+countOfAverage);
		System.out.println("Total Count of Negative === "+countOfNegative);
		System.out.println("Total Count of Positive === "+countOfPositive);
		System.out.println("Total Count of Bugs === "+countOfBugs);
		System.out.println("Total Count of Crashes === "+countOfCrashes);
		System.out.println("Total Count of Both === "+countOfBoth);
		System.out.println("Total Count of Other === "+countOfOther);
		
		//String label = "æ¯Žå›žå‡ºå¼µå…ˆã‚’è¨˜å…¥ã—ãªã„ã¨ã„ã‘ãªã„ã€‚ä¸€åº¦è¨˜å…¥ã—ãŸã‚‚ã®ã¯ã€ãƒ‡ãƒ•ã‚©ãƒ«ãƒˆã§æ®‹ã—ã¦ãŠã„ã¦ã»ã—ã„ã€‚æ¥­å‹™å†…å®¹ã¯ãã®ã‚ˆã†ã«ãªã£ã¦ã„ã¾ã™ãŒã€‚";
		//Charset charset = Charset.forName("UTF-8");
		//label = charset.decode(charset.encode(label)).toString();
		//System.out.println(label);
		//String label = "abc 12";
		
	   // System.out.println(" isPureAscii() : " + TestParser.isPureAscii(label));
	  
	    //System.out.println(" isPureAscii() : " + TestParser.isPureAscii(label));
	
	}

}


	
	