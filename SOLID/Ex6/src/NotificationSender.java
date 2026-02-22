/**
 * Base contract for all notification senders:
 * - Precondition: notification must be non-null
 * - Postcondition: always returns a non-null SendResult (never throws)
 * - Each channel uses the fields relevant to it (email, phone, subject, body)
 */
public abstract class NotificationSender {
    protected final AuditLog audit;
    protected NotificationSender(AuditLog audit) { this.audit = audit; }
    public abstract SendResult send(Notification n);
}
