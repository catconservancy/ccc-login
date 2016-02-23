package org.rmcc.ccc.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
	
	private DropboxService dropboxService;

	@Autowired
	public DropboxController(DropboxService dropboxService) {
		this.dropboxService = dropboxService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Metadata> findAll(@RequestParam(value = "path", defaultValue = "") String path) throws DbxException {
		return (List<Metadata>) dropboxService.getFolderContentsByPath(path);
    }

	@RequestMapping(value = "/image", method = RequestMethod.GET, produces="image/png")
    public byte[] outputImage(HttpServletResponse response, 
    		@RequestParam(value = "path", defaultValue = "") String path) throws DbxException, IOException {		
		InputStream in = dropboxService.getInputStreamByPath(path);
		return IOUtils.toByteArray(in);
    }

}
