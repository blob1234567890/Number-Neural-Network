public class Runner {
    public static void main(String[] args) {
        Reader trainI_reader = new Reader("./Training_Data/train-images-idx3-ubyte", Reader.TYPE_FILE_IDX_IMAGE);
        Reader trainL_reader = new Reader("./Training_Data/train-labels-idx1-ubyte", Reader.TYPE_FILE_IDX_IMAGE);
        Reader testI_reader = new Reader("./Testing_Data/t10k-images-idx3-ubyte", Reader.TYPE_FILE_IDX_IMAGE);
        Reader testL_reader = new Reader("./Testing_Data/t10k-labels-idx1-ubyte", Reader.TYPE_FILE_IDX_IMAGE);

        NeuralNetwork nn = new NeuralNetwork(
            /**
             * We are using the MNIST data base so the range of values
             * in `trainL_reader` is 0-9 (inclusive).
             * TODO: find range of `trainL_reader` w/out being given
             * TODO: try different hidden layer numbers and sizes
             */
            new int[] {trainI_reader.getImageSize(), 16, 16, 10}
        );
        nn.train(trainI_reader, trainL_reader, testI_reader, testL_reader);
    }
}
