package excepsions;

public class JudgeIsNotFoundException extends Exception {
	public JudgeIsNotFoundException(){
		super("please add more judges to database");
	}
}
