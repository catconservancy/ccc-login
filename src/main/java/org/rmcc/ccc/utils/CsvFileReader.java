package org.rmcc.ccc.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.rmcc.ccc.model.BaseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class CsvFileReader {

	@Autowired
	private ResourceLoader resourceLoader;
	
	public List<BaseModel> readCsvFile(BaseModel model) {
		
		Resource resource = resourceLoader.getResource("classpath:/data_load/" + model.getFileName());

		FileReader fileReader = null;
		
		CSVParser csvFileParser = null;
		
		//Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(model.getFileHeaderMappings());
     
        try {
        	
        	//Create a new list of model objects to be filled by CSV file data 
        	List<BaseModel> models = new ArrayList<>();
            
            //initialize FileReader object
            fileReader = new FileReader(resource.getFile());
            
            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            
            //Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords(); 
            
            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
            	CSVRecord record = (CSVRecord) csvRecords.get(i);
            	//Create a new student object and fill his data
            	BaseModel loadModel = model.getFromCsvRecord(record);
                models.add(loadModel);	
			}

            return models;
        } 
        catch (Exception e) {
        	System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
            	System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
		return null;

	}

}
