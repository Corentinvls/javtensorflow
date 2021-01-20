

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ClassifyImage {
    private static Utils utils = new Utils();
    private static TFUtils tfutils = new TFUtils();

    /**
     * Function to display the result of an image classifaction in the terminal
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // path : ../../inception5h/
        if (args.length != 2) {
            System.exit(1);
        }
        String modelDir = args[0];
        String imageFile = args[1];
        displayClassify(modelDir, imageFile);
    }

    static void displayClassify(String modelDir, String imageFile) {
        List<String> labels = utils.readAllLinesOrExit(Paths.get(modelDir, "labels.txt"));
        float[] labelProbabilities = getClassify(modelDir, imageFile);
        System.out.println(Arrays.toString(labelProbabilities));
        int bestLabelIdx = utils.bestMatch(labelProbabilities);
        System.out.printf("BEST MATCH: %s (%.2f%% likely)%n",
                labels.get(bestLabelIdx),
                labelProbabilities[bestLabelIdx] * 100f);
    }

    static void ArrayClassify(String modelDir, String imageDir, String saveDir) {
        File[] imageList = utils.GetImageFromDir(imageDir);
        System.out.println(Arrays.toString(imageList));
        for (File image : imageList) {
            displayClassify(modelDir, image.getAbsolutePath());
        }
    }

    private static float[] getClassify(String modelDir, String imageFile) {
        byte[] graphDef = utils.readAllBytesOrExit(Paths.get(modelDir, "tensorflow_inception_graph.pb"));
        byte[] imageBytes = utils.readAllBytesOrExit(Paths.get(imageFile));
        try (Tensor image = tfutils.byteBufferToTensor(imageBytes)) {
            return executeClassify(graphDef, image);
        }
    }

    /**
     * Function to run the Image Classification
     *
     * @param graphDef
     * @param image
     * @return
     */
    private static float[] executeClassify(byte[] graphDef, Tensor<Float> image) {
        try (Graph g = new Graph()) {
            g.importGraphDef(graphDef);
            try (Session s = new Session(g);
                 Tensor<Float> result =
                         s.runner().feed("input", image).fetch("output").run().get(0).expect(Float.class)) {
                final long[] rshape = result.shape();
                if (result.numDimensions() != 2 || rshape[0] != 1) {
                    throw new RuntimeException(
                            String.format(
                                    "Expected model to produce a [1 N] shaped tensor where N is the number of labels, instead it produced one with shape %s",
                                    Arrays.toString(rshape)));
                }
                int nlabels = (int) rshape[1];
                return result.copyTo(new float[1][nlabels])[0];
            }
        }
    }

}
