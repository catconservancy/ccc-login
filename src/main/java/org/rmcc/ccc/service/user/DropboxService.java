package org.rmcc.ccc.service.user;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxRequestUtil;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DeletedMetadata;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.ListFolderContinueErrorException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.LookupError;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;


@Service
public class DropboxService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DropboxService.class);
	
	static final String ACCESS_TOKEN = "0zSFsnWoJOUAAAAAAAEafHy4-QOqabrvGxliLU3rkk1XAu0GkpdWfLwzciXM_f6B";
	
    private DbxRequestConfig config;
    private DbxClientV2 client;
    private FullAccount account;
	
	public DropboxService() throws DbxException, IOException {
		// Create Dropbox client
		config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
		client = new DbxClientV2(config, ACCESS_TOKEN);

        // Get current account info
        account = client.users.getCurrentAccount();

//        // Upload "test.txt" to Dropbox
//        InputStream in = new FileInputStream("test.txt");
//        DbxFiles.FileMetadata metadata = client.files.uploadBuilder("/test.txt").run(in);
	}
	
	public List<Metadata> getFolderContentsByPath(String path) throws DbxException {
		
		List<Metadata> encodedPaths = new ArrayList<Metadata>();
		
		 // Get the folder listing from Dropbox.
        TreeMap<String,Metadata> children = new TreeMap<String,Metadata>();

        ListFolderResult result;
        try {
            try {
                result = client.files.listFolder(path);
            } catch (ListFolderErrorException ex) {
                if (ex.errorValue.isPath()) {
                	LOGGER.error("Dropbox error", ex);
                    if (checkPathError(path, ex.errorValue.getPathValue())) return null;
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
                        if (checkPathError(path, ex.errorValue.getPathValue())) break;
                    }
                    throw ex;
                }
            }
        }
        catch (DbxException ex) {
        	LOGGER.error("Dropbox error", ex);
//            common.handleDbxException(response, user, ex, "listFolder(" + jq(path) + ")");
            throw ex;
        }
        
        for (Metadata child : children.values()) {
        	Map<String,String> fileMap = new HashMap<String,String>();
        	fileMap.put(DbxRequestUtil.encodeUrlParam(child.getPathLower()), child.getName());
        	encodedPaths.add(child);
        }
        
		return encodedPaths;
	}
	
	public InputStream getInputStreamByPath(String path) throws DownloadErrorException, DbxException, IOException {
		return client.files.download(path).getInputStream();
	}

    private boolean checkPathError(String path, LookupError le) {
        switch (le.tag()) {
            case NOT_FOUND:
            case NOT_FOLDER:
            	LOGGER.error("Path doesn't exist on Dropbox:");
                return true;
        }
        return false;
    }


}
