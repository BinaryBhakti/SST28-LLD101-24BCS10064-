// Internal buttons inside elevator panel
class InternalButton extends Button {
    private String elevatorId;

    public InternalButton(String elevatorId, int floor) {
        super(floor);
        this.elevatorId = elevatorId;
    }

    public String getElevatorId() { return elevatorId; }

    @Override
    public String toString() {
        return "Elevator-" + elevatorId + "-Floor-" + floorNumber;
    }
}
