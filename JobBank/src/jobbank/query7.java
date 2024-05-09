package jobbank;

public class query7
{
	private int jobID;
	private String company;
	private String profession;
	private int salary;
	private String datePosted;
	private String deadlineDate;
	private String duration;
	private String arrangement;
	private String employerFirstName;
	private String employerLastName;

	public query7(int jobID, String company, String profession, int salary, String datePosted, String deadlineDate, String duration, String arrangement, String employerFirstName, String employerLastName)
	{
		this.jobID = jobID;
		this.company = company;
		this.profession = profession;
		this.salary = salary;
		this.datePosted = datePosted;
		this.deadlineDate = deadlineDate;
		this.duration = duration;
		this.arrangement = arrangement;
		this.employerFirstName = employerFirstName;
		this.employerLastName = employerLastName;
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

	public String getEmployerFirstName()
	{
		return employerFirstName;
	}

	public String getEmployerLastName()
	{
		return employerLastName;
	}
}
