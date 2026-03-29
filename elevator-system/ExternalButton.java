// External buttons on each floor (UP/DOWN)
class ExternalButton extends Button {
    private RequestType requestType;

    public ExternalButton(int floor, RequestType type) {
        super(floor);
        this.requestType = type;
    }

    public RequestType getRequestType() { return requestType; }

    @Override
    public String toString() {
        return "Floor-" + floorNumber + "-" + requestType;
    }
}
