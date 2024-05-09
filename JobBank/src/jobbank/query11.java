package jobbank;

public class query11
{
	private int jobID;
	private int employerID;
	private String company;
	private String profession;
	private int salary;
	private String datePosted;
	private String deadlineDate;
	private String duration;
	private String arrangement;

	public query11(int jobID, int employerID, String company, String profession, int salary, String datePosted, String deadlineDate, String duration, String arrangement)
	{
		this.jobID = jobID;
		this.employerID = employerID;
		this.company = company;
		this.profession = profession;
		this.salary = salary;
		this.datePosted = datePosted;
		this.deadlineDate = deadlineDate;
		this.duration = duration;
		this.arrangement = arrangement;
	}

	public int getJobID()
	{
		return jobID;
	}

	public int getEmployerID()
	{
		return employerID;
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

	public String getDatePosted()
	{
		return datePosted;
	}

	public String getDeadlineDate()
	{
		return deadlineDate;
	}

	public String getDuration()
	{
		return duration;
	}

	public String getArrangement()
	{
		return arrangement;
	}
}
