package org.rmcc.ccc.service.user;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxFiles;
import com.dropbox.core.v2.DbxFiles.Metadata;
import com.dropbox.core.v2.DbxUsers;
import com.dropbox.core.v2.DbxUsers.GetCurrentAccountException;

@Service
public class DropboxService {
	
	static final String ACCESS_TOKEN = "0zSFsnWoJOUAAAAAAAEafHy4-QOqabrvGxliLU3rkk1XAu0GkpdWfLwzciXM_f6B";
	
    private DbxRequestConfig config;
    private DbxClientV2 client;
    private DbxUsers.FullAccount account;
	
	public DropboxService() throws GetCurrentAccountException, DbxException, IOException {
		// Create Dropbox client
		config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
		client = new DbxClientV2(config, ACCESS_TOKEN);

        // Get current account info
        account = client.users.getCurrentAccount();
        System.out.println(account.name.displayName);

        // Get files and folder metadata from Dropbox root directory
        ArrayList<DbxFiles.Metadata> entries = (ArrayList<Metadata>) client.files.listFolder("").entries;
        for (DbxFiles.Metadata metadata : entries) {
            System.out.println(metadata.pathLower);
        }

//        // Upload "test.txt" to Dropbox
//        InputStream in = new FileInputStream("test.txt");
//        DbxFiles.FileMetadata metadata = client.files.uploadBuilder("/test.txt").run(in);
	}


}
