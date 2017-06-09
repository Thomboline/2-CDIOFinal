package PasswordGenerator;

public class PasswordGenerator {

	private final char[] Digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private final char[] SpecialSigns = { '.', '-', '_', '!', '+', '?', '=' };
	private final char[] UpperCaseChars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private final char[] LowerCaseChars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	/*
	 * Passwords minimum og maksimums længde ændres her.
	 */
	private final int Min = 4; 
	private final int Max = 20; 
	private String password;

	/*
	 * Kør denne her metode for at få dit password. 
	 * Er lavet med henblik på viderebygning.
	 */
	public void runPasswordGenerator() {

		this.password = PasswordGen(PasswordLength());
		System.out.println(this.password);
	}

	public String PasswordGen(int length) {

		int j;
		char[] password;
		password = new char[length];
		boolean UCC = false; boolean LCC = false;
		boolean Digit = false; boolean SS = false;
		boolean PWApproved = false;

		while (PWApproved == false) {
			
			for (int i = 0; i < length; i++) {
				
				/*
				 * Værdien er sat til 6, da jeg har valgt at der skal være dobbelt chance for at få 
				 * et bogstav i forhold til et tegn eller tal. 
				 */
				int signType = (int) ((Math.random() * 6) + 1);
				
				if (signType == 1 || signType == 2) {
					j = (int) ((Math.random() * this.LowerCaseChars.length));
					password[i] = LowerCaseChars[j];
					if (LCC == false)
						LCC = true;
				} else if (signType == 3 || signType == 4) {
					j = (int) ((Math.random() * this.UpperCaseChars.length));
					password[i] = UpperCaseChars[j];
					if (UCC == false)
						UCC = true; 
				} else if (signType == 5) {
					j = (int) ((Math.random() * this.Digits.length));
					password[i] = Digits[j];
					if (Digit == false)
						Digit = true;
				} else if (signType == 6) {
					j = (int) ((Math.random() * this.SpecialSigns.length));
					password[i] = SpecialSigns[j];
					if (SS == false)
						SS = true;
				} else
					/*
					 *  Hvis dit password indeholder et 'å' så har du lavet en fejl i signType linjen
					 *  før den nested if statement.
					 */
					password[i] = 'å';

			}
			
			/*
			 * tjekker om mindst 3 af de fire muligheder bliver brugt
			 */
			if (UCC == true && LCC == true && Digit == true && SS == false ||
				UCC == true && LCC == true && SS == true && Digit == false ||
				UCC == true && Digit == true && SS == true && LCC == false ||
				LCC == true && Digit == true && SS == true && UCC == false || 
				UCC == true && LCC == true && Digit == true && SS == true)
			{
				PWApproved = true;
			}
			
		}

		/*
		 * Laver vores char [] om til en string
		 */
		String SPassword = String.valueOf(password);

		return SPassword;
	}

	/*
	 * Bestemmer længden på vores password.
	 */
	public int PasswordLength() {

		int length = (int) ((Math.random() * this.Max) + this.Min);

		return length;
	}
}
