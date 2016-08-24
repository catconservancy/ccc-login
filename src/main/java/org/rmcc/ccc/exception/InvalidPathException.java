package org.rmcc.ccc.exception;

import org.rmcc.ccc.controller.PhotoController;

public class InvalidPathException extends Exception {

	private static final long serialVersionUID = 1L;

	private String studyAreaName;
	private String locationId;
	
	public InvalidPathException(String path) {

		String pathSubstr = path.substring(path.indexOf(PhotoController.UNCATALOGED_ROOT) + PhotoController.UNCATALOGED_ROOT.length() + 1);
		String[] pathElements = pathSubstr.split("/");
		if (pathElements.length > 0) {
			this.studyAreaName = pathElements[0];
			this.locationId = pathElements.length > 1 ? pathElements[1] : null;
		}
	}

	public String getStudyAreaName() {
		return studyAreaName;
	}

	public void setStudyAreaName(String studyAreaName) {
		this.studyAreaName = studyAreaName;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

}
