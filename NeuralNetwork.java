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
        try {
            int[][] batchI = trainI.read(batch_size);
            /**
             * `batchL` is an int[`batch_size`][1] storing the label for each image, not the label arry.
             * i.e. if `batchL` = {{5}, {0}}, but we want {{0, 0, 0, 0, 0, 1, 0, 0, 0, 0}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0}}
             * this is for the cost function can relating the oututs.
             * I use `labelMaker` to make the 0-1 arrays
             */
            double[][] batchL = labelMaker(trainL.read(batch_size));
            for (int i = 0; i < batch_size; i++) {
                forwardProp(batchI[i]);
                backProp(batchL[i]);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
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
                n.forward();
                if (layer == activations.length - 1) {
                    System.out.println(n);
                }
            }
        }
    }

    public void backProp(double[] labels) {
        for (int i = 0; i < labels.length; i++) {

            System.out.print(labels[i] + " ");
        }
        System.out.println();

        backPropError(labels);
        desiredGradient();
    }

    public void backPropError(double[] labels) {
        for (int layer = activations.length - 1; layer > 0; layer--) {
            for (Neuron n : activations[layer]) {
                //labels is ignored for hidden layers
                n.error(labels);
            }
        }
    }

    public void desiredGradient() {
        for (int layer = 1; layer < activations.length; layer++) {
            for (Neuron n : activations[layer]) {
                n.desiredGradient();
            }
        }
    }

    public double[][] labelMaker(int[][] labels) throws IllegalArgumentException {

        double[][] relabels = new
        //same number of labels, size of a label == # of outputs
        //NOTE: this is fully initialized
        double[labels.length][activations[activations.length-1].length];
        for (int i = 0; i < labels.length; i++) {
            if (labels[i].length == 1) {
                relabels[i][labels[i][0]] = 1;
            } else {
                throw new IllegalArgumentException("Can only make label out of int[][1]");
            }
        }
        return relabels;
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
