import java.util.*;

public class DeviceRegistry {
    private final java.util.List<Object> devices = new ArrayList<>();

    public void add(Object d) { devices.add(d); }

    public Object getFirstOfType(String simpleName) {
        for (Object d : devices) {
            if (d.getClass().getSimpleName().equals(simpleName)) return d;
        }
        throw new IllegalStateException("Missing: " + simpleName);
    }

    public <T> T getFirstByCapability(Class<T> capability) {
        for (Object d : devices) {
            if (capability.isInstance(d)) return capability.cast(d);
        }
        throw new IllegalStateException("Missing capability: " + capability.getSimpleName());
    }

    public <T> List<T> getAllByCapability(Class<T> capability) {
        List<T> result = new ArrayList<>();
        for (Object d : devices) {
            if (capability.isInstance(d)) result.add(capability.cast(d));
        }
        return result;
    }
}
