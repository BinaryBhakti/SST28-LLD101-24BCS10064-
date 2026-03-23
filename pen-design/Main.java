public class Main {
    public static void main(String[] args) {

        Pen pen = new Pen("ball", "click", "blue", new InkRefill());

        GripDecorator gripPen = new GripDecorator(pen);

        gripPen.start();
        gripPen.write();
        gripPen.close();
        gripPen.refill();
    }
}
