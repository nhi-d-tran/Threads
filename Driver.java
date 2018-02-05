import java.util.concurrent.*;

public class Driver 
{
	static int numofStudents = 14;
	static int defaultNumber = 0;
	static Semaphore lineOrgo = new Semaphore(defaultNumber);
	static Semaphore lineCalculus = new Semaphore(defaultNumber);
	static Semaphore lineTheory = new Semaphore(defaultNumber);
	static Semaphore examWait = new Semaphore(defaultNumber);
	static Semaphore waitInstructor = new Semaphore(defaultNumber);
	
	
	public static void main(String [] args)
	{
		Student[] studentList = new Student[15];
		Instructor instructor = new Instructor();
		
		for(int i = 1; i <= numofStudents; i++)
		{
			Student student = new Student("Student ", i);
			studentList[i] = student;
		}
		
		instructor.start();
	}
	
	public static long sTime = System.currentTimeMillis();
}