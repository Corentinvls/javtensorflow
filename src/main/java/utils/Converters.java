package utils;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Converters {

    /**
     * Converts an iplImage to a ByteArray
     *
     * @param img
     * @return
     * @throws IOException
     */
    public static byte[] iplImageToByteArray(IplImage img) throws IOException {
        BufferedImage im = new Java2DFrameConverter().convert(new OpenCVFrameConverter.ToIplImage().convert(img));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] barr = null;
        try {
            ImageIO.write(im, "jpg", baos);
            baos.flush();
            barr = baos.toByteArray();
        } finally {
            baos.close();
        }
        return barr;
    }

    /**
     * Converts an iplImage to a BufferedImage
     *
     * @param src
     * @return
     */
    public static BufferedImage convertIplImageToBuffImage(IplImage src) {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter paintConverter = new Java2DFrameConverter();
        Frame frame = grabberConverter.convert(src);
        return paintConverter.getBufferedImage(frame, 1);
    }

    /**
     * Converts a BufferedImage to an IplImage
     *
     * @param img
     * @return
     */
    public static IplImage convertBuffToIplImage(BufferedImage img) {
        Java2DFrameConverter converter1 = new Java2DFrameConverter();
        OpenCVFrameConverter.ToIplImage converter2 = new OpenCVFrameConverter.ToIplImage();
        IplImage iploriginal = converter2.convert(converter1.convert(img));
        return iploriginal.clone();
    }
}