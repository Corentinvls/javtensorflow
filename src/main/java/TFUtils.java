import org.tensorflow.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TFUtils {

    Tensor executeSavedModel(String modelFolderPath, Tensor input) {
        try {
            Path path = Paths.get(ClassLoader.getSystemClassLoader().getResource(modelFolderPath).toURI()).toAbsolutePath();
            //Parse model, and read all bytes or exit
            SavedModelBundle model = SavedModelBundle.load(path.toString(),"serve");
            List<Tensor<?>> results = model.session().runner().feed("input", input).fetch("output").run();
            Tensor result = results.get(0);
            return result;
        } catch (URISyntaxException e) {
            throw new Error("invalid path");
        }
    }

    Tensor executeModelFromByteArray(byte[] graphDef, Tensor input) {
        try (Graph g = new Graph()) {
            g.importGraphDef(graphDef);
            try (Session s = new Session(g)) {
                Tensor result = s.runner().feed("input", input).fetch("output").run().get(0);
                return result;
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
    Tensor byteBufferToTensor(byte[] imageBytes) {
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
            final int H =224;
            final int W =224;

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
