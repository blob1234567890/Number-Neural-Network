public class Weight {
    private Neuron source;
    private Neuron sink;
    private double value, value_delta;

    public Weight(Neuron source, Neuron sink) {
        this.source = source;
        this.sink = sink;
        this.value = Math.random() * 8.0 - 4.0;
        value_delta = 0;
    }

    public void addDelta(double d) {
        value_delta += d;
    }

    public void avgAddDelta(int size) {
        value += value_delta / size;
        value_delta = 0;
    }

    public double forwardCalc() {
        return source.getValue() * value;
    }

    public double getValue() {
        return value;
    }

    public Neuron getSink() {
        return sink;
    }

    public Neuron getSource() {
        return source;
    }
}
