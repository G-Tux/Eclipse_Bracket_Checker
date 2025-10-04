package eclipse.bcp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import eclipse.bcp.data.BracketData;

@ExtendWith(BcpCoreTest.ErrorLogger.class)
public class BcpCoreTest {

	private List<BracketData> errors;

	@AfterEach
	void afterEach(TestInfo testInfo) {
		ErrorLogger.lastErrors = errors;
	}

	@Test
	public void testRemoveDirectlyMatchingBrackets() {
		String input = "{([()])}";
		errors = BcpCore.findBracketErrors(input);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void testBracketMatch() {
		String input = "{]}";
		errors = BcpCore.findBracketErrors(input);
		assertEquals(1, errors.size());
		assertTrue("']' has no opening bracket [line 1, pos 1]".equals(errors.get(0).getMessage()));
	}

	@Test
	public void testLambdaExpression() {
		String input = ".filter(x -> (x + 1) * 5 < '2')";
		errors = BcpCore.findBracketErrors(input);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void testBracketWeight() {
		String input = "<}><><><><><><{(}><><{)}>";
		errors = BcpCore.findBracketErrors(input);
		assertEquals(3, errors.size());
		assertTrue("'}' has no opening bracket [line 1, pos 1]".equals(errors.get(0).getMessage()));
		assertTrue("'(' has no closing bracket [line 1, pos 15]".equals(errors.get(1).getMessage()));
		assertTrue("')' has no opening bracket [line 1, pos 22]".equals(errors.get(2).getMessage()));
	}

	@Test
	public void testBracketCounter() {
		String input = "<<<<[>>><[>";
		errors = BcpCore.findBracketErrors(input);
		assertEquals(3, errors.size());
		assertTrue("'<' has no closing bracket [line 1, pos 0]".equals(errors.get(0).getMessage()));
		assertTrue("'[' has no closing bracket [line 1, pos 4]".equals(errors.get(1).getMessage()));
		assertTrue("'[' has no closing bracket [line 1, pos 9]".equals(errors.get(2).getMessage()));
	}

	@Test
	public void testBracketLineNumber() {
		String input = """
				<>
				>()
				>[]
				<>
				""";
		errors = BcpCore.findBracketErrors(input);
		assertEquals(2, errors.size());
		assertTrue("'>' has no opening bracket [line 2, pos 3]".equals(errors.get(0).getMessage()));
		assertTrue("'>' has no opening bracket [line 3, pos 7]".equals(errors.get(1).getMessage()));
	}

	@Test
	public void testPerformance() {
		String repeated = """
				<{
				()
				[]
				}>""".repeat(1000);

		String inputWithOneIssue = repeated + "]" + repeated;

		Assertions.assertTimeout(Duration.ofMillis(50), () -> {
			errors = BcpCore.findBracketErrors(inputWithOneIssue);
		});

	}

	static class ErrorLogger implements TestWatcher {
		static List<BracketData> lastErrors;

		@Override
		public void testFailed(ExtensionContext context, Throwable cause) {
			System.out.println("Test: " + context.getDisplayName() + " Assertion failed: " + cause);

			if (lastErrors != null) {
				for (BracketData e : lastErrors) {
					System.out.println(e.getMessage());
				}
			}

			System.out.println("");
		}
	}

}