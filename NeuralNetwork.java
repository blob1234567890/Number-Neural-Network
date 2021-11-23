public class NeuralNetwork {
    Neuron[][] activations;

    public NeuralNetwork(int[] layer_sizes) {
        activations = new Neuron[layer_sizes.length][];

        //Initialize the neurons
        //for each layer
        for (int layer = 0; layer < activations.length; layer++) {
            activations[layer] = new Neuron[layer_sizes[layer]];
            //for each neuron in the layer
            for (int j = 0; j < activations[layer].length; j++) {
                if (layer == 0) {
                    //input neurons
                    activations[layer][j] = new Neuron(Neuron.TYPE_INPUT);
                } else if (layer == activations.length-1) {
                    //output neurons
                    activations[layer][j] = new Neuron(Neuron.TYPE_OUTPUT);
                } else {
                    activations[layer][j] = new Neuron(Neuron.TYPE_HIDDEN);
                }
            }
        }

        //give the neurons proper weights
        for (int layer = 0; layer < activations.length; layer++) {
            for (Neuron neuron : activations[layer]) {
                if (neuron.getType() == Neuron.TYPE_INPUT) {
                    neuron.setWeights(activations[layer + 1]);
                } else if (neuron.getType() == Neuron.TYPE_OUTPUT) {
                    neuron.setWeights(activations[layer - 1]);
                } else {
                    neuron.setWeights(activations[layer-1], activations[layer+1]);
                }
            }
        }
    }
}
