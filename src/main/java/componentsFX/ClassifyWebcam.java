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
import utils.Filter;
import utils.Utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;


public class ClassifyWebcam extends VBox{

    Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
    ChoiceBoxFilter choiceBoxFilter=new ChoiceBoxFilter();
    public  ClassifyWebcam() throws FrameGrabber.Exception {
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
            IplImage img = null;
            Timer time = new Timer(); // Instantiate Timer Object
            ScheduledClassify scheduledTask = new ScheduledClassify(barr, img,choiceBoxFilter);
            time.schedule(scheduledTask, 3000, 3000);
            while (true) {
                Frame frame = null;
                try {
                    frame = grabber.grabFrame();
                    img= converter.convert(frame);
                    barr = Utils.iplImageToByteArray(img);
                    scheduledTask.setParam(barr);
                    scheduledTask.setImg(img);
                    scheduledTask.setChoiceBoxFilter(choiceBoxFilter);
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
        this.getChildren().add(choiceBoxFilter);
        this.setAlignment(Pos.BASELINE_CENTER);


    }


    private WritableImage frameToImage(Frame frame) {
        BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
    public static class ScheduledClassify extends TimerTask {
        private ChoiceBoxFilter choiceBoxFilter;
        private  IplImage img;
        byte[] param;
        private float resultPercent;
        private String resultLabel;

        public ScheduledClassify(byte[] param,IplImage img,ChoiceBoxFilter choiceBoxFilter) {
            this.param = param;
            this.resultLabel = "";
            this.resultPercent = 0.0f;
            this.img=img;
            this.choiceBoxFilter=choiceBoxFilter;
        }

        @Override
        public void run() {
            if (param != null) {
                Date date = new Date();
                Instant instant = date.toInstant();
                ArrayList<Object> result = ClassifyImage.getClassifyFromByteImage("src/inception5h/", param);
                resultLabel = (String) result.get(0);
                resultPercent = (float) result.get(1);
                cvSaveImage("src/inception5h/webcam/"+resultLabel+instant+".png",img);
                String filter= (String) choiceBoxFilter.getValue();
                if(filter!="aucun"){
                try {

                    Filter.filter("src/inception5h/webcam/"+resultLabel+instant+".png",resultLabel+instant,"src/inception5h/webcam/",filter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

        public void setImg(IplImage img) {
            this.img = img;
        }

        public void setChoiceBoxFilter(ChoiceBoxFilter choiceBoxFilter) {
            this.choiceBoxFilter = choiceBoxFilter;
        }
    }
} 
