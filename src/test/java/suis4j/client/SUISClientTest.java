package suis4j.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


// public class SUISClien

public class SUISClientTes
    extends TestCase
{
    public SUISClientTest( String testName )
    {
        super( testName );
    }


    public static Test suite()
    {
        return new TestSuite( SUISClientTest.class );
    }

    public void testSUISClient()
    {
        assertTrue( true );
        //assertTrue( false );
    }
}
