package jobbank;

public class query10
{
	private int locationID;
	private int jobID;
	private int number;
	private String street;
	private int zipCode;

	public query10(int locationID, int jobID, int number, String street, int zipCode)
	{
		this.locationID = locationID;
		this.jobID = jobID;
		this.number = number;
		this.street = street;
		this.zipCode = zipCode;
	}

	public int getLocationID()
	{
		return locationID;
	}

	public int getJobID()
	{
		return jobID;
	}

	public int getNumber()
	{
		return number;
	}

	public String getStreet()
	{
		return street;
	}

	public int getZipCode()
	{
		return zipCode;
	}
}
