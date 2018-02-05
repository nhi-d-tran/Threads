import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Student extends Thread
{
	private static AtomicBoolean wait = new AtomicBoolean(false);
	public static long time = System.currentTimeMillis();
	int timer;
	int shortTime;		
	int id;
	int attempts = 0;
	int group_size = 3;																//number of test taken
	int grade;
	int numOfExams = 3;
	int testTaken = 0;
	int examAttempts = 3;
	static int classCapacity = 8;
	static int currentClassSize = 0;
	String Sid;
	String Sname;
	String Status = "In School";
	String classAttended[] = {"Waiting", "Waiting", "Waiting"};
	boolean isWaiting = false;
	boolean reported = false;
	boolean inClass = false;
	static boolean isFull = false;
	Random rng = new Random();
	
	public Student(String name, int id)
	{
		/*
		 * Student constructor where the student name and ID is set
		 * initializes the random times
		 * starts the thread
		 */
		this.id = id;
		Sid = Integer.toString(id);
		Sname = name + Sid;
		setName(Sname);
		timer = rng.nextInt(5000);
		shortTime = rng.nextInt(1000);
		start();
	}
	
	public void msg(String m) 
	{
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": " + m);
	}
	
	private int RNGsus(int min, int max)
	{
		/*
		 * randomly generates a number for student report grades for each exam
		 * the number generated would be between the min and max numbers which are 10 and 100 respectively
		 * the min and max numbers are going to be specified when the method is called when the student threads are generating the report
		 */
		Random rng = new Random();
		grade = rng.nextInt((max - min) + 1) + min;
		
		return grade;
	}
	
	public void run()
	{
		while(true)	
		{
			msg(" has arrived");
			
			for(int i = 0; i < examAttempts; i++)
			{
				if(Instructor.examStarted[0] == "No")
				{
					try
					{
						msg("is waiting for Exam 1");
						Driver.lineOrgo.aquire();
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				else if(Instructor.examStarted[1] == "No")
				{
					try
					{
						msg("is waiting for Exam 2");
						Driver.lineCalculus.aquire();
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					try
					{
						msg("is waiting for Exam 3");
						Driver.lineTheory.aquire();
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
				
				if(currentClassSize >= classCapacity)
				{
					isFull = true;
					msg("has missed " + Instructor.exams[i] + " exam");
					classAttended[i] = "No";
					
					if(isFull == true)
					{
						if(Instructor.examStarted[1] == "No")
						{
							msg("Exam");
//								Driver.lineCalculus.aquire();
						}
						else
						{
							msg("Exam");
//								Driver.lineTheory.aquire();
						}
						
					}
				}
				else
				{
					msg("is in " + Instructor.exams[i]);
					currentClassSize++;
					inClass = true;
					
					try
					{
						if(currentClassSize == 8)
						{
							Driver.waitInstructor.release();
						}
						
						Driver.examWait.aquire();
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
										
					if(Instructor.examStarted[i] == "Finished" && inClass == true)
					{
						testTaken++;
						classAttended[i] = "Yes";
						
						msg("is now leaving the classroom");
					}
				}
				
				try
				{
					sleep(10000);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			if(testTaken == 2)
			{
				break;
			}
		}
//-------------------------------------------------------------------------------- Student Reports --------------------------------------------------------------------------------\\
		
		while(Instructor.examStatus[2] == "Finished" && Status == "Done" && reported == false)
		{
			try
	       	{
	       		sleep(5000);
	       	}
	       	catch(InterruptedException e)
	       	{
	       		e.printStackTrace();
	       	}
					
			while(!wait.compareAndSet(false, true))
			{
				try
				{
					yield();
					sleep(1000);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
					
			System.out.println("\n" + currentThread().getName() + "'s Report\n");
					
			for(int i = 0; i < 3; i++)
			{
				for(int j = 0; j < 3; j++)
				{
					if(i == j)
					{
						if(classAttended[j] == "No")
						{
							System.out.println(Instructor.exams[i] + ": " + 0);
						}
						else
						{
							System.out.println(Instructor.exams[i] + ": " + RNGsus(10, 100));
						}
					}
				}
			}
					
			reported = true;
			wait.set(false);
					
			System.out.println("Total Number of Exams Taken: " + testTaken);
		}
	}
}