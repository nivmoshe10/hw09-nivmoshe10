import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps a window to a list of CharData objects.
    HashMap<String, List> CharDataMap;

    // The window length used in this model.
    int windowLength;

    // The random number generator used by this model. 
    private Random randomGenerator;

    /** Constructs a language model with the given window length and a 
     * random number generator (for testing). */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length and a 
     * random number generator (with a seed). */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Computes a probability distribution for the given list of CharData objects. */
    public void calculateProbabilities(List probs) {
        int totalChars = 0;
        Node current = probs.first;
        while (current != null) {
            totalChars += current.cp.count;
            current = current.next;
        }

        double cumulativeProb = 0.0;
        current = probs.first;
        while (current != null) {
            current.cp.p = (double) current.cp.count / totalChars;
            cumulativeProb += current.cp.p;
            current.cp.cp = cumulativeProb;
            current = current.next;
        }
    }

    /** Returns a random character from the given probabilities list. */
    public char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        Node current = probs.first;
        while (current != null) {
            if (current.cp.cp > r) {
                return current.cp.chr;
            }
            current = current.next;
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    /**
     * Trains the model based on the given file.
     * This method reads the file char by char, creates windows, and updates the map.
     */
    public void train(String fileName) {
        String window = "";
        char c;
        In in = new In(fileName); 

        // Reads just enough characters to form the first window
        for (int i = 0; i < windowLength; i++) {
            if (!in.isEmpty()) {
                window += in.readChar();
            }
        }

        while (!in.isEmpty()) {
            c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c);
            window = window.substring(1) + c;
        }

        for (List probs : CharDataMap.values()) {
            calculateProbabilities(probs);
        }
    }

    /** Generates a random text, based on the probability distribution that was learned. */
    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) {
            return initialText;
        }

        String window = initialText.substring(initialText.length() - windowLength);
        String generatedText = initialText;

        while (generatedText.length() < textLength) {
            List probs = CharDataMap.get(window);
            if (probs == null) {
                break;
            }
            char nextChar = getRandomChar(probs);
            generatedText += nextChar;
            window = generatedText.substring(generatedText.length() - windowLength);
        }

        return generatedText;
    }
}