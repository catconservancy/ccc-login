package org.rmcc.ccc.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.rmcc.ccc.model.DropboxFolder;
import org.rmcc.ccc.model.User;
import org.rmcc.ccc.service.user.DropboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.Metadata;

@RestController
@RequestMapping("/api/dropbox")
public class DropboxController extends BaseController {
	
	private static final String UNCATALOGED_ROOT = "/ccc camera study project/uncataloged camera study area photos";
	private static final String ARCHIVED_ROOT = "/ccc camera study project/archived photos";
	
	private DropboxService dropboxService;

	@Autowired
	public DropboxController(DropboxService dropboxService) {
		this.dropboxService = dropboxService;
	}

	/*@RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<DropboxFolder> recursiveQuery(@RequestParam Map<String,String> params) throws DbxException, IOException {
		if (params.get("path") != null) {
			return dropboxService.getFoldersByPath(params.get("recursivePath"));
		}
		return null;
    }*/

	@RequestMapping(method = RequestMethod.GET)
    public List<Metadata> findAll(@RequestParam Map<String,String> params) throws DbxException, IOException {
		if (params.get("path") != null) {
			return (List<Metadata>) dropboxService.getFolderContentsByPath(params.get("path"));
		}		
		return (List<Metadata>) dropboxService.getFolderContentsByPath(UNCATALOGED_ROOT);
    }

	@RequestMapping(value = "/image", method = RequestMethod.GET, produces="image/png")
    public byte[] outputImage(HttpServletResponse response, 
    		@RequestParam(value = "path", defaultValue = "") String path) throws DbxException, IOException {		
		InputStream in = dropboxService.getInputStreamByPath(path);
		return IOUtils.toByteArray(in);
    }

	@RequestMapping(value = "/thumb", method = RequestMethod.GET, produces="image/png")
    public byte[] outputThumbnailImage(HttpServletResponse response, 
    		@RequestParam(value = "path", defaultValue = "") String path) throws DbxException, IOException {		
		InputStream in = dropboxService.getThumbnailInputStreamByPath(path);
		return IOUtils.toByteArray(in);
    }

}
