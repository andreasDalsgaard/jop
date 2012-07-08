
package pmFFTcpResult;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;

import javax.safetycritical.JopSystem;
import javax.safetycritical.Safelet;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

import pmFFTcpResult.app.MainMission;
import pmFFTcpResult.app.AppMainMissionSequencer;
import pmFFTcpResult.app.AppPeriodicEventHandler;



public class pmFFTcpResult implements Safelet {
	
	public void setUp() {}
	
    public void tearDown() {}

    public MissionSequencer getSequencer() {
    	StorageParameters sp =
            new StorageParameters(100000L, null);
        return new AppMainMissionSequencer(new PriorityParameters(10), sp);
    }

	public static void main(String[] args) {
		JopSystem.startMission(new pmFFTcpResult());		
	}

	@Override
	public long immortalMemorySize() {
		// TODO Auto-generated method stub
		return 0;
	}

}


