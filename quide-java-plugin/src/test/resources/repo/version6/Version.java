@SuppressWarnings("ALL")
class Version {

	// moved field dead code smell
	public String stuff;
	public int otherStuff;
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

	// No more LM but CommentSmell
	public <T> T lm(T type) {
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