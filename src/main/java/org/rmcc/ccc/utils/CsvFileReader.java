package org.rmcc.ccc.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.rmcc.ccc.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvFileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvFileReader.class);

    private static final String PROFILE_DEV = "development";
    private static final String PROFILE_HEROKU = "heroku";
    @Autowired
    Environment env;
    @Autowired
	private ResourceLoader resourceLoader;
	
	public List<BaseModel> readCsvFile(BaseModel model) {

        String fileSuffix = env.getActiveProfiles() != null && env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : PROFILE_DEV;
        if (fileSuffix.equalsIgnoreCase(PROFILE_HEROKU)) {
            fileSuffix = PROFILE_DEV;
        }

        String importFileName = model.getDataImportFileName() + "_" + fileSuffix + ".csv";

        Resource resource = resourceLoader.getResource("classpath:/data_load/" + importFileName);

		InputStreamReader fileReader = null;
		
		CSVParser csvFileParser = null;
		
		//Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(model.getFileHeaderMappings());
     
        try {
        	
        	//Create a new list of model objects to be filled by CSV file data 
        	List<BaseModel> models = new ArrayList<BaseModel>();
            
            //initialize FileReader object
            fileReader = new InputStreamReader(resource.getInputStream());
            
            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            
            //Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords(); 
            
            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                //Create a new student object and fill his data
                BaseModel loadModel = model.getFromCsvRecord(record);
                models.add(loadModel);	
			}

            return models;
        } 
        catch (Exception e) {
            LOGGER.error("Error occurred reading CSV file: " + importFileName, e);
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
                LOGGER.error("Error while closing fileReader/csvFileParser !!!", e);
            }
        }
		return null;

	}

}
