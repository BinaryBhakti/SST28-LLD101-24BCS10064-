import java.time.*;
import java.util.*;
import java.util.stream.*;

// Singleton Pattern — one BookMyShow instance across the system
// DIP — depends on PricingStrategy abstraction, not implementations
class BookMyShow {
    private static BookMyShow instance;

    private Map<String, Theater> theaters;
    private Map<String, Show> shows;
    private Map<String, ShowTiming> showTimings;
    private Map<String, Booking> bookings;
    private Map<String, User> users;

    private BookMyShow() {
        theaters = new HashMap<>();
        shows = new HashMap<>();
        showTimings = new HashMap<>();
        bookings = new HashMap<>();
        users = new HashMap<>();
    }

    public static BookMyShow getInstance() {
        if (instance == null) {
            instance = new BookMyShow();
        }
        return instance;
    }

    // Theater Management
    public void addTheater(Theater theater) {
        theaters.put(theater.getTheaterId(), theater);
    }

    public Theater getTheater(String theaterId) {
        return theaters.get(theaterId);
    }

    public List<Theater> getTheatersByCity(String city) {
        return theaters.values().stream()
                .filter(t -> t.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    // Show Management
    public void addShow(Show show) {
        shows.put(show.getShowId(), show);
    }

    public Show getShow(String showId) {
        return shows.get(showId);
    }

    // ShowTiming Management
    public void addShowTiming(ShowTiming timing) {
        showTimings.put(timing.getTimingId(), timing);
    }

    public ShowTiming getShowTiming(String timingId) {
        return showTimings.get(timingId);
    }

    public List<ShowTiming> getShowTimingsByTheater(String theaterId) {
        return showTimings.values().stream()
                .filter(st -> st.getTheater().getTheaterId().equals(theaterId))
                .collect(Collectors.toList());
    }

    public List<ShowTiming> getShowTimingsByShow(String showId) {
        return showTimings.values().stream()
                .filter(st -> st.getShow().getShowId().equals(showId))
                .collect(Collectors.toList());
    }

    // User Management
    public void registerUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    // Booking Management
    public Booking createBooking(String bookingId, String userId, String timingId) throws Exception {
        User user = getUser(userId);
        ShowTiming timing = getShowTiming(timingId);

        if (user == null || timing == null)
            throw new Exception("Invalid user or show timing");

        Booking booking = new Booking(bookingId, user, timing);
        bookings.put(bookingId, booking);
        return booking;
    }

    public Booking getBooking(String bookingId) {
        return bookings.get(bookingId);
    }

    public List<Booking> getUserBookings(String userId) {
        return bookings.values().stream()
                .filter(b -> b.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // Seat Selection (with Nearest Seat logic)
    public List<Seat> getAvailableSeatsFor(String timingId) {
        ShowTiming timing = getShowTiming(timingId);
        if (timing == null) return new ArrayList<>();
        return timing.getScreen().getAvailableSeats();
    }

    public void displayShowTimingSeats(String timingId) {
        ShowTiming timing = getShowTiming(timingId);
        if (timing == null) return;

        System.out.println("\n--- Seat Layout: " + timing.getShow().getMovieName() + " ---");
        Screen screen = timing.getScreen();

        // Display column headers
        System.out.print("     ");
        for (int col = 1; col <= screen.getTotalColumns(); col++) {
            System.out.print(" " + col + "  ");
        }
        System.out.println();

        // Display seats
        for (int row = 0; row < screen.getTotalRows(); row++) {
            System.out.print((char)('A' + row) + "    ");
            for (int col = 0; col < screen.getTotalColumns(); col++) {
                Seat seat = screen.getSeat(row, col);
                String seatDisplay = (seat.getStatus() == SeatStatus.AVAILABLE) ? "[·]" : "[X]";
                System.out.print(" " + seatDisplay + " ");
            }
            System.out.println();
        }
        System.out.println("  [·] = Available   [X] = Booked\n");
    }
}
