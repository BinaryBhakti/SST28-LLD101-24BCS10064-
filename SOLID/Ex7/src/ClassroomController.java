public class ClassroomController {
    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) { this.reg = reg; }

    public void startClass() {
        PowerDevice pjPower = (PowerDevice) reg.getFirstOfType("Projector");
        pjPower.powerOn();
        ConnectableDevice pjConn = reg.getFirstByCapability(ConnectableDevice.class);
        pjConn.connectInput("HDMI-1");

        BrightnessDevice lights = reg.getFirstByCapability(BrightnessDevice.class);
        lights.setBrightness(60);

        TemperatureDevice ac = reg.getFirstByCapability(TemperatureDevice.class);
        ac.setTemperatureC(24);

        ScannerDevice scan = reg.getFirstByCapability(ScannerDevice.class);
        System.out.println("Attendance scanned: present=" + scan.scanAttendance());
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        for (PowerDevice p : reg.getAllByCapability(PowerDevice.class)) {
            p.powerOff();
        }
    }
}
