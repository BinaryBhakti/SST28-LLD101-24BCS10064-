import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BookMyShow platform = BookMyShow.getInstance();

        // ============= Setup: Create Theaters =============
        Theater theaters_mumbai_1 = new Theater("T1", "PVR Cinemas", "Mumbai", "Dadar, Mumbai");
        Theater theaters_mumbai_2 = new Theater("T2", "INOX", "Mumbai", "Bandra, Mumbai");
        Theater theaters_bangalore = new Theater("T3", "Cinepolis", "Bangalore", "Whitefield, Bangalore");
        
        platform.addTheater(theaters_mumbai_1);
        platform.addTheater(theaters_mumbai_2);
        platform.addTheater(theaters_bangalore);

        // ============= Setup: Create Screens =============
        // PVR Cinemas — Screen 1: 6 rows x 10 cols
        Screen screen_1 = new Screen("S1", "Premium Screen", 6, 10);
        theaters_mumbai_1.addScreen(screen_1);

        // PVR Cinemas — Screen 2: 8 rows x 8 cols
        Screen screen_2 = new Screen("S2", "Standard Screen", 8, 8);
        theaters_mumbai_1.addScreen(screen_2);

        // INOX — Screen 1: 7 rows x 10 cols
        Screen screen_3 = new Screen("S3", "IMAX Screen", 7, 10);
        theaters_mumbai_2.addScreen(screen_3);

        // ============= Setup: Create Shows =============
        Show movie_1 = new Show("SH1", "Dune 2", "English", "Sci-Fi", 166);
        Show movie_2 = new Show("SH2", "3 Idiots", "Hindi", "Comedy-Drama", 170);
        Show movie_3 = new Show("SH3", "Dark Knight", "English", "Thriller", 152);

        movie_1.setStatus(ShowStatus.NOW_SHOWING);
        movie_2.setStatus(ShowStatus.NOW_SHOWING);
        movie_3.setStatus(ShowStatus.COMING_SOON);

        platform.addShow(movie_1);
        platform.addShow(movie_2);
        platform.addShow(movie_3);

        // ============= Setup: Create Show Timings =============
        LocalDateTime now = LocalDateTime.now();
        
        // Morning show (Weekday pricing)
        ShowTiming morning = new ShowTiming(
            "ST1", movie_1, theaters_mumbai_1, screen_1,
            now.plusDays(1).withHour(10).withMinute(0),
            new WeekdayPricing()
        );
        platform.addShowTiming(morning);

        // Evening show (Weekend pricing)
        ShowTiming evening = new ShowTiming(
            "ST2", movie_1, theaters_mumbai_1, screen_1,
            now.plusDays(2).withHour(18).withMinute(0),
            new WeekendPricing()
        );
        platform.addShowTiming(evening);

        // Night show (Weekday pricing)
        ShowTiming night = new ShowTiming(
            "ST3", movie_2, theaters_mumbai_1, screen_2,
            now.plusDays(1).withHour(21).withMinute(30),
            new WeekdayPricing()
        );
        platform.addShowTiming(night);

        // ============= Setup: Register Users =============
        User user1 = new User("U1", "Amit Kumar", "amit@email.com", "9876543210");
        User user2 = new User("U2", "Priya Sharma", "priya@email.com", "9123456789");
        User user3 = new User("U3", "Rahul Verma", "rahul@email.com", "9999888877");

        platform.registerUser(user1);
        platform.registerUser(user2);
        platform.registerUser(user3);

        System.out.println("========== BookMyShow Demo ==========\n");

        // ============= Display Theaters =============
        System.out.println("--- Available Theaters in Mumbai ---");
        List<Theater> mumbaiTheaters = platform.getTheatersByCity("Mumbai");
        for (Theater t : mumbaiTheaters) {
            System.out.println("  • " + t);
        }

        // ============= Display Shows =============
        System.out.println("\n--- Now Showing Movies ---");
        System.out.println("  • " + movie_1);
        System.out.println("  • " + movie_2);

        // ============= Show Seat Layout =============
        platform.displayShowTimingSeats("ST1");

        // ============= Booking Flow =============
        System.out.println("--- Booking Flow ---\n");

        // User 1 books tickets
        System.out.println("Step 1: User creates a booking");
        Booking booking1 = platform.createBooking("B1", "U1", "ST1");
        System.out.println("  Created: " + booking1);

        // Add seats to booking
        System.out.println("\nStep 2: User selects seats");
        Seat seat_A1 = screen_1.getSeat(0, 0);
        Seat seat_A2 = screen_1.getSeat(0, 1);
        booking1.addSeat(seat_A1);
        booking1.addSeat(seat_A2);
        System.out.println("  Selected seats: " + seat_A1.getSeatId() + ", " + seat_A2.getSeatId());
        System.out.println("  Total Price: ₹" + booking1.getTotalPrice());

        // Process Payment
        System.out.println("\nStep 3: Process payment");
        Payment payment1 = new Payment("P1", booking1.getTotalPrice(), "CREDIT_CARD");
        payment1.processPayment();
        System.out.println("  " + payment1);

        // Confirm Booking
        System.out.println("\nStep 4: Confirm booking");
        boolean confirmed = booking1.confirmBooking(payment1);
        System.out.println("  Booking confirmed: " + confirmed);
        System.out.println("  Final Status: " + booking1);

        // ============= Second Booking =============
        System.out.println("\n--- Second Booking ---\n");
        
        Booking booking2 = platform.createBooking("B2", "U2", "ST2");
        Seat seat_D3 = screen_1.getSeat(3, 2);
        Seat seat_D4 = screen_1.getSeat(3, 3);
        Seat seat_D5 = screen_1.getSeat(3, 4);
        
        booking2.addSeat(seat_D3);
        booking2.addSeat(seat_D4);
        booking2.addSeat(seat_D5);
        
        System.out.println("Booking: " + booking2);
        System.out.println("Selected Seats: " + seat_D3.getSeatId() + ", " + seat_D4.getSeatId() + ", " + seat_D5.getSeatId());
        System.out.println("Total Price (Weekend): ₹" + booking2.getTotalPrice());

        Payment payment2 = new Payment("P2", booking2.getTotalPrice(), "UPI");
        payment2.processPayment();
        booking2.confirmBooking(payment2);
        System.out.println("Payment: " + payment2);
        System.out.println("Status: " + booking2.getStatus());

        // ============= Show Updated Seat Layout =============
        System.out.println("\n--- Updated Seat Layout ---");
        platform.displayShowTimingSeats("ST1");

        // ============= User Bookings History =============
        System.out.println("--- User Bookings History ---\n");
        List<Booking> user1Bookings = platform.getUserBookings("U1");
        System.out.println("Bookings for " + user1.getName() + ":");
        for (Booking b : user1Bookings) {
            System.out.println("  • " + b);
        }

        // ============= Show Availability =============
        System.out.println("\n--- Seat Availability ---");
        List<Seat> availableSeats = platform.getAvailableSeatsFor("ST1");
        System.out.println("Available seats for " + morning.getShow().getMovieName() + ": " + availableSeats.size());

        System.out.println("\n========== Demo Complete ==========");
    }
}
