import java.time.LocalDateTime;

class Payment {
    private String paymentId;
    private int amount;
    private PaymentStatus status;
    private LocalDateTime paymentTime;
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, UPI, etc.

    public Payment(String paymentId, int amount, String method) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentMethod = method;
        this.status = PaymentStatus.PENDING;
        this.paymentTime = null;
    }

    public void processPayment() {
        // Simulate payment processing
        this.status = PaymentStatus.SUCCESS;
        this.paymentTime = LocalDateTime.now();
    }

    public String getPaymentId() { return paymentId; }
    public int getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getPaymentTime() { return paymentTime; }
    public String getPaymentMethod() { return paymentMethod; }

    @Override
    public String toString() {
        return "Payment(" + amount + " via " + paymentMethod + " - " + status + ")";
    }
}
