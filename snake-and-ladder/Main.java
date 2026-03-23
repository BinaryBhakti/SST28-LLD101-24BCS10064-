import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter board size (n for n x n board): ");
        int n = scanner.nextInt();

        System.out.print("Enter number of players: ");
        int playerCount = scanner.nextInt();
        scanner.nextLine();

        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            System.out.print("Enter name for player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name));
        }

        System.out.print("Enter difficulty level (easy/hard): ");
        String level = scanner.nextLine().trim().toLowerCase();

        DifficultyStrategy difficulty;
        if (level.equals("hard")) {
            difficulty = new HardDifficulty();
        } else {
            difficulty = new EasyDifficulty();
        }

        System.out.println("\nCreating " + n + " x " + n + " board (" + level + " mode) with " + n + " snakes and " + n + " ladders...\n");

        Board board = new Board(n, difficulty);
        Dice dice = new Dice(6);
        Game game = new Game(board, dice, players);
        game.play();

        scanner.close();
    }
}
