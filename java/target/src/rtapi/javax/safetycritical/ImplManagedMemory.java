package javax.safetycritical;

import javax.safetycritical.annotate.Allocate;
import javax.safetycritical.annotate.MemoryAreaEncloses;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Memory;
import javax.realtime.InaccessibleAreaException;

/**
 * This class is used to add support for getCurrentManagedMemory and
 * executeInArea for MangedMemory.
 */
@SCJAllowed
public class ImplManagedMemory extends ManagedMemory {

	protected Memory scope;

	public ImplManagedMemory(Memory m) {
		super(0);
		this.scope = m;
	}

	@Override
	@Allocate(sameAreaAs = { "object" })
	@MemoryAreaEncloses(inner = { "logic" }, outer = { "this" })
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)	
	public void executeInArea(Runnable logic) throws InaccessibleAreaException {		
		scope.enter(logic); 
	}

}
