import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;

import java.util.Timer;
import java.util.TimerTask;

import static org.bytedeco.opencv.global.opencv_core.cvFlip;

public class Test implements Runnable {
    final int INTERVAL = 10;///you may use interval
    CanvasFrame canvas = new CanvasFrame("Web Cam");

    public Test() {
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }

    public void run() {
        byte[] barr = null;
        Timer time = new Timer(); // Instantiate Timer Object
        ScheduledClassify scheduledTask = new ScheduledClassify(barr);
        time.schedule(scheduledTask, 3000, 3000);
        FrameGrabber grabber = new OpenCVFrameGrabber(0); // 1 for next camera
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        IplImage img;

        try {
            grabber.start();

            while (true) {
                Frame frame = grabber.grab();
                img = converter.convert(frame);
                 barr = Utils.iplImageToByteArray(img);
                scheduledTask.setParam(barr);
                String resultLabel = scheduledTask.getResultLabel();
                float resultPercent = scheduledTask.getResultPercent();
                System.out.println(resultLabel);
                System.out.println(resultPercent);
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