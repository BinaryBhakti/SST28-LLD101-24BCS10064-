import java.util.*;

class Floor {
    private int floorNumber;
    private FloorStatus status;
    private ExternalButton upButton;
    private ExternalButton downButton;
    private List<User> usersWaiting;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.status = FloorStatus.ACTIVE;
        this.upButton = new ExternalButton(floorNumber, RequestType.UP);
        this.downButton = new ExternalButton(floorNumber, RequestType.DOWN);
        this.usersWaiting = new ArrayList<>();
    }

    public int getFloorNumber() { return floorNumber; }
    public FloorStatus getStatus() { return status; }
    public void setStatus(FloorStatus status) { this.status = status; }
    
    public ExternalButton getUpButton() { return upButton; }
    public ExternalButton getDownButton() { return downButton; }
    
    public void addUserWaiting(User user) { usersWaiting.add(user); }
    public void removeUserWaiting(User user) { usersWaiting.remove(user); }
    public List<User> getUsersWaiting() { return usersWaiting; }

    public boolean canAccessButton(RequestType type) {
        if (status == FloorStatus.MAINTENANCE) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Floor-" + floorNumber + " [" + status + "] (" + usersWaiting.size() + " waiting)";
    }
}
