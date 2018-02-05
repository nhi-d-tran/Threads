import java.util.Random;

public class Instructor extends Thread
{
	public static long time = System.currentTimeMillis();
	int timer;
	int instructorSleep = 20000;
	static int examDuration = 10000;
	static int examTimes [] = {45000, 60000, 75000};
	static String exams[] = {"Exam 1", "Exam 2", "Exam 3"};
	static String examStatus[] = {"Hasn't started", "Hasn't started", "Hasn't started"};
	static String examStarted[] = {"No", "No", "No"};
	Random rng = new Random();
	public static boolean Arrived = false;
	static boolean end = false;
	boolean eMessage[] = {false, false, false};
	
	public void instructor()
	{
		timer = rng.nextInt(5000);
		Thread.currentThread().setName("Instructor");
	}
	
	public void msg(String m) 
	{
		System.out.println("\n[" + (System.currentTimeMillis()-time) + "] " + getName() + ": " + m + "\n");
	}
	
	public long Time()
	{
		return System.currentTimeMillis() - Driver.sTime;
	}
	
	public void run()
	{
		while(examStatus[exams.length-1] != "Finished")
		{
			for(int i = 0; i < exams.length; i++)
			{
				while(examStatus[i] != "Finished")
				{
					if(examStarted[i] == "No" && eMessage[i] == false)
					{
						msg("Instructor has arrived for " + exams[i]);
						Arrived = true;
						eMessage[i] = true;
						
						try 
						{
							for(int j = 0; j < Driver.numofStudents; j++)
							{
								sleep(1000);
								
								if(Instructor.examStarted[0] == "No")
								{
										Driver.lineOrgo.release();
								}
								else if(Instructor.examStarted[1] == "No")
								{
										Driver.lineCalculus.release();
								}
								else
								{
										Driver.lineTheory.release();
								}
							}
							
							sleep(timer);
							
							Driver.waitInstructor.aquire();
						} 
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
					}
	
					
					if(Student.isFull == true)
					{
						if(examStarted[i] != "Finished")
						{
							msg("Instructor started " + exams[i]);
							examStarted[i] = "Yes";
						}
						
						try 
						{
							sleep(examDuration);
							examStarted[i] = "Finished";
							msg(exams[i] + " has ended");
							examStatus[i] = "Finished";
							Arrived = false;
							
							for(int j = 0; j < Student.classCapacity; j++)
							{
								Driver.examWait.release();
							}
							
						} 
						catch(InterruptedException e) 
						{
							e.printStackTrace();
						}
						
					}
				}
				
				try 
				{
					msg("Instructor is taking a break\n");
					Student.currentClassSize = 0;
					sleep(instructorSleep);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		if(examStatus[2] == "Finished")
		{
			msg("All 3 exams are finished\n");
		}
	}
}