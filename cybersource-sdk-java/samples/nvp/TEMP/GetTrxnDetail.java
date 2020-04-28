/*
 * Mark Johnson
 * Visa
 * 2013
 */
import java.net.*;
import java.io.*;
import java.security.Security;

public class GetTrxnDetail {

	private static String server = "https://ebc.cybersource.com/ebc/Query"; // for test system, use https://ebctest.cybersource.com/ebctest/Query
	private static String merchantID = "gptw15877002600";
	private static String username = "jasontest";
	private static String passwd = "Qazwsx123";

	public static void main(String[] args) {
		// Collect the request id via the command line
		String requestID = "";
		try{
			if(args[0] != null || args[0] != ""){
				requestID = args[0];
				// call GetTrxnRecord with the requestID as the argument
				GetTrxnRecord(requestID);
			}
		}catch(Exception e){
			System.err.println("Expected usage:\nGetTrxnDetail <requestID>\nWhere <requestID> is a 22 digit numeric ID corresponding to a transaction in the CyberSource system");
		}
	}
	private static void GetTrxnRecord(String requestID){
	/*
	 * This method takes a CyberSource request id as input, opens an https connection to CyberSource and posts the request id.
	 * The reply is in the form of an XML Transaction Detail Report record
	 */
	 
		try {
			// add SSL provider to the java policy file
			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

			// Construct post data
			String data = "merchantID=" + merchantID;
			data += "&type=transaction";
			//data += "&versionNumber=1.9"; // If the versionNumber is not submitted, the latest version of the DTD will be used
			data += "&subtype=transactionDetail";
			data += "&requestID=" + requestID;

			// Encode login info
			String credString = username + ":" + passwd;
			String encoding = new sun.misc.BASE64Encoder().encode(credString.getBytes());

			// Send post data
			URL url = new URL(server);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			// Handle basic authentication
			conn.setRequestProperty("Authorization","Basic " + encoding);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuffer result = new StringBuffer();
			int c;
			String doc;
			while ((c = rd.read()) != -1) {
				result.append((char) c);
			}
			doc = result.toString();
			System.out.println(doc);
			wr.close();
			rd.close();
		} catch(IOException e) {
			System.err.println(e.getMessage() + "\n" + e.getStackTrace());
		} catch(Exception e) {
			System.err.println(e.getMessage() + "\n" + e.getStackTrace());
		} finally {
		}

	}

}
