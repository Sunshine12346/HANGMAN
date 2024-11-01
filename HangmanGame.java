import java.util.*;

class GameInstructions {
    static void displayEntryExitPrompt() {
        System.out.println();
        System.out.print("Enter 1 to start the game or 0 to exit: ");
    }
}

public class HangmanGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Player setup
        System.out.print("Enter your username: ");
        String playerName = scanner.nextLine();
        System.out.println("HELLO " + playerName + ", WELCOME TO THE HANGMAN GAME!");

        // Game setup
        final String[] hints = {"ANIMAL", "OPERATING SYSTEM", "PC COMPANY", "SMARTPHONE COMPANY", "PROGRAMMING_LANGUAGE", "WEB BROWSER"};
        final String[][] words = {
                {"RHINO", "ELEPHANT", "LION", "TIGER"},
                {"WINDOWS", "MACOS", "LINUX", "IOS", "ANDROID", "CHROMEOS"},
                {"HP", "DELL", "APPLE", "ASUS", "GOOGLE"},
                {"SAMSUNG", "XIAOMI", "APPLE", "GOOGLE", "VIVO", "OPPO"},
                {"PYTHON", "JAVA", "CSHARP", "C", "CPLUSPLUS", "LUA", "RUBY", "JAVASCRIPT"},
                {"CHROME", "FIREFOX", "OPERA", "MSEDGE", "BRAVE"}
        };

        int level = 1;
        int maxChances = 6;

        GameInstructions.displayEntryExitPrompt();
        int startGameChoice = scanner.nextInt();

        if (startGameChoice == 1) {
            startGameLoop(scanner, words, hints, playerName, level, maxChances);
        } else if (startGameChoice == 0) {
            System.out.println("Game exited.");
        } else {
            System.out.println("Invalid choice. Game exited.");
        }

        scanner.close();  // Close the scanner to avoid resource leaks
    }

    private static void startGameLoop(Scanner scanner, String[][] words, String[] hints, String playerName, int level, int maxChances) {
        while (level <= hints.length) {
            System.out.println();
            System.out.print("Enter 1 to start level " + level + " or 0 to exit: ");
            int levelChoice = scanner.nextInt();

            if (levelChoice == 1) {
                boolean levelWon = playLevel(scanner, words, hints, level, maxChances);

                if (levelWon) {
                    if (level == hints.length) {
                        System.out.println("Congratulations " + playerName + "! You have completed all levels of the Hangman Game.");
                        System.out.println("You have reached the end of the game.");
                        break;
                    } else {
                        level++;
                        System.out.println("Congratulations! You have advanced to level " + level);
                    }
                } else {
                    System.out.println("You did not guess the word correctly. Game over.");
                    break;
                }
            } else if (levelChoice == 0) {
                System.out.println("Game exited.");
                break;
            } else {
                System.out.println("Invalid choice. Please enter 1 to play or 0 to exit.");
            }
        }
    }

    private static boolean playLevel(Scanner scanner, String[][] words, String[] hints, int level, int maxChances) {
        Random random = new Random();
        String wordToGuess = words[level - 1][random.nextInt(words[level - 1].length)];
        int chancesLeft = maxChances;
        StringBuilder currentGuess = new StringBuilder("_".repeat(wordToGuess.length()));
        Set<Character> guessedLetters = new HashSet<>();

        System.out.println("Start of level " + level);
        System.out.println("Hint: " + hints[level - 1]);

        while (chancesLeft > 0 && currentGuess.toString().contains("_")) {
            System.out.println("Current guess: " + currentGuess);
            drawHangman(maxChances - chancesLeft);

            System.out.print("Enter a letter (or type the whole word to guess): ");
            String userInput = scanner.next().toUpperCase();

            if (userInput.length() > 1) {
                if (userInput.equals(wordToGuess)) {
                    System.out.println("You guessed the word!");
                    currentGuess = new StringBuilder(wordToGuess);
                    break;
                } else {
                    System.out.println("Incorrect word guess.");
                    chancesLeft--;
                }
            } else {
                char guessedLetter = userInput.charAt(0);

                if (guessedLetters.contains(guessedLetter)) {
                    System.out.println("You have already guessed this letter.");
                } else {
                    guessedLetters.add(guessedLetter);
                    if (wordToGuess.contains(Character.toString(guessedLetter))) {
                        System.out.println("Correct letter!");
                        for (int i = 0; i < wordToGuess.length(); i++) {
                            if (wordToGuess.charAt(i) == guessedLetter) {
                                currentGuess.setCharAt(i, guessedLetter);
                            }
                        }
                    } else {
                        System.out.println("Incorrect letter.");
                        chancesLeft--;
                    }
                    System.out.println("Chances left: " + chancesLeft);
                }
            }

            if (currentGuess.toString().equals(wordToGuess)) {
                System.out.println("You have guessed the word!");
                return true;
            }
        }

        if (chancesLeft == 0 && currentGuess.toString().contains("_")) {
            System.out.println("You couldn't guess the word. The word was: " + wordToGuess);
            drawHangman(maxChances);
            return false;
        }

        return true;
    }

    private static void drawHangman(int incorrectGuesses) {
        System.out.println("  +---+");
        System.out.println("  |   |");

        if (incorrectGuesses >= 1) System.out.println("  O   |");  // Head
        else System.out.println("      |");

        if (incorrectGuesses == 2) System.out.println("  |   |");       // Body only
        else if (incorrectGuesses == 3) System.out.println(" /|   |");   // One arm
        else if (incorrectGuesses >= 4) System.out.println(" /|\\  |");  // Both arms
        else System.out.println("      |");

        if (incorrectGuesses == 5) System.out.println(" /    |");       // One leg
        else if (incorrectGuesses >= 6) System.out.println(" / \\  |");  // Both legs
        else System.out.println("      |");

        System.out.println("=========");
    }

}
