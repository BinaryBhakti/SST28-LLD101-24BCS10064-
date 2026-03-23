class ClosedState implements PenState {
    public void start(Pen pen) {
        pen.setState(new StartedState());
        System.out.println("Pen started");
    }

    public void write(Pen pen) {
        throw new IllegalStateException("Cannot write without starting!");
    }

    public void close(Pen pen) {
        System.out.println("Already closed");
    }
}