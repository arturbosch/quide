@SuppressWarnings("ALL")
class Version1 {

	private int deadInt;

	class FeatureEnvy {
		String string;
		int i;

		public int getI() {
			return i;
		}

		public String getString() {
			return string;
		}
	}

	/**
	 * CommentSmell still dead but again a LM.
	 * @param type sss
	 * @param <T> sss
	 * @return sss
	 */
	public <T> T lm(T type) {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}

	/**
	 * No LPL anymore, all dead parameters disappear.
	 */
	public void lpl() {
		FeatureEnvy envy = new FeatureEnvy();
		envy.getI();
		envy.getI();
		envy.getI();
		envy.getString();
		envy.getString();
		envy.getString();
	}
}