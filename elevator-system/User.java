class User {
    private String userId;
    private String name;
    private int weight; // in kg

    public User(String userId, String name, int weight) {
        this.userId = userId;
        this.name = name;
        this.weight = weight;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public int getWeight() { return weight; }

    @Override
    public String toString() {
        return name + " (" + weight + "kg)";
    }
}
