package jobbank;

public class query6
{
	private int jobID;
	private String company;
	private String profession;
	private int salary;

	public query6(int jobID, String company, String profession, int salary)
	{
		this.jobID = jobID;
		this.company = company;
		this.profession = profession;
		this.salary = salary;
	}

	public int getJobID()
	{
		return jobID;
	}

	public String getCompany()
	{
		return company;
	}

	public String getProfession()
	{
		return profession;
	}

	public int getSalary()
	{
		return salary;
	}
}
