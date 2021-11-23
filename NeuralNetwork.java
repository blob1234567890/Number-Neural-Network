public class NeuralNetwork {
    private Neuron[][] activations;

    private int batch_size;

    public NeuralNetwork(int[] layer_sizes) {
        batch_size = 1;

        activations = new Neuron[layer_sizes.length][];

        //Initialize the neurons
        //for each layer
        for (int layer = 0; layer < activations.length; layer++) {
            activations[layer] = new Neuron[layer_sizes[layer]];
            //for each neuron in the layer
            for (int index = 0; index < activations[layer].length; index++) {
                if (layer == 0) {
                    //input neurons
                    activations[layer][index] = new Neuron(Neuron.TYPE_INPUT, layer, index);
                } else if (layer == activations.length-1) {
                    //output neurons
                    activations[layer][index] = new Neuron(Neuron.TYPE_OUTPUT, layer, index);
                } else {
                    activations[layer][index] = new Neuron(Neuron.TYPE_HIDDEN, layer, index);
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

    public void train(Reader trainI, Reader trainL, Reader testI, Reader testL) {
        int[][] batch = trainI.read(batch_size);
        for (int[] image : batch) {
            forwardProp(image);
        }
    }

    public void forwardProp(int[] image) {
        for (int i = 0; i < image.length; i++) {
            try {
                activations[0][i].setValue(image[i]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        //do the forward calculations of layers 1-output
        for (int layer = 1; layer < activations.length; layer++) {
            for (Neuron n : activations[layer]) {
                n.forwardCalc();
                if (layer == activations.length - 1) {
                    System.out.println(n);
                }
            }
        }
    }

    public String toString() {
        String s = "";

        for (int layer = 0; layer < activations.length; layer++) {
            for (int i = 0; i < activations[layer].length; i++) {
                System.out.print("+ ");
            }
            System.out.println();
            System.out.println();
        }
        return s;
    }
}
