package jobbank;

public class query8
{
	private int employerID;
	private String firstName;
	private String lastName;
	private int averageSalary;

	public query8(int employerID, String firstName, String lastName, int averageSalary)
	{
		this.employerID = employerID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.averageSalary = averageSalary;
	}

	public int getEmployerID()
	{
		return employerID;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public int getAverageSalary()
	{
		return averageSalary;
	}
}
