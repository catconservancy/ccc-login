package org.rmcc.ccc.controller;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.Metadata;
import org.apache.commons.io.IOUtils;
import org.rmcc.ccc.exception.InvalidImageTypeException;
import org.rmcc.ccc.service.user.DropboxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.rmcc.ccc.controller.PhotoController.UNCATALOGED_ROOT;

@RestController
@RequestMapping("/api/dropbox")
public class DropboxController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserControllerAdvice.class);

	private DropboxService dropboxService;

	@Autowired
	public DropboxController(DropboxService dropboxService) {
		this.dropboxService = dropboxService;
	}

	@RequestMapping(method = RequestMethod.GET)
    public List<Metadata> findAll(@RequestParam Map<String,String> params) throws DbxException, IOException {
		if (params.get("path") != null) {
            return dropboxService.getFolderContentsByPath(params.get("path"));
        }
        return dropboxService.getFolderContentsByPath(UNCATALOGED_ROOT);
    }

	@RequestMapping(value = "/image", method = RequestMethod.GET, produces="image/png")
    public byte[] outputImage(HttpServletResponse response,
                              @RequestParam(value = "path", defaultValue = "") String path) throws DbxException, IOException, InvalidImageTypeException {
        InputStream in = dropboxService.getInputStreamByPath(path);
		if (in == null) {
		    LOGGER.error("File is not a valid image type: " + path);
			throw new InvalidImageTypeException("File is not a valid image type: " + path);
		}
		return IOUtils.toByteArray(in);
    }

	@RequestMapping(value = "/thumb", method = RequestMethod.GET, produces="image/png")
    public byte[] outputThumbnailImage(HttpServletResponse response,
                                       @RequestParam(value = "path", defaultValue = "") String path) throws DbxException, IOException, InvalidImageTypeException {
        InputStream in;
        in = dropboxService.getThumbnailInputStreamByPath(path);
        if (in == null) {
            LOGGER.error("File is not a valid image type: " + path);
            throw new InvalidImageTypeException("File is not a valid image type: " + path, true);
        }
        return IOUtils.toByteArray(in);
    }

}
