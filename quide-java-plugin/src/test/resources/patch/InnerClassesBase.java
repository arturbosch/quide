class Test {

	private int value;

	public void method() {

	}

	class InnerTest<T extends String, B extends Boolean> {

		private class InnerInnerTest<T extends Integer> {

		}

	}

	public class Nope<Test> {

	}
}