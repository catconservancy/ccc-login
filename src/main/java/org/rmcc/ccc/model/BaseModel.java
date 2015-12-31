package org.rmcc.ccc.model;

import org.apache.commons.csv.CSVRecord;

public interface BaseModel {

	public String[] getFileHeaderMappings();
	public String getFileName();
	public BaseModel getFromCsvRecord(CSVRecord record);

}
