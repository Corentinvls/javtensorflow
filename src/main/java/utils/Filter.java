package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class to apply filter
 */
public class Filter {


    /**
     * Applies a given filter to an image File and save it with in the filename
     * @param imagePath
     * @param label
     * @param pathOut
     * @param filter
     * @throws Exception
     */
    public static void filter(String imagePath, String label, String pathOut, String filter) throws Exception {
        BufferedImage img = null;
        File f = null;

        //read image
        try {
            f = new File(imagePath);
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }

        //get width and height
        img = applyColor(img, filter);

        //write image
        try {
            f = new File(pathOut + "/" + label + "." + Utils.getExtension(imagePath));
            ImageIO.write(img, Utils.getExtension(imagePath), f);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Applies a filter to an BufferedImage image and returns it
     * @param img
     * @param filter
     * @return
     */
    public static BufferedImage applyColor(BufferedImage img, String filter) {
        int width = img.getWidth();
        int height = img.getHeight();

        //convert to green image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);

                int a = (p >> 24) & 0xff;
                int g = (p >> 8) & 0xff;
                int r = (p >> 16) & 0xff;
                int b = p & 0xff;
                if (filter.equals("Green"))
                    p = (a << 24) | (0 << 16) | (g << 8) | 0;
                if (filter.equals("Red"))
                    p = (a << 24) | (r << 16) | (0 << 8) | 0;
                if (filter.equals("Blue"))
                    p = (a << 24) | (0 << 16) | (0 << 8) | b;
                if (filter.equals("Black and White")) {
                    int avg = (r + g + b) / 3;
                    p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                }
                if (filter.equals("Sepia")) {
                    int tr = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                    int tg = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                    int tb = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                    //check condition
                    if (tr > 255) {
                        r = 255;
                    } else {
                        r = tr;
                    }

                    if (tg > 255) {
                        g = 255;
                    } else {
                        g = tg;
                    }

                    if (tb > 255) {
                        b = 255;
                    } else {
                        b = tb;
                    }

                    //set new RGB value
                    p = (a << 24) | (r << 16) | (g << 8) | b;
                }

                img.setRGB(x, y, p);
            }
        }
        return img;
    }
    /**
     * Apply a frame on buffered image if name done register it on src/inception5h/framed/
     * @param imageToModify
     * @param pathImageToAplly
     * @param name
     * @return
     * @throws IOException
     */
    public static BufferedImage applyFrame(BufferedImage imageToModify, String pathImageToAplly, String name) throws IOException {
        BufferedImage getImage2 = ImageIO.read(new File(pathImageToAplly));
        Graphics graphics = imageToModify.getGraphics();

        graphics.drawImage(getImage2, 0, 0, imageToModify.getWidth(), imageToModify.getHeight(), null);
        if (name != null && !name.equals(""))
            ImageIO.write(imageToModify, "png", new File("src/inception5h/framed/" + name + ".png"));
        return imageToModify;
    }

    /**
     * past an image on another at selected pos and size
     * @param imageToModify
     * @param pathImageToAplly
     * @param posX
     * @param posY
     * @param height
     * @param width
     * @return
     * @throws IOException
     */
    public static BufferedImage applyImage(BufferedImage imageToModify, String pathImageToAplly, int posX, int posY, int height, int width) throws IOException {
        BufferedImage getImage2 = ImageIO.read(new File(pathImageToAplly));
        Graphics graphics = imageToModify.getGraphics();
        graphics.drawImage(getImage2, posX, posY, width, height, null);

        return imageToModify;
    }

}
