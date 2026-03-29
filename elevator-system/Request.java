class Request {
    private String requestId;
    private int sourceFloor;
    private int destinationFloor;
    private RequestType requestType;
    private User user;
    private boolean isAssigned;

    public Request(String requestId, int source, int destination, RequestType type, User user) {
        this.requestId = requestId;
        this.sourceFloor = source;
        this.destinationFloor = destination;
        this.requestType = type;
        this.user = user;
        this.isAssigned = false;
    }

    public String getRequestId() { return requestId; }
    public int getSourceFloor() { return sourceFloor; }
    public int getDestinationFloor() { return destinationFloor; }
    public RequestType getRequestType() { return requestType; }
    public User getUser() { return user; }
    public boolean isAssigned() { return isAssigned; }
    public void setAssigned(boolean assigned) { this.isAssigned = assigned; }

    @Override
    public String toString() {
        return "Request(" + sourceFloor + "->" + destinationFloor + " [" + user.getName() + "])";
    }
}
