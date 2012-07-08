package pmFFTcpResult.app;

import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;

import javax.safetycritical.ManagedMemory;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import pmFFTcpResult.app.Complex;

public class AppPeriodicEventHandler extends PeriodicEventHandler
{
	ManagedMemory phpm;
	Complex[] input;
	Complex[] result;
	Complex[] myResult;
	
	public AppPeriodicEventHandler(PriorityParameters priority, 
	                                        PeriodicParameters parameters,
	                                        StorageParameters storage,
	                                        int memorySize	                                        
	                                        )
	{
		super(priority, parameters, storage, memorySize);		
		Complex a = new Complex(5.0, 6.0);
		this.input = new Complex[] { a, a, a, a, a, a, a, a};
	}

	public void handleAsyncEvent() 
	{		
		
		ManagedMemory mm = ManagedMemory.getCurrentManagedMemory();				
		mm.enterPrivateMemory(20, new FooRunnable(mm, this, this.input));
		
		
		System.out.print(this.result);

	}
	
	public void saveResult(Complex[] produced)
	{
		System.arraycopy(produced, 0, this.result, 0, produced.length);

	}
}

class FooRunnable implements Runnable{
	
	Complex[] input;
	ManagedMemory pepm;
	PeriodicEventHandler peh;
	
	public FooRunnable(ManagedMemory pepm, PeriodicEventHandler peh, Complex[] input)
	{
		this.input = input;
		this.pepm = pepm;
		this.peh = peh;
	}
	
    public void run() {
    	Complex[] result = FFT.fft(input);

    	pepm.executeInArea(new CopyToMission2(result, this.peh));
    }
}

class CopyToMission2 implements Runnable{

	Complex[] result;
	PeriodicEventHandler peh;
	
	public CopyToMission2(Complex[] result, PeriodicEventHandler peh)
	{
		this.result = result;
		this.peh = peh;
	}
	
	public void run() {
		((AppPeriodicEventHandler)this.peh).saveResult(result);		
	}
	
}
