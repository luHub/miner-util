package methodBugLevelStudyTest;

import core.MethodBugLevelCollector;
import org.junit.Before;
import org.junit.Test;
import utils.GitReader;

import java.io.IOException;

/**
 * Created by mey on 10/29/2016.
 * Functional Test
 */


public class MethodBugLevelStudyTest {



    @Before public void initialize() {
        
    }

    @Test
    public void populateMetrics() throws IOException, InterruptedException {
    	 MethodBugLevelCollector methodBugLevelCollector = new MethodBugLevelCollector();
        methodBugLevelCollector.populateMetrics();
    }
    
    @Test
    public void rapidMinerTest(){
    	
    }

}
