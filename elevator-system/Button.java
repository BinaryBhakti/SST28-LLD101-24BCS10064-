abstract class Button {
    protected int floorNumber;
    protected boolean isPressed;

    public Button(int floor) {
        this.floorNumber = floor;
        this.isPressed = false;
    }

    public void press() {
        this.isPressed = true;
    }

    public void release() {
        this.isPressed = false;
    }

    public int getFloorNumber() { return floorNumber; }
    public boolean isPressed() { return isPressed; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + floorNumber + ")";
    }
}
