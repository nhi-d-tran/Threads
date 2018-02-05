import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Semaphore extends Object implements Serializable
{
	private static AtomicBoolean wait = new AtomicBoolean(false);
	int permit;
	int counter = 0;
	Queue<Student> studentQueue = new LinkedList<Student>();
	
	public Semaphore(int permits)
	{
		permit = permits;
	}
	
	public void aquire() throws InterruptedException
	{
		permit--;
		
		if(permit < 0)
		{
			
			while(!wait.compareAndSet(false, true))	
			{
				/*
				 * busy wait loop
				 * thread can only leave once the first thread sets the wait value to false again
				 * while inside the thread yields then sleeps
				 */
				try
				{
					Student.yield();
					Student.sleep(1000);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void release()
	{
		permit ++;
		
		if(permit <= 0)
		{
			wait.set(false);
		}
	}
}