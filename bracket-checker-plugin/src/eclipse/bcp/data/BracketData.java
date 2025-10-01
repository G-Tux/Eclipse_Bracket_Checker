package eclipse.bcp.data;

public class BracketData {

	private char bracket;

	private int lineNumber;

	private int charStart;

	private String message;

	private int weight;

	private boolean opening;

	private boolean processed;

	private boolean error;

	public BracketData(char bracket, int lineNumber, int charStart, String message, boolean opening) {
		this.bracket = bracket;
		this.lineNumber = lineNumber;
		this.charStart = charStart;
		this.message = message;
		this.weight = initWeight(bracket);
		this.opening = opening;
		this.processed = false;
		this.error = false;
	}

	public char getBracket() {
		return bracket;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public int getCharStart() {
		return charStart;
	}

	public String getMessage() {
		return message;
	}

	public int getWeight() {
		return weight;
	}

	public boolean isOpening() {
		return opening;
	}

	public boolean isClosing() {
		return !opening;
	}

	public boolean isProcessed() {
		return processed;
	}

	public boolean isNotProcessed() {
		return !processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public int initWeight(char bracket) {
		return switch (bracket) {
		case '(', ')' -> 1;
		case '[', ']' -> 2;
		case '{', '}' -> 3;
		case '<', '>' -> 4;
		default -> 0;
		};
	}

}
