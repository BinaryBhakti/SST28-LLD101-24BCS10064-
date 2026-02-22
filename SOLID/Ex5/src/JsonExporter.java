import java.nio.charset.StandardCharsets;

/**
 * JSON export: handles null request consistently with base contract
 * (precondition requires non-null request; null fields are escaped to empty string).
 */
public class JsonExporter extends Exporter {
    @Override
    public ExportResult export(ExportRequest req) {
        String json = "{\"title\":\"" + escape(req.title) + "\",\"body\":\"" + escape(req.body) + "\"}";
        return new ExportResult("application/json", json.getBytes(StandardCharsets.UTF_8));
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"");
    }
}
