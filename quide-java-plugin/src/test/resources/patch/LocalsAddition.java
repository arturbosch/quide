class LocalsBase {

	private static final int NORMAL = 0;
	private static final int EASY = -1;
	private static final int VERY_EASY = -2;
	private static final int HARD = 1;
	private static final int VERY_HARD = 2;

	public static void main(String[] args) {
		int value = Integer.parseInt(args[0]);

		if (value == NORMAL) {
			System.out.println(NORMAL);
		} else if (value == EASY) {
			System.out.println(EASY);
		} else if (value == VERY_EASY) {
			System.out.println(VERY_EASY);
		} else if (value == HARD) {
			System.out.println(HARD);
		} else if (value == VERY_HARD) {
			System.out.println(VERY_HARD);
		}

		System.out.println("ENDE");


		if (NORMAL == 0 && EASY == -1 && VERY_EASY == -2 && HARD == 1 || VERY_HARD == 2 || 42 != 1) {
			System.out.println("STUFF");
		}

		int neverUsed = -1;

		int stuff = builder().moreStuff()
				.moreStuff()
				.moreStuff()
				.moreStuff();
	}

}