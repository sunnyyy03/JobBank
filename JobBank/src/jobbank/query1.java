package jobbank;

public class query1
{
	private String employerLastName;
	private int jobsPosted;
	
	public query1(String employerLastName, int jobsPosted)
	{
		this.employerLastName = employerLastName;
		this.jobsPosted = jobsPosted;
	}
	
	public String getEmployerLastName()
	{
		return employerLastName;
	}
	
	public int getJobsPosted()
	{
		return jobsPosted;
	}
}
