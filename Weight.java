public class Weight {
    private Neuron source;
    private Neuron sink;
    private double value;

    public Weight(Neuron source, Neuron sink) {
        this.source = source;
        this.sink = sink;
        this.value = Math.random() * 8.0 - 4.0;
    }
}
