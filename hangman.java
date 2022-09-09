import java.util.Random;
import java.util.HashMap;
import java.util.Scanner;

class Hangman {

    // Words available for hangman

    static String[] wordArray = new String[] {"hello", "world", "there", "here", "italian"};

    // Possible statuses for game

    enum gameStatus {
        LOST,
        IN_PROGRESS,
        WON
    }

    // Primitive components of hangman game

    private String answer;
    private String wordAttempt;
    private int attemptsAllowed;
    private int attempts;
    private HashMap<Character, Integer> letters = new HashMap<>();
    private gameStatus status;

    // Constructor

    Hangman (int attemptsAllowed) {

        this.attemptsAllowed = attemptsAllowed;
        Random rand = new Random();
        int randInt = rand.nextInt(wordArray.length);
        this.answer = wordArray[randInt];
        String starter = "";
        for (int i = 0; i < this.answer.length(); i++) {
            starter += "_";
            
            letters.put(this.answer.charAt(i), 1 + this.letters.getOrDefault(this.answer.charAt(i), 0));

        }
        this.wordAttempt = starter;
        this.status = gameStatus.IN_PROGRESS;

    }

    // Setters

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void setWordAttempt(String wordAttempt) {
        this.wordAttempt = wordAttempt;
    }

    public void setGameStatus(gameStatus status) {
        this.status = status;
    }

    // Getters

    public int getAttempts() {
        return this.attempts;
    }

    public String getWordAttempt() {
        return this.wordAttempt;
    }

    public gameStatus getGameStatus() {
        return this.status;
    }

    // Functions

    public void updateBoard(Character input) {

        // letters is the hashmap containing the letters necessary to win

        if (letters.containsKey(input)) {
            letters.remove(input);
        } else {
            attempts++;
        }

        if (letters.isEmpty()) {
            status = gameStatus.WON;
        }else if (attempts < attemptsAllowed) {
            status = gameStatus.IN_PROGRESS;
        }else{
            status = gameStatus.LOST;
        }

        this.updateAttempString(input);

        return;
    }

    public void wordGuess(String input) {

        if (input.equals(this.answer)) {
            this.setGameStatus(gameStatus.WON);
            this.setWordAttempt(input);
        }else{
            System.out.println("\nWrong Word!\n" + this.attempts);
            attempts++;
            if (!(attempts < this.attemptsAllowed)) {
                this.setGameStatus(gameStatus.LOST);
            }
        }

        return;
    }

    public String getInput() {

        Scanner keyboard = new Scanner(System.in);
        int attLeft = this.attemptsAllowed - this.attempts;
        System.out.println("Enter a letter, you have " + attLeft + " attempts left\n");
        String inputString = keyboard.next().toLowerCase();
        return inputString;
    
    }

    public void updateAttempString(Character input) {
        String newAttempt = "";
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == input && wordAttempt.charAt(i) == '_') {
                newAttempt += (input);
            }else if (97 <= wordAttempt.charAt(i) && wordAttempt.charAt(i) <= 122) {
                newAttempt += wordAttempt.charAt(i);
            }else{
                newAttempt += "_";
            }
        }
        wordAttempt = newAttempt;
        return;
    }

    public static boolean isValidInput(String input) {
        for (int i = 0; i < input.length(); i++) {
            int inputASCII = (int) input.charAt(i);
            if (97 > inputASCII || inputASCII > 122) {
                return false;
            }
        }
        return true;
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  

    // Main

    public static void main (String[] args) {

        int allowedAttempts = 7;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-a")) {
                allowedAttempts = Integer.parseInt(args[i+1]);
            }
        }
        
        Hangman newGame = new Hangman(allowedAttempts);

        while (newGame.getGameStatus() == gameStatus.IN_PROGRESS) {
            
            clearScreen();
            System.out.println(newGame.getWordAttempt() + "\n");
            String inputString = newGame.getInput();
            Character inputChar = inputString.trim().charAt(0);
            boolean isValid = isValidInput(inputString);

            
            if (inputString.length() == 1 && isValid) {
                newGame.updateBoard(inputChar);
            }else if (inputString.length() > 1 && isValid) {
                newGame.wordGuess(inputString);
            }else{
                System.out.println("\nInput was invalid, please input a letter or word\n");
                continue;
            }
            
        }

        clearScreen();
        if (newGame.getGameStatus() == gameStatus.WON) {
            System.out.println(newGame.getWordAttempt());
            System.out.println("\nCongratulations, you won!");
        }else{
            System.out.println(newGame.answer);
            System.out.println("\nSorry, you Lost.");
        }
        
    }
}