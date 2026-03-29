class Seat {
    private String seatId;
    private int rowNumber;
    private int columnNumber;
    private SeatType seatType;
    private SeatStatus status;

    public Seat(String seatId, int row, int col, SeatType type) {
        this.seatId = seatId;
        this.rowNumber = row;
        this.columnNumber = col;
        this.seatType = type;
        this.status = SeatStatus.AVAILABLE;
    }

    public String getSeatId() { return seatId; }
    public int getRowNumber() { return rowNumber; }
    public int getColumnNumber() { return columnNumber; }
    public SeatType getSeatType() { return seatType; }
    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }

    @Override
    public String toString() {
        return seatId + "(" + status + ")";
    }
}
