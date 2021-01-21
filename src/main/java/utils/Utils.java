package utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
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
    public byte[] readAllBytesOrExit(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public List<String> readAllLinesOrExit(Path path) {
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    public int bestMatch(float[] probabilities) {
        int best = 0;
        for (int i = 1; i < probabilities.length; ++i) {
            if (probabilities[i] > probabilities[best]) {
                best = i;
            }
        }
        return best;
    }

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

    public static void copyFile(String source, String name, String dest) throws IOException {
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
    public static byte[] iplImageToByteArray(IplImage img) throws IOException {
        BufferedImage im = new Java2DFrameConverter().convert(new OpenCVFrameConverter.ToIplImage().convert(img));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] barr = null;
        try{
            ImageIO.write(im,"jpg",baos);
            baos.flush();
            barr = baos.toByteArray();
        }finally{
            baos.close();
        }
        return barr;
    }
    static Image mat2Image(IplImage frame)
    {
        try
        {
            return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        }
        catch (Exception e)
        {
            System.err.println("Cannot convert the Mat obejct: " + e);
            return null;
        }
    }
    private static BufferedImage matToBufferedImage(IplImage original)
    {
        // init
        BufferedImage image = null;
        int width = original.width(), height = original.height(), channels = original.arrayChannels();
        byte[] sourcePixels = new byte[width * height * channels];
        //original.get(0, 0, sourcePixels);


        if (original.arrayChannels() > 1)
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        }
        else
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }
}
