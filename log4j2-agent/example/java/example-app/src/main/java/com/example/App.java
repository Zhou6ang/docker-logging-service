package com.example;

import com.nokia.oss.logging.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
	public static final Logger log = Logger.getLogger(App.class);
    public static void main( String[] args )
    {
    	log.debug("App launching.");
    	log.info("App logic handling.");
        System.out.println( "Hello World!" );
        log.debug("App stalling.");
    }
}
