package com.hackathon18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class CsvDocReader {
	
	private Function<String, Optional<Complaint>> mapToItem = (line) -> {
		String[] entryArray = line.split(",");
		Complaint complaint = null;

		String consumer_complaint = entryArray[5];
		if(StringUtils.isNotBlank(consumer_complaint)) {
			System.out.println(consumer_complaint);
			//complaint.setProduct(Optional.ofNullable(entryArray[1]));
			//complaint.setSub_product(Optional.ofNullable(entryArray[2]));
			//complaint.setIssue(Optional.ofNullable(entryArray[3]));
			//complaint.setSub_issue(Optional.ofNullable(entryArray[4]));
			//complaint.setComplaint(Optional.ofNullable(consumer_complaint));
			String id = UUID.randomUUID().toString();
			//complaint.setUuid(id);
			try {
				FileUtils.writeStringToFile(new File("C:/users/Obipc/Desktop/modelData1/"+id+".txt"), consumer_complaint, "UTF-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Optional<Complaint> comp = Optional.ofNullable(complaint);
		return comp;
	};
	
	public List<Optional<Complaint>> processAndSaveIntoFile(String inputFilePath) {
		  	List<Optional<Complaint>> inputList = new ArrayList<Optional<Complaint>>();
		    try{
		    	
		      File inputF = new File(inputFilePath);
		      InputStream inputFS = new FileInputStream(inputF);
		      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
		      // skip the header of the csv
		      inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
		      br.close();
		    } catch (IOException e) {
		      //....
		    }
		    return inputList ;
	}
	
	
	public static void main(String[] args) {
		CsvDocReader docReader = new CsvDocReader();
		docReader.processAndSaveIntoFile("c:/users/obiPc/Desktop/us-consumer-finance-complaint-database/consumer_complaints.csv");
	}
	
}
