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
		bracketList = removeAngleBracketsInsideLambdaExpressions(bracketList);

		for (int x = 0; x < bracketList.size(); x++) {
			BracketData currentBracket = bracketList.get(x);

			int bracketLevel = 0;

			for (int y = x + 1; y < bracketList.size(); y++) {
				BracketData candidate = bracketList.get(y);

				if (candidate.isOpening() && currentBracket.getWeight() == candidate.getWeight()) {
					bracketLevel++;
				}

				if (BcpUtils.isBracketMatch(currentBracket.getBracket(), candidate.getBracket())) {
					if (bracketLevel == 0) {
						currentBracket.setProcessed(true);
						candidate.setProcessed(true);
						break;
					} else {
						bracketLevel--;
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

	public static List<BracketData> removeAngleBracketsInsideLambdaExpressions(List<BracketData> bracketList) {
		List<BracketData> result = new ArrayList<>();
		int roundLevel = 0;

		for (BracketData b : bracketList) {
			char currentBracket = b.getBracket();

			if (BcpUtils.isRoundOpen(currentBracket))
				roundLevel++;
			else if (BcpUtils.isRoundClose(currentBracket) && roundLevel > 0)
				roundLevel--;

			boolean isNotLambdaExpression = roundLevel == 0;

			if (isNotLambdaExpression || BcpUtils.isNotAngleBracket(currentBracket)) {
				result.add(b);
			}
		}

		return result;
	}

}
