package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Test {
    private static BufferedImage getFramedImage(BufferedImage imageToModify, String pathImageToAplly, String name) throws IOException {
        BufferedImage getImage2 = ImageIO.read(new File(pathImageToAplly));
        Graphics graphics = imageToModify.getGraphics();

        graphics.drawImage(getImage2, 0, 0, imageToModify.getWidth(), imageToModify.getHeight(), null);
        if (name != null && !name.equals(""))
            ImageIO.write(imageToModify, "png", new File("src/inception5h/framed/" + name + ".png"));
        return imageToModify;
    }

    public static BufferedImage applyFrame(String pathImageToModify, String pathImageToAplly, String name) {
        try {
            BufferedImage getImage1 = ImageIO.read(new File(pathImageToModify));
            return getFramedImage(getImage1, pathImageToAplly, name);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage applyFrame(BufferedImage imageToModify, String pathImageToAplly, String name) {
        try {
            return getFramedImage(imageToModify, pathImageToAplly, name);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static BufferedImage applyImage(BufferedImage imageToModify, String pathImageToAplly, int posX, int posY, int height, int width) throws IOException {
        BufferedImage getImage2 = ImageIO.read(new File(pathImageToAplly));
        Graphics graphics = imageToModify.getGraphics();
        graphics.drawImage(getImage2, posX, posY, width, height, null);

        return imageToModify;
    }
}
