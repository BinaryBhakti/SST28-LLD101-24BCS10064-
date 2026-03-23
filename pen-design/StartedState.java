class StartedState implements PenState{
    public void start(Pen pen){
        System.out.println("Already started");
    }

    public void write(Pen pen){
        System.out.println("Writing...");
    }

    public void close(Pen pen){
        pen.setState(new ClosedState());
        System.out.println("Pen closed");
    }
}