import java.time.LocalDateTime;
import java.util.*;

class Booking {
    private String bookingId;
    private User user;
    private ShowTiming showTiming;
    private List<Seat> bookedSeats;
    private BookingStatus status;
    private Payment payment;
    private LocalDateTime bookingTime;
    private int totalPrice;

    public Booking(String bookingId, User user, ShowTiming showTiming) {
        this.bookingId = bookingId;
        this.user = user;
        this.showTiming = showTiming;
        this.bookedSeats = new ArrayList<>();
        this.status = BookingStatus.PENDING;
        this.bookingTime = LocalDateTime.now();
        this.totalPrice = 0;
    }

    public void addSeat(Seat seat) throws Exception {
        if (seat.getStatus() != SeatStatus.AVAILABLE)
            throw new Exception("Seat " + seat.getSeatId() + " is not available");
        
        bookedSeats.add(seat);
        seat.setStatus(SeatStatus.RESERVED);
        totalPrice += showTiming.getPricingStrategy().calculatePrice(seat.getSeatType());
    }

    public void removeSeats() {
        for (Seat seat : bookedSeats) {
            seat.setStatus(SeatStatus.AVAILABLE);
        }
        bookedSeats.clear();
        totalPrice = 0;
    }

    public boolean confirmBooking(Payment payment) {
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            for (Seat seat : bookedSeats) {
                seat.setStatus(SeatStatus.BOOKED);
            }
            this.payment = payment;
            this.status = BookingStatus.CONFIRMED;
            return true;
        }
        return false;
    }

    public String getBookingId() { return bookingId; }
    public User getUser() { return user; }
    public ShowTiming getShowTiming() { return showTiming; }
    public List<Seat> getBookedSeats() { return bookedSeats; }
    public BookingStatus getStatus() { return status; }
    public Payment getPayment() { return payment; }
    public int getTotalPrice() { return totalPrice; }

    @Override
    public String toString() {
        return "Booking(" + bookingId + " - " + user.getName() + " [" + bookedSeats.size() + 
               " seats] ₹" + totalPrice + " - " + status + ")";
    }
}
