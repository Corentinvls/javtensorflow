package utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Utils {
    /**
     * Reads all bytes of a file and returns them as array of bytes
     * @param path
     * @return
     */
    public byte[] readAllBytesOrExit(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    /**
     * Reads all lines of a file and returns them as array of strings
     * @param path
     * @return
     */
    public List<String> readAllLinesOrExit(Path path) {
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    /**
     * Returns the highest value for a list of Floats
     * @param probabilities
     * @return
     */
    public int bestMatch(float[] probabilities) {
        int best = 0;
        for (int i = 1; i < probabilities.length; ++i) {
            if (probabilities[i] > probabilities[best]) {
                best = i;
            }
        }
        return best;
    }

    /**
     * Returns a list of image files for a given directory
     * @param path
     * @return
     */
    public File[] GetImageFromDir(String path) {
        File dir = new File(path);
        String[] EXTENSIONS = new String[]{
                "gif", "png", "jpg", "jpeg"
        };
        FilenameFilter IMAGE_FILTER = new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                for (final String ext : EXTENSIONS) {
                    if (name.endsWith("." + ext)) {
                        return (true);
                    }
                }
                return (false);
            }
        };
        return dir.listFiles(IMAGE_FILTER);
    }

    /**
     * Gets the file extension for a given file path
     * @param path
     * @return
     */
    public static String getExtension(String path) {
        int i = path.lastIndexOf('.');

        if (i > 0) {
            return path.substring(i + 1);
        }
        return null;
    }

    /**
     * Copies a file to a given directory with a specific name
     * @param source
     * @param name
     * @param dest
     * @throws IOException
     */
    public static void copyFile(String source, String name, String dest) throws IOException {
        File copy = null;

        String extension = getExtension(source);
        copy = new File(dest + "/" + name + "." + extension);

        BufferedImage image = null;
        File f = null;

        try {
            f = new File(source);
            image = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        try {
            ImageIO.write(image, extension, copy);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
