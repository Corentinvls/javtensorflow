package main;

import utils.Utils;
import utils.GraphBuilder;
import org.tensorflow.*;


import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassifyImage {
    private static Utils utils = new Utils();

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

    static ArrayList<Object> displayClassify(String modelDir, String imageFile) {
        List<String> labels = utils.readAllLinesOrExit(Paths.get(modelDir, "labels.txt"));
        float[] labelProbabilities = getClassify(modelDir, imageFile);

        int bestLabelIdx = utils.bestMatch(labelProbabilities);
        String find = labels.get(bestLabelIdx);
        float percent = labelProbabilities[bestLabelIdx] * 100f;
        System.out.printf("BEST MATCH: %s (%.2f%% likely)%n",
                find,
                percent);
        ArrayList<Object> result = new ArrayList<>();
        result.add(find);
        result.add(percent);
        result.add(imageFile);
        return result;
    }

    static List<ArrayList<Object>> ArrayClassify(String modelDir, String imageDir) {
        File[] imageList = utils.GetImageFromDir(imageDir);
        ArrayList<ArrayList<Object>> result = new ArrayList<>();
        for (File image : imageList) {
            result.add(displayClassify(modelDir, image.getAbsolutePath()));
        }
        return result;
    }

    private static float[] getClassify(String modelDir, String imageFile) {
        byte[] graphDef = utils.readAllBytesOrExit(Paths.get(modelDir, "tensorflow_inception_graph.pb"));
        byte[] imageBytes = utils.readAllBytesOrExit(Paths.get(imageFile));
        try (Tensor image = byteBufferToTensor(imageBytes)) {
            return executeClassify(graphDef, image);
        }
    }

    public static ArrayList<Object> getClassifyFromByteImage(String modelDir, byte[] imageBytes) {
        List<String> labels = utils.readAllLinesOrExit(Paths.get(modelDir, "labels.txt"));
        byte[] graphDef = utils.readAllBytesOrExit(Paths.get(modelDir, "tensorflow_inception_graph.pb"));
        ;
        try (Tensor image = byteBufferToTensor(imageBytes)) {
            float[] labelProbabilities = executeClassify(graphDef, image);
            int bestLabelIdx = utils.bestMatch(labelProbabilities);
            ArrayList<Object> result = new ArrayList<>();
            result.add(labels.get(bestLabelIdx));
            result.add(labelProbabilities[bestLabelIdx] * 100f);
            return result;
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

    /**
     * create a Tensor from an image
     * <p>
     * Scale and normalize an image (to 224x224), and convert to a tensor
     *
     * @param imageBytes
     * @return
     */
    private static Tensor byteBufferToTensor(byte[] imageBytes) {
        try (Graph g = new Graph()) {
            GraphBuilder graphBuilder = new GraphBuilder(g);
            // Some constants specific to the pre-trained model at:
            // https://storage.googleapis.com/download.tensorflow.org/models/inception5h.zip
            //
            // - The model was trained with images scaled to 224x224 pixels.
            // - The colors, represented as R, G, B in 1-byte each were converted to
            //   float using (value - Mean)/Scale.
            final float mean = 117f;
            final float scale = 1f;
            final int H = 224;
            final int W = 224;

            // Since the graph is being constructed once per execution here, we can use a constant for the
            // input image. If the graph were to be re-used for multiple input images, a placeholder would
            // have been more appropriate.
            final Output input = graphBuilder.constant("input", imageBytes);
            final Output output =
                    graphBuilder.div(
                            graphBuilder.sub(
                                    graphBuilder.resizeBilinear(
                                            graphBuilder.expandDims(
                                                    graphBuilder.cast(graphBuilder.decodeJpeg(input, 3), DataType.FLOAT),
                                                    graphBuilder.constant("make_batch", 0)),
                                            graphBuilder.constant("size", new int[]{H, W})),
                                    graphBuilder.constant("mean", mean)),
                            graphBuilder.constant("scale", scale));
            try (Session s = new Session(g)) {
                return s.runner().fetch(output.op().name()).run().get(0);
            }
        }
    }

}
