package excepsions;

public class endSomeCompetitionsException extends Exception {
	public endSomeCompetitionsException(){
		super("You need to clear stadiums - finish competitions first");
	}
}
