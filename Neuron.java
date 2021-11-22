import java.util.Map;
import java.util.HashMap;

public class Neuron {
    //input neurons have null forward_weights
    public static final byte TYPE_INPUT = 0;
    //hidden neurons have both forward_weights and back_weights
    public static final byte TYPE_HIDDEN = 1;
    //output neurons have null back_weights
    public static final byte TYPE_OUTPUT = 2;

    private Weight[] forward_weights = null; //all the weights used in forward prop
    private Weight[] back_weights = null; //all the weights used in back prop

    private int neuron_type;

    public Neuron(Neuron[] sources, Neuron[] sinks) {
        neuron_type = TYPE_HIDDEN;

        forward_weights = new Weight[sources.length];
        back_weights = new Weight[sinks.length];

        //randomize the weights where this is the source
        for (int i = 0; i < back_weights.length; i++) {
            back_weights[i] = new Weight(this, sinks[i]);
        }

        for (int i = 0; i < forward_weights.length; i++) {
            forward_weights[i] = sources[i].getSink(i);
        }
    }

    public Neuron(Neuron[] others, int type) {
        neuron_type = type;

        if (neuron_type == TYPE_INPUT) {
            back_weights = new Weight[others.length];
            for (int i = 0; i < back_weights.length; i++) {
                back_weights[i] = new Weight(this, others[i]);
            }
        } else if (neuron_type == TYPE_OUTPUT) {
            forward_weights = new Weight[others.length];
            for (int i = 0; i < forward_weights.length; i++) {
                forward_weights[i] = others[i].getSink(i);
            }
        }
    }

    public Weight getSink(int i) {
        return forward_weights[i];
    }
}
