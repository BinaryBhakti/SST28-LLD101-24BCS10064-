class Pen implements WritingInstrument, Refillable{

    private PenState state;
    private RefillStrategy refillStrategy;

    private String type;
    private String mechanism;
    private String colour;

    public Pen(String type, String mechanism, String colour, RefillStrategy refillStrategy){
        this.type = type;
        this.mechanism = mechanism;
        this.colour = colour;
        this.state = new ClosedState();
        this.refillStrategy = refillStrategy;
    }

    public void setState(PenState state){
        this.state = state;
    }

    public void start() {
        state.start(this);
    }
    
    public void write() {
        state.write(this);
    }
    public void close() {
        state.close(this);
    }
    public void refill() {
        refillStrategy.refill();
    } 

}