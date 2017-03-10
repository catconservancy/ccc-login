package org.rmcc.ccc.service.user;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeletedMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderContinueErrorException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.LookupError;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.RelocationErrorException;
import com.dropbox.core.v2.users.FullAccount;
import org.rmcc.ccc.model.CccMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


@Service
public class DropboxService {
	
//	@Value("${ccc.dropbox.accessToken}")
//    private String accessToken;
static final String ACCESS_TOKEN = "8pUCurIPXlUAAAAAAAAIGeOLExI8YLErAizpTMiV5EhbWhDw5jA83Yw3RKkjCz0D"; //mine
    //	static final String ACCESS_TOKEN = "0zSFsnWoJOUAAAAAAAEafHy4-QOqabrvGxliLU3rkk1XAu0GkpdWfLwzciXM_f6B"; //catconservancy
    private static final Logger LOGGER = LoggerFactory.getLogger(DropboxService.class);
    private DbxRequestConfig config;
    private DbxClientV2 client;
    private FullAccount account;
	
	public DropboxService() throws DbxException, IOException {
		// Create Dropbox client
		config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
		client = new DbxClientV2(config, ACCESS_TOKEN);

//        // Get current account info
//        try {
//            account = client.users.getCurrentAccount();
//        } catch (DbxException e) {
//            LOGGER.error("Can't connect to dropbox.");
//        }

	}

	public List<Metadata> getFolderContentsByPath(String path) throws DbxException, IOException {

        List<Metadata> encodedPaths = new ArrayList<>();

        // Get the folder listing from Dropbox.
        TreeMap<String, Metadata> children = new TreeMap<>();

        ListFolderResult result;
        try {
            try {
                result = client.files.listFolder(path);
            } catch (ListFolderErrorException ex) {
                if (ex.errorValue.isPath()) {
                	LOGGER.error("Dropbox error", ex);
                    if (checkPathError(ex.errorValue.getPathValue())) return null;
                }
                throw ex;
            }

            while (true) {
                for (Metadata md : result.getEntries()) {
                    if (md instanceof DeletedMetadata) {
                        children.remove(md.getPathLower());
                    } else {
                        children.put(md.getPathLower(), md);
                    }
                }

                if (!result.getHasMore()) break;

                try {
                    result = client.files.listFolderContinue(result.getCursor());
                }
                catch (ListFolderContinueErrorException ex) {
                    if (ex.errorValue.isPath()) {
                    	LOGGER.error("Dropbox error", ex);
                        if (checkPathError(ex.errorValue.getPathValue())) break;
                    }
                    throw ex;
                }
            }
        }
        catch (DbxException ex) {
        	LOGGER.error("Dropbox error", ex);
            throw ex;
        }
        
        for (Metadata child : children.values()) {
        	boolean isDir = (child instanceof FolderMetadata);
    		CccMetadata cccMetadata = new CccMetadata(child, isDir);
			encodedPaths.add(cccMetadata);
        }
        
		return encodedPaths;
	}

    public InputStream getInputStreamByPath(String path) throws DbxException, IOException {
        try {
            return client.files.download(path).getInputStream();
        } catch (Exception e) {
            LOGGER.warn("Exception occurred getting photo input stream by path: " + path);
            return null;
        }
    }

    public InputStream getThumbnailInputStreamByPath(String path) throws DbxException, IOException {
        try {
            return client.files.getThumbnail(path).getInputStream();
        } catch (Exception e) {
            LOGGER.warn("Exception occurred getting thumbnail input stream by path: " + path);
            return null;
        }
    }

    private boolean checkPathError(LookupError le) {
        switch (le.tag()) {
            case NOT_FOUND:
            case NOT_FOLDER:
            	LOGGER.error("Path doesn't exist on Dropbox:");
                return true;
        }
        return false;
    }

	public void deleteFile(String path) throws DbxException {
		client.files.delete(path);
	}

	public Metadata moveFile(String fromPath, String toPath) throws DbxException {
        try {
            return client.files.move(fromPath, toPath);
        } catch (RelocationErrorException e) {
            LOGGER.error("failed to move file from path: " + fromPath + ", to path: " + toPath, e);
            return null;
        }
    }
}
