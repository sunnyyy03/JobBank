package jobbank;

public class query4
{
	private String jobProfession;
	private int averageSalary;

	public query4(String jobProfession, int averageSalary)
	{
		this.jobProfession = jobProfession;
		this.averageSalary = averageSalary;
	}

	public String getJobProfession()
	{
		return jobProfession;
	}

	public int getAverageSalary()
	{
		return averageSalary;
	}
}
