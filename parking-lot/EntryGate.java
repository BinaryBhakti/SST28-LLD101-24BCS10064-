class EntryGate {
    private String gateId;
    private String gateName;
    private int position; // simulated linear position for distance calculation

    public EntryGate(String gateId, String gateName, int position) {
        this.gateId = gateId;
        this.gateName = gateName;
        this.position = position;
    }

    public String getGateId() { return gateId; }
    public String getGateName() { return gateName; }
    public int getPosition() { return position; }
}
