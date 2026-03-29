import java.util.*;

class Theater {
    private String theaterId;
    private String theaterName;
    private String city;
    private String address;
    private List<Screen> screens;

    public Theater(String theaterId, String name, String city, String address) {
        this.theaterId = theaterId;
        this.theaterName = name;
        this.city = city;
        this.address = address;
        this.screens = new ArrayList<>();
    }

    public void addScreen(Screen screen) {
        screens.add(screen);
    }

    public String getTheaterId() { return theaterId; }
    public String getTheaterName() { return theaterName; }
    public String getCity() { return city; }
    public List<Screen> getScreens() { return screens; }

    public Screen getScreen(String screenId) {
        return screens.stream()
                .filter(s -> s.getScreenId().equals(screenId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return theaterName + " (" + city + ")";
    }
}
