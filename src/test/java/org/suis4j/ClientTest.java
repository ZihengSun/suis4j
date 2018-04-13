package org.suis4j;

import java.util.*;

import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


public class ClientTest
{

	@Test
	public void testCreateClient()
	{
		Client restClient = new Client(ServiceType.REST);
		Client soapClient = new Client(ServiceType.SOAP);
		Client wmsClient = new Client(ServiceType.OGC_WMS_130);
		
		assertTrue(true);
	}

	@Test
	public void testClientStub()
	{
		Client client = new Client(ServiceType.REST);

		// bind remote service schema
		client.bindURL("https://service.iris.edu/irisws/timeseries/1/application.wadl");

		// schema from string
		client.bindSchema("<schema>...</schema>");

		// operations
		List<Operation> operations = client.getOperations();
		List<String> operationName = client.getOperationNames();
		Operation operation = client.getOperation("subsetImage");
	}
}
