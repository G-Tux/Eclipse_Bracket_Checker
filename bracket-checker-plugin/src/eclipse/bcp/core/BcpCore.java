package eclipse.bcp.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import eclipse.bcp.data.BracketData;
import eclipse.bcp.utils.BcpUtils;

public class BcpCore {

	public static List<BracketData> findBracketErrors(String content) {
		List<BracketData> errors = new ArrayList<>();
		List<BracketData> bracketList = BcpUtils.toBracketList(content);

		bracketList = removeDirectlyMatchingBrackets(bracketList);

		for (int x = 0; x < bracketList.size(); x++) {
			BracketData currentBracket = bracketList.get(x);

			int bracketCounter = 0;

			for (int y = x + 1; y < bracketList.size(); y++) {
				BracketData candidate = bracketList.get(y);

				if (candidate.isOpening() && currentBracket.getWeight() == candidate.getWeight()) {
					bracketCounter++;
				}

				if (BcpUtils.isBracketMatch(currentBracket.getBracket(), candidate.getBracket())) {
					if (bracketCounter == 0) {
						currentBracket.setProcessed(true);
						candidate.setProcessed(true);
						break;
					} else {
						bracketCounter--;
					}
				} else if (currentBracket.getWeight() < candidate.getWeight()) {
					break;
				}
			}

			if (currentBracket.isNotProcessed()) {
				currentBracket.setError(true);
			}
		}

		for (BracketData bracketData : bracketList) {
			if (bracketData.isError()) {
				errors.add(bracketData);
			}
		}

		return errors;
	}

	public static List<BracketData> removeDirectlyMatchingBrackets(List<BracketData> bracketList) {
		Stack<BracketData> stack = new Stack<>();

		for (BracketData bracketData : bracketList) {
			if (!stack.isEmpty() && BcpUtils.isBracketMatch(stack.peek().getBracket(), bracketData.getBracket())) {
				stack.pop();
			} else {
				stack.push(bracketData);
			}
		}

		return new ArrayList<>(stack);
	}

}
