abstract class PenDecorator implements WritingInstrument, Refillable {
    protected Pen pen;

    public PenDecorator(Pen pen) {
        this.pen = pen;
    }

    public void start() { pen.start(); }
    public void write() { pen.write(); }
    public void close() { pen.close(); }
    public void refill() { pen.refill(); }
}