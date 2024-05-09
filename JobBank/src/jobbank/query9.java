package jobbank;

public class query9
{
	private int employerID;
	private String employerFirstName;
	private String employerLastName;
	private int averageSalary;
	private int salaryVariance;

	public query9(int employerID, String employerFirstName, String employerLastName, int averageSalary, int salaryVariance)
	{
		this.employerID = employerID;
		this.employerFirstName = employerFirstName;
		this.employerLastName = employerLastName;
		this.averageSalary = averageSalary;
		this.salaryVariance = salaryVariance;
	}

	public int getEmployerID()
	{
		return employerID;
	}

	public String getEmployerFirstName()
	{
		return employerFirstName;
	}

	public String getEmployerLastName()
	{
		return employerLastName;
	}

	public int getAverageSalary()
	{
		return averageSalary;
	}

	public int getSalaryVariance()
	{
		return salaryVariance;
	}
}