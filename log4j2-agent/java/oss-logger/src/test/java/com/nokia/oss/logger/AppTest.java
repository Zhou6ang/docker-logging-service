package com.nokia.oss.logger;

import java.util.List;
import java.util.stream.LongStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	Logger log = LogManager.getLogger(AppTest.class);
        LongStream.range(1, 100).forEach(e->{
			List l = null;
			try {
				l.add("");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
//				e1.printStackTrace();
				log.info("error:",e1);
			}
			
		});
    }
}
