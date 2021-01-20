import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.bytedeco.opencv.global.opencv_core.cvFlip;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

public class Test implements Runnable {
    final int INTERVAL = 10;///you may use interval
    CanvasFrame canvas = new CanvasFrame("Web Cam");

    public Test() {
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }

    public void run() {

        FrameGrabber grabber = new OpenCVFrameGrabber(0); // 1 for next camera
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        IplImage img;
        int i = 0;
        try {
            grabber.start();

            while (true) {
                Frame frame = grabber.grab();
                img = converter.convert(frame);
                byte[] barr = Utils.iplImageToByteArray(img);
                if(i%150==0){
                    System.out.println("go");
                    ClassifyImage.getClassifyFromByteImage("/Users/vallois/Documents/COURS/javatensorflow/src/inception5h/",barr);
                }
               i++;
                //the grabbed frame will be flipped, re-flip to make it right
                cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise;
                canvas.showImage(converter.convert(img));

                Thread.sleep(INTERVAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        Test gs = new Test();
        Thread th = new Thread(gs);
        th.start();
    }
}