package org.rmcc.ccc.utils;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class ImageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

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

    private static void print(Metadata metadata) {
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

    private static void writeHtml(PrintStream ps, Iterable<PhotoLocation> photoLocations) {
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

        for (PhotoLocation photoLocation : photoLocations) {
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
                            LOGGER.error("ParseException occurred getting image date.", e);
                        }
                    }
                }
            }
        } catch (ImageProcessingException e) {
            LOGGER.error("ImageProcessingException occurred getting image date.", e);
        } catch (IOException e) {
            LOGGER.error("IOException occurred getting image date.", e);
        }
        return new Timestamp(imageDate.getTime());
    }

    /**
     * Simple tuple type, which pairs an image file with its {@link GeoLocation}.
     */
    public static class PhotoLocation {
        public final GeoLocation location;
        public final InputStream file;

        public PhotoLocation(final GeoLocation location, final InputStream file) {
            this.location = location;
            this.file = file;
        }
    }
}
