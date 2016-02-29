package org.rmcc.ccc.model;

import java.io.Serializable;

import com.dropbox.core.v2.files.Metadata;

public class CccMetadata extends Metadata implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isDir;

	public CccMetadata(Metadata m, boolean isDir) {
		super(m.getName(), m.getPathLower(), m.getParentSharedFolderId());
		this.isDir = isDir;
	}

	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

}
