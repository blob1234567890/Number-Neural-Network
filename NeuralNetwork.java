public class NeuralNetwork {
    /**
     * Activations ordering is [layer][neuron]. E.g:
     * + (0,0)                    + (3,0)
     *          + (1,0)  + (2,0)
     * + (0,1)                    + (3,1)
     *          + (1,1)  + (2,1)
     * + (0,2)                    + (3,2)
     */
    private double[][] activations;
    /**
     * Weights are ordered sink_index, source_neuron.
     * So the weight connecting (1,0) to (2,1) would be weights[2,1,0] and
     * the weight connection (1,1) to (2,1) would be weights[2,1,1]
     *
     * NOTE: Since layer 0 (the input) has no source, weights[0] can be effectively ignored
     */
    private double[][][] weights;
    /**
     * `biases` are ordered the same as `activations`
     *
     * NOTE: bises[0] can be effectively ignored for the same reason as in weights
     */
    private double[][] biases;

    public NeuralNetwork(int[] layer_sizes) {
        // TODO: update in accordance to new rules
        activations = new double[layer_sizes.length][];

        weights = new double[layer_sizes.length][][];
        biases = new double[layer_sizes.length][];

        //input has no previous layer so should be instantiated outside the loop
        activations[0] = new double[layer_sizes[0]];
        for (int i = 1; i < layer_sizes.length; i++) {
            activations[i] = new double[layer_sizes[i]];
            biases[i] = new double[layer_sizes[i]];
            // instantiate biases with random values: -1 -> 1
            for (int j = 0; j < biases.length; j++) {
                biases[i][j] = Math.random() * 2.0 - 1.0;
            }
            weights[i] = new double[layer_sizes[i]][layer_sizes[i-1]];
            //instantiate every weight with a random value: -1 -> 1
            for (int j = 0; j < weights.length; j++) {
                for (int k = 0; k < weights[j].length; j++) {
                    weights[i][j][k] = Math.random() * 2.0 - 1.0;
                }
            }
        }
    }


}
