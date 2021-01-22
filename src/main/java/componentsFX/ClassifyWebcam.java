package componentsFX;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import main.ClassifyImage;
import main.Test;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;
import utils.Filter;
import utils.Utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;


public class ClassifyWebcam extends VBox {

    Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
    private String labels[] = {"aucun", "vert", "rouge", "bleu", "noir et blanc", "sepia"};
    private String labelsFrame[] = {"Doré", "Trait"};


    public ClassifyWebcam(Stage stage) throws FrameGrabber.Exception {
//FILTRE
        CheckBox checkBoxFilter = new CheckBox();
        ChoiceBoxCustom choiceBoxFilter = new ChoiceBoxCustom(labels);
        FlowPane flowPaneFilter=new FlowPane();
        flowPaneFilter.getChildren().add(checkBoxFilter);
        flowPaneFilter.getChildren().add(choiceBoxFilter);
//CADRES
        CheckBox checkBoxFrame = new CheckBox();
        ChoiceBoxCustom choiceBoxFrame = new ChoiceBoxCustom(labelsFrame);
        FlowPane flowPaneFrame=new FlowPane();
        flowPaneFrame.getChildren().add(checkBoxFrame);
        flowPaneFrame.getChildren().add(choiceBoxFrame);
//IMAGE
        CheckBox checkBoxImageToPaste = new CheckBox();
        ButtonSelectFilePath buttonSelectImage=new ButtonSelectFilePath("Choose image",stage);
        Spinner<Integer> spinnerX = new Spinner<Integer>(0, 10000, 0);
        Spinner<Integer> spinnerY = new Spinner<Integer>(0, 10000, 0);
        Spinner<Integer> spinnerH = new Spinner<Integer>(0, 10000, 50);
        Spinner<Integer> spinnerW = new Spinner<Integer>(0, 10000, 50);
        spinnerX.setEditable(true);
        spinnerY.setEditable(true);
        FlowPane flowPaneImage=new FlowPane();
        flowPaneImage.getChildren().addAll(checkBoxImageToPaste,buttonSelectImage,spinnerX,spinnerY,spinnerH,spinnerW);

        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        grabber.start();
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(400);
        imageView.setFitWidth(700);
        Label label = new Label("");


        Executors.newSingleThreadExecutor().execute(() -> {
            byte[] barr = null;
            IplImage img = null;
            Timer time = new Timer(); // Instantiate Timer Object
            ScheduledClassify scheduledTask = new ScheduledClassify(barr, img, choiceBoxFilter);
            time.schedule(scheduledTask, 3000, 3000);
            while (true) {
                Frame frame = null;
                BufferedImage imgBuff = null;
                try {
                    frame = grabber.grabFrame();
                    img = converter.convert(frame);
                    barr = Utils.iplImageToByteArray(img);
                    System.out.println(buttonSelectImage.getPath());
                    if (choiceBoxFrame.getValue() != null && checkBoxFrame.isSelected()) {
                        imgBuff = Test.applyFrame(Utils.convertIplImageToBuffImage(img), "src/frame/" + choiceBoxFrame.getValue() + ".png", null);
                        img = Utils.convertBuffToIplImage(imgBuff);
                    }
                    if(buttonSelectImage.getPath()!=null&& !buttonSelectImage.getPath().equals("src") && checkBoxImageToPaste.isSelected()){
                       imgBuff = Test.applyImage(Utils.convertIplImageToBuffImage(img),
                               buttonSelectImage.getPath(),
                               spinnerX.getValue(),spinnerY.getValue(),
                               spinnerH.getValue(),spinnerW.getValue());
                        img = Utils.convertBuffToIplImage(imgBuff);
                    }

                        imgBuff = Utils.convertIplImageToBuffImage(img);




                    scheduledTask.setParam(barr);
                    scheduledTask.setImg(img);
                    scheduledTask.setChoiceBoxFilter(choiceBoxFilter);
                    Platform.runLater(() -> {
                        label.setText(String.format("BEST MATCH: %s %.2f%% likely%n", scheduledTask.getResultLabel(), scheduledTask.getResultPercent()));
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImage(SwingFXUtils.toFXImage(imgBuff, null));
            }
        });


        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");
        this.getChildren().add(imageView);
        this.getChildren().add(label);
        this.setAlignment(Pos.BASELINE_CENTER);
        this.getChildren().add(flowPaneFilter);
        this.getChildren().add(flowPaneFrame);
        this.getChildren().add(flowPaneImage);




    }


    public static class ScheduledClassify extends TimerTask {
        private ChoiceBoxCustom choiceBoxFilter;
        private IplImage img;
        byte[] param;
        private float resultPercent;
        private String resultLabel;

        public ScheduledClassify(byte[] param, IplImage img, ChoiceBoxCustom choiceBoxFilter) {
            this.param = param;
            this.resultLabel = "";
            this.resultPercent = 0.0f;
            this.img = img;
            this.choiceBoxFilter = choiceBoxFilter;
        }

        @Override
        public void run() {
            if (param != null) {
                ArrayList<Object> result = ClassifyImage.getClassifyFromByteImage("src/inception5h/", param);
                Date date = new Date();
                long instant = date.getTime();
                resultLabel = (String) result.get(0);
                resultPercent = (float) result.get(1);
                cvSaveImage("src/inception5h/webcam/" + instant + resultLabel + ".png", img);

                String filter = (String) choiceBoxFilter.getValue();
                if (filter != null && !filter.equals("aucun")) {
                    try {
                        Filter.filter("src/inception5h/webcam/" + instant + resultLabel + ".png", instant + resultLabel, "src/inception5h/webcam/", filter);
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

        public void setChoiceBoxFilter(ChoiceBoxCustom choiceBoxFilter) {
            this.choiceBoxFilter = choiceBoxFilter;
        }
    }
} 
