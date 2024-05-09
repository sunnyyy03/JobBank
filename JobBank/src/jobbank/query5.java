package jobbank;

public class query5
{
	private int jobID;
	private String company;
	private String profession;
	private int salary;
	private String date_posted;
	private String deadline_date;
	private String duration;
	private String arrangement;
	private String employer_first_name;
	private String employer_last_name;
	private int location_number;
	private String location_street;
	private int location_zip_code;
	private String job_experience_level;
	private String job_education_level;

	public query5(int jobID, String company, String profession, int salary, String date_posted, String deadline_date, String duration, String arrangement, String employer_first_name, String employer_last_name, int location_number, String location_street, int location_zip_code, String job_experience_level, String job_education_level)
	{
		this.jobID = jobID;
		this.company = company;
		this.profession = profession;
		this.salary = salary;
		this.date_posted = date_posted;
		this.deadline_date = deadline_date;
		this.duration = duration;
		this.arrangement = arrangement;
		this.employer_first_name = employer_first_name;
		this.employer_last_name = employer_last_name;
		this.location_number = location_number;
		this.location_street = location_street;
		this.location_zip_code = location_zip_code;
		this.job_experience_level = job_experience_level;
		this.job_education_level = job_education_level;
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

	public String getDate_posted()
	{
		return date_posted;
	}

	public String getDeadline_date()
	{
		return deadline_date;
	}

	public String getDuration()
	{
		return duration;
	}

	public String getArrangement()
	{
		return arrangement;
	}

	public String getEmployer_first_name()
	{
		return employer_first_name;
	}

	public String getEmployer_last_name()
	{
		return employer_last_name;
	}

	public int getLocation_number()
	{
		return location_number;
	}

	public String getLocation_street()
	{
		return location_street;
	}

	public int getLocation_zip_code()
	{
		return location_zip_code;
	}

	public String getJob_experience_level()
	{
		return job_experience_level;
	}

	public String getJob_education_level()
	{
		return job_education_level;
	}
}
