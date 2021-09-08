package dedup;

public class Main {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Missing File name in Arguments. Closing Program...");
			System.exit(1);
		}
		String fileName = args[0];
		Deduplicate deduplicate = new Deduplicate();
		deduplicate.dedupOutput(fileName);
	}
}
