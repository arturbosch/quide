import java.util.ArrayList;

class Test {

	private int value;

	public void method() {

	}

	class InnerTest<T extends Integer, B extends Integer> {

		private class InnerInnerTest<T extends Integer> {

		}

	}

	public class Nope<Test> {

	}
}