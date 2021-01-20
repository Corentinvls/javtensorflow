import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Utils {
    byte[] readAllBytesOrExit(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    List<String> readAllLinesOrExit(Path path) {
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    int bestMatch(float[] probabilities) {
        int best = 0;
        for (int i = 1; i < probabilities.length; ++i) {
            if (probabilities[i] > probabilities[best]) {
                best = i;
            }
        }
        return best;
    }

    File[] GetImageFromDir(String path) {
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

    static void copyFile(String source, String name, String dest) throws IOException {
        File copy = null;
        int i = source.lastIndexOf('.');
        String extension = null;
        if (i > 0) {
            extension = source.substring(i + 1);
        }
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
