class GripDecorator extends PenDecorator {

    public GripDecorator(Pen pen) {
        super(pen);
    }

    @Override
    public void write() {
        System.out.println("Writing with grip");
        pen.write();
    }
}