

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        Utils utils =new Utils();

        // path : ../../inception5h/
        if (args.length != 2) {
            System.exit(1);
        }
        String modelDir = args[0];
        String imageFile = args[1];

        byte[] graphDef = utils.readAllBytesOrExit(Paths.get(modelDir, "tensorflow_inception_graph.pb"));
        List<String> labels = utils.readAllLinesOrExit(Paths.get(modelDir, "labels.txt"));
        byte[] imageBytes = utils.readAllBytesOrExit(Paths.get(imageFile));
    }
}
