package componentsFX;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;

import main.ClassifyImage;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;
import utils.Utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;


public class ClassifyWebcam extends VBox {
    private static Utils utils = new Utils();
    Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();

    public ClassifyWebcam() throws FrameGrabber.Exception {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        grabber.start();
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(400);
        imageView.setFitWidth(680);
        Label label = new Label("");


        Executors.newSingleThreadExecutor().execute(() -> {
            byte[] barr = null;
            IplImage img;
            Timer time = new Timer(); // Instantiate Timer Object
            ScheduledClassify scheduledTask = new ScheduledClassify(barr);
            time.schedule(scheduledTask, 3000, 3000);
            while (true) {
                Frame frame = null;
                try {
                    frame = grabber.grabFrame();
                    img = converter.convert(frame);
                    barr = utils.iplImageToByteArray(img);
                    scheduledTask.setParam(barr);
                    Platform.runLater(() -> {
                        label.setText(String.format("BEST MATCH: %s %.2f%% likely%n", scheduledTask.getResultLabel(), scheduledTask.getResultPercent()));
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImage(frameToImage(frame));
            }
        });


        label.setStyle("-fx-font-weight: bold");
        this.getChildren().add(imageView);
        this.getChildren().add(label);
        this.setAlignment(Pos.BASELINE_CENTER);


    }


    private WritableImage frameToImage(Frame frame) {
        BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public class ScheduledClassify extends TimerTask {
        byte[] param;
        private float resultPercent;
        private String resultLabel;

        public ScheduledClassify(byte[] param) {
            this.param = param;
            this.resultLabel = "";
            this.resultPercent = 0.0f;
        }

        @Override
        public void run() {
            if (param != null) {
                ArrayList<Object> result = ClassifyImage.getClassifyFromByteImage("src/inception5h/", param);
                resultLabel = (String) result.get(0);
                resultPercent = (float) result.get(1);
            }
        }

        public float getResultPercent() {
            return resultPercent;
        }

        public String getResultLabel() {
            return resultLabel;
        }

        public void setParam(byte[] param) {
            this.param = param;
        }
    }
} 
