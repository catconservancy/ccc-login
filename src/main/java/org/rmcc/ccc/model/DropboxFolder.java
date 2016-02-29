package org.rmcc.ccc.model;

import java.io.Serializable;
import java.util.List;

public class DropboxFolder implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<DropboxFile> files;
	private List<DropboxFolder> folders;
	
	public List<DropboxFile> getFiles() {
		return files;
	}
	public void setFiles(List<DropboxFile> files) {
		this.files = files;
	}
	public List<DropboxFolder> getFolders() {
		return folders;
	}
	public void setFolders(List<DropboxFolder> folders) {
		this.folders = folders;
	}
	

}
