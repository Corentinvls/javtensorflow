package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class to apply filter
 */
public class Filter {


    /**
     * @param imagePath
     * @param pathOut
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
        img=applyColor(img, filter);

        //write image
        try {
            f = new File(pathOut + "/" + label + "." + Utils.getExtension(imagePath));
            ImageIO.write(img, Utils.getExtension(imagePath), f);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static BufferedImage applyColor( BufferedImage img,String filter) {
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


}
