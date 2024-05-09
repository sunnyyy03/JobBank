package jobbank;

public class query3
{
	private int employerID;
	private int jobCount;

	public query3(int employerID, int jobCount)
	{
		this.employerID = employerID;
		this.jobCount = jobCount;
	}

	public int getEmployerID()
	{
		return employerID;
	}

	public int getJobCount()
	{
		return jobCount;
	}
}
