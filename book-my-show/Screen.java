import java.util.*;

class Screen {
    private String screenId;
    private String screenName;
    private int totalRows;
    private int totalColumns;
    private List<List<Seat>> seats;

    public Screen(String screenId, String name, int rows, int cols) {
        this.screenId = screenId;
        this.screenName = name;
        this.totalRows = rows;
        this.totalColumns = cols;
        this.seats = new ArrayList<>();

        initializeSeats();
    }

    private void initializeSeats() {
        for (int row = 0; row < totalRows; row++) {
            List<Seat> rowSeats = new ArrayList<>();
            for (int col = 0; col < totalColumns; col++) {
                String seatId = (char)('A' + row) + "-" + (col + 1);
                
                // Mix of seat types
                SeatType type;
                if (row < 3) type = SeatType.PREMIUM;
                else if (row < totalRows - 2) type = SeatType.STANDARD;
                else type = SeatType.RECLINER;
                
                rowSeats.add(new Seat(seatId, row, col, type));
            }
            seats.add(rowSeats);
        }
    }

    public String getScreenId() { return screenId; }
    public String getScreenName() { return screenName; }
    public int getTotalRows() { return totalRows; }
    public int getTotalColumns() { return totalColumns; }
    public List<List<Seat>> getSeats() { return seats; }

    public Seat getSeat(int row, int col) {
        if (row < 0 || row >= totalRows || col < 0 || col >= totalColumns)
            return null;
        return seats.get(row).get(col);
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> available = new ArrayList<>();
        for (List<Seat> row : seats) {
            for (Seat seat : row) {
                if (seat.getStatus() == SeatStatus.AVAILABLE)
                    available.add(seat);
            }
        }
        return available;
    }
}
