import java.util.ArrayList;
import java.util.TimerTask;

class ScheduledClassify extends TimerTask {
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
            ArrayList<Object> result = ClassifyImage.getClassifyFromByteImage("/Users/vallois/Documents/COURS/javatensorflow/src/inception5h/", param);
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