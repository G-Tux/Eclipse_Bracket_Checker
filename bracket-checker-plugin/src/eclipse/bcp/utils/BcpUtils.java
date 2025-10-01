package eclipse.bcp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eclipse.bcp.data.BracketData;

public class BcpUtils {

	private static final char ROUND_OPEN = '(';
	private static final char ROUND_CLOSE = ')';

	private static final char SQUARE_OPEN = '[';
	private static final char SQUARE_CLOSE = ']';

	private static final char CURLY_OPEN = '{';
	private static final char CURLY_CLOSE = '}';

	private static final char ANGLE_OPEN = '<';
	private static final char ANGLE_CLOSE = '>';

	public static boolean isOpeningBracket(char c) {
		return c == ROUND_OPEN || c == SQUARE_OPEN || c == CURLY_OPEN || c == ANGLE_OPEN;
	}

	public static boolean isClosingBracket(char c) {
		return c == ROUND_CLOSE || c == SQUARE_CLOSE || c == CURLY_CLOSE || c == ANGLE_CLOSE;
	}

	public static boolean isBracket(char c) {
		return isOpeningBracket(c) || isClosingBracket(c);
	}

	public static boolean isBracketMatch(char open, char close) {
		return (open == ROUND_OPEN && close == ROUND_CLOSE) || (open == SQUARE_OPEN && close == SQUARE_CLOSE)
				|| (open == CURLY_OPEN && close == CURLY_CLOSE) || (open == ANGLE_OPEN && close == ANGLE_CLOSE);
	}

	public static List<BracketData> toBracketList(String content) {

		List<BracketData> bracketList = new ArrayList<>();

		int pos = 0;
		int line = 1;

		for (char c : content.toCharArray()) {

			if (c == '\n') {
				line++;
			}

			if (isBracket(c)) {
				boolean opening = isOpeningBracket(c);

				String errorMsg = "'" + c + "' has no " + (opening ? "closing" : "opening") + " bracket" + " [line "
						+ line + ", pos " + pos + "]";

				bracketList.add(new BracketData(c, line, pos, errorMsg, opening));
			}

			pos++;
		}

		return bracketList;
	}

	public static String toString(List<BracketData> bracketList) {
		return bracketList.stream().map(x -> String.valueOf(x.getBracket())).collect(Collectors.joining());
	}

}
