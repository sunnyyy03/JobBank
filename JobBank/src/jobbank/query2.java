package jobbank;

public class query2
{
	private String applicantName;
	private int numberOfApplications;

	public query2(String applicantName, int numberOfApplications)
	{
		this.applicantName = applicantName;
		this.numberOfApplications = numberOfApplications;
	}

	public String getApplicantName()
	{
		return applicantName;
	}

	public int getNumberOfApplications()
	{
		return numberOfApplications;
	}
}
