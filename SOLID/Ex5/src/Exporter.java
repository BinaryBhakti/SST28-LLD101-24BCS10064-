/**
 * Base contract for all exporters:
 * - Precondition: req must be non-null
 * - Postcondition: always returns a non-null ExportResult (never throws for valid input)
 * - If the exporter cannot fully process the content, it returns an error result
 *   (success == false) rather than throwing an exception.
 */
public abstract class Exporter {
    public abstract ExportResult export(ExportRequest req);
}
