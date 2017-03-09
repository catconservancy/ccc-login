package org.rmcc.ccc.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.iptc.IptcReader;
import com.mysema.query.types.expr.BooleanExpression;
import org.rmcc.ccc.model.Photo;
import org.rmcc.ccc.repository.PhotoPredicatesBuilder;
import org.rmcc.ccc.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.rmcc.ccc.controller.PhotoController.ARCHIVED_ROOT;
import static org.rmcc.ccc.controller.PhotoController.UNCATALOGED_ROOT;

@Service
public class PhotoService {

    private static final int DEFAULT_DAYS_BACK = 30;

    private PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<Photo> getPhotos(Map<String,String> params, Pageable pageable) {

        Sort sort = pageable.getSort() != null ? pageable.getSort() : new Sort(Sort.Direction.ASC,"imageDate");

        List<Photo> photos = new ArrayList<Photo>();

        Integer studyAreaId =  getInteger(params, "studyAreaId");
        Integer locationId =  getInteger(params, "locationId");
        Boolean highlighted = getBoolean(params, "highlighted");
        Timestamp startDate = getStartDate(params.get("startDate"));
        Timestamp endDate = getEndDate(params.get("endDate"));
        Integer[] speciesIds = getSpeciesIds(params.get("speciesIds"));

        PhotoPredicatesBuilder builder = new PhotoPredicatesBuilder();

        if (studyAreaId != null)
            builder.with("deployment.studyArea.id", ":", studyAreaId);
        if (locationId != null)
            builder.with("deployment.id", ":", locationId);
        if (speciesIds != null && speciesIds.length > 0)
            builder.with("species.id", "in", speciesIds);
        if (highlighted != null)
            builder.with("highlight", ":", highlighted);
        builder.with("imageDate", ">", startDate);
        builder.with("imageDate", "<", endDate);

        BooleanExpression exp = builder.build();
        photoRepository.findAll(exp, pageable).forEach(photos::add);

        return photos;
    }

    public String convertToArchivedPath(Photo photo) {

        String archivedPath = null;

        if (photo.getDropboxPath() != null) {
            archivedPath = photo.getDropboxPath().toLowerCase().replace(UNCATALOGED_ROOT, ARCHIVED_ROOT + "/archived study area photos");
            if (photo.getHighlight() != null && photo.getHighlight()) {
                archivedPath = archivedPath.replace("/archived study area photos", "/highlight photos");
                String[] pathElements = archivedPath.split("/");
                String highlightPath = "";
                for (int i = 0; i < 6; i++)
                    highlightPath += pathElements[i] + "/";

                long timestamp = photo.getImageDate().getTime();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(timestamp);
                int imageYear = cal.get(Calendar.YEAR);

                archivedPath = highlightPath + imageYear + "/";
            }
        }
        return archivedPath;
    }

    private Integer[] getSpeciesIds(String speciesIdsStr) {
        String[] speciesIdsStrings = new String[0];
        if (speciesIdsStr != null && !"".equalsIgnoreCase(speciesIdsStr)) {
            speciesIdsStrings = speciesIdsStr.split(",");
        }
        Integer[] speciesIds = new Integer[speciesIdsStrings.length];
        for (int i = 0; i < speciesIdsStrings.length; i++) {
            speciesIds[i] = Integer.parseInt(speciesIdsStrings[i]);
        }
        return speciesIds.length > 0 ? speciesIds : null;
    }

    private Timestamp getStartDate(String startDateStr) {
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -DEFAULT_DAYS_BACK);

        Timestamp startDate = new Timestamp(cal.getTime().getTime());

        if (startDateStr != null) {
            Date start = convertToDate(startDateStr);
            if (start != null) {
                startDate = new Timestamp(start.getTime());
            }
        }

        return startDate;
    }

    private Date convertToDate(String startDateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            return df.parse(startDateString.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Timestamp getEndDate(String endDateStr) {
        Date today = new Date();
        Timestamp endDate = new Timestamp(today.getTime());

        if (endDateStr != null) {
            Date end = convertToDate(endDateStr);
            endDate = new Timestamp(end.getTime());
        }

        return endDate;
    }

    private Integer getInteger(Map<String,String> params, String param) {
        return params.get(param) != null ? Integer.parseInt(params.get(param)) : null;
    }

    private Boolean getBoolean(Map<String,String> params, String param) {
        return params.get(param) != null ? Boolean.valueOf(params.get(param)) : null;
    }

    public Timestamp getImageDate(InputStream inputStream) {
        Date imageDate = new Date();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    String tagStr = tag.toString().toLowerCase();
                    if (tagStr.indexOf("date") > -1) {
                        String dateStr = tagStr.substring(tagStr.indexOf("- ") + 2);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                        try {
                            return new Timestamp(sdf.parse(dateStr).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Timestamp(imageDate.getTime());
    }

	public static void getFileMetadata(final InputStream file, String fileName) throws ImageProcessingException, IOException {
//        File file = new File("Tests/Data/withIptcExifGps.jpg");

        // There are multiple ways to get a Metadata object for a file

        //
        // SCENARIO 1: UNKNOWN FILE TYPE
        //
        // This is the most generic approach.  It will transparently determine the file type and invoke the appropriate
        // readers.  In most cases, this is the most appropriate usage.  This will handle JPEG, TIFF, GIF, BMP and RAW
        // (CRW/CR2/NEF/RW2/ORF) files and extract whatever metadata is available and understood.
        //
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            print(metadata);
            

			Collection<PhotoLocation> photoLocations = new ArrayList<PhotoLocation>();
			Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
			if (gpsDirectories != null) {
			    for (GpsDirectory gpsDirectory : gpsDirectories) {
			        // Try to read out the location, making sure it's non-zero
			        GeoLocation geoLocation = gpsDirectory.getGeoLocation();
			        if (geoLocation != null && !geoLocation.isZero()) {
			            // Add to our collection for use below
			            photoLocations.add(new PhotoLocation(geoLocation, file));
			            break;
			        }
			    }
			}

			// Write output to the console.
			// You can pipe this to a file if you like, or alternatively modify the output stream here
			// to be a file or network stream.
			PrintStream ps = new PrintStream(System.out);

			writeHtml(ps, photoLocations);

			// Make sure we flush the stream before exiting.  If you use a different type of stream, you
			// may need to close it here instead.
			ps.flush();
        } catch (ImageProcessingException e) {
            // handle exception
        } catch (IOException e) {
            // handle exception
        }

        //
        // SCENARIO 2: SPECIFIC FILE TYPE
        //
        // If you know the file to be a JPEG, you may invoke the JpegMetadataReader, rather than the generic reader
        // used in approach 1.  Similarly, if you knew the file to be a TIFF/RAW image you might use TiffMetadataReader,
        // PngMetadataReader for PNG files, BmpMetadataReader for BMP files, or GifMetadataReader for GIF files.
        //
        // Using the specific reader offers a very, very slight performance improvement.
        //
        try {
            Metadata metadata = JpegMetadataReader.readMetadata(file);

            print(metadata);
        } catch (JpegProcessingException e) {
            // handle exception
        } catch (IOException e) {
            // handle exception
        }

        //
        // APPROACH 3: SPECIFIC METADATA TYPE
        //
        // If you only wish to read a subset of the supported metadata types, you can do this by
        // passing the set of readers to use.
        //
        // This currently only applies to JPEG file processing.
        //
        try {
            // We are only interested in handling
            Iterable<JpegSegmentMetadataReader> readers = Arrays.asList(new ExifReader(), new IptcReader());

            Metadata metadata = JpegMetadataReader.readMetadata(file, readers);

            print(metadata);
        } catch (JpegProcessingException e) {
            // handle exception
        } catch (IOException e) {
            // handle exception
        }
    }

    private static void print(Metadata metadata)
    {
        System.out.println("-------------------------------------");

        // Iterate over the data and print to System.out

        //
        // A Metadata object contains multiple Directory objects
        //
        for (Directory directory : metadata.getDirectories()) {

            //
            // Each Directory stores values in Tag objects
            //
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }

            //
            // Each Directory may also contain error messages
            //
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.println("ERROR: " + error);
                }
            }
        }
    }

    /**
     * Simple tuple type, which pairs an image file with its {@link GeoLocation}.
     */
    public static class PhotoLocation
    {
        public final GeoLocation location;
        public final InputStream file;

        public PhotoLocation(final GeoLocation location, final InputStream file)
        {
            this.location = location;
            this.file = file;
        }
    }

    private static void writeHtml(PrintStream ps, Iterable<PhotoLocation> photoLocations)
    {
        ps.println("<!DOCTYPE html>");
        ps.println("<html>");
        ps.println("<head>");
        ps.println("<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />");
        ps.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>");
        ps.println("<style>html,body{height:100%;margin:0;padding:0;}#map_canvas{height:100%;}</style>");
        ps.println("<script type=\"text/javascript\" src=\"https://maps.googleapis.com/maps/api/js?sensor=false\"></script>");
        ps.println("<script type=\"text/javascript\">");
        ps.println("function initialise() {");
        ps.println("    var options = { zoom:2, mapTypeId:google.maps.MapTypeId.ROADMAP, center:new google.maps.LatLng(0.0, 0.0)};");
        ps.println("    var map = new google.maps.Map(document.getElementById('map_canvas'), options);");
        ps.println("    var marker;");

        for (PhotoLocation photoLocation : photoLocations)
        {
            final String fullPath = "TODO"; /* photoLocation.file.getAbsoluteFile().toString().trim().replace("\\", "\\\\");*/

            ps.println("    marker = new google.maps.Marker({");
            ps.println("        position:new google.maps.LatLng(" + photoLocation.location + "),");
            ps.println("        map:map,");
            ps.println("        title:\"" + fullPath + "\"});");
            ps.println("    google.maps.event.addListener(marker, 'click', function() { document.location = \"" + fullPath + "\"; });");
        }

        ps.println("}");
        ps.println("</script>");
        ps.println("</head>");
        ps.println("<body onload=\"initialise()\">");
        ps.println("<div id=\"map_canvas\"></div>");
        ps.println("</body>");
        ps.println("</html>");
    }

}
