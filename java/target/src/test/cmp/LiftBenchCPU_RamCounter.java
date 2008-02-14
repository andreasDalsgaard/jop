package cmp;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;
import jbe.LowLevel;
import jbe.BenchMark;
import jbe.BenchLift;


// Counts the memory accesses of a benchmark to the external RAM 
// This program can be used when some comments of the dspio
// project are removed. (jopcyc12.vhd, sc_sys.vhd, scio_dspio.vhd)
// Don't forget to remove the comment in Const.java

public class LiftBenchCPU_RamCounter {

	public static int lift0 = 0;
	public static BenchMark bm0;
	public static int cnt = 16384;

	public static void main(String[] args) {

		int count0 = 0;
		int count1 = 0;
		int us0 = 0;
		int us1 = 0;
		int count_result = 0;
		int us_result = 0;
		
		bm0 = new BenchLift();
		System.out.println("Bandwidth:");
		
		// Startpoint of measuring
		count0 = Native.rdMem(Const.IO_RAMCNT);
		us0 = Native.rdMem(Const.IO_CNT); // Clockcycles
		
		bm0.test(cnt);
		
		// Endpoint of measuring
		us1 = Native.rdMem(Const.IO_CNT); // Clockcycles
		count1 = Native.rdMem(Const.IO_RAMCNT);
		
		count_result = count1 - count0;
		us_result = us1 - us0;
			
		LowLevel.msg("RAM Accesses:", count_result);
		LowLevel.lf();
		LowLevel.msg("Time us:", us_result);
		LowLevel.lf();
		LowLevel.msg("in %:", count_result/(us_result/100));
		LowLevel.lf();
	}		
}