import java.util.*;

class Game {
    private Board board;
    private Dice dice;
    private Queue<Player> players;
    private List<Player> winners;

    public Game(Board board, Dice dice, List<Player> players) {
        this.board = board;
        this.dice = dice;
        this.players = new LinkedList<>(players);
        this.winners = new ArrayList<>();
    }

    public void play() {
        System.out.println("\n--- Game Start ---\n");

        while (players.size() > 1) {
            Player current = players.poll();
            int diceValue = dice.roll();
            int oldPosition = current.getPosition();
            int newPosition = oldPosition + diceValue;

            System.out.println(current.getName() + " (at " + oldPosition + ") rolled " + diceValue);

            if (newPosition > board.getSize()) {
                System.out.println("  Can't move beyond " + board.getSize() + ". Stays at " + oldPosition);
                players.add(current);
                continue;
            }

            newPosition = board.getFinalPosition(newPosition);
            current.setPosition(newPosition);
            System.out.println("  " + current.getName() + " moved to " + newPosition);

            if (newPosition == board.getSize()) {
                winners.add(current);
                System.out.println("  " + current.getName() + " wins! (Rank #" + winners.size() + ")\n");
            } else {
                players.add(current);
            }
        }

        if (!players.isEmpty()) {
            Player last = players.poll();
            System.out.println(last.getName() + " is the last player remaining.\n");
        }

        System.out.println("--- Game Over ---");
        System.out.println("Final Rankings:");
        for (int i = 0; i < winners.size(); i++) {
            System.out.println("  #" + (i + 1) + " " + winners.get(i).getName());
        }
    }
}
