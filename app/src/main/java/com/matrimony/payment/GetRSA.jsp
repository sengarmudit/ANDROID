<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="javax.net.ssl.HttpsURLConnection"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="java.util.Enumeration"%>
<%
	StringBuffer vRequest = new StringBuffer("");
	Enumeration vReqParam = request.getParameterNames();
	while (vReqParam.hasMoreElements()) {
		String vParamName = (String)vReqParam.nextElement();
		vRequest.append(vParamName+"="+request.getParameter(vParamName)+"&");
	}
	URL	vUrl = null;
	URLConnection vHttpUrlConnection = null;
	DataOutputStream vPrintout = null;
	DataInputStream	vInput = null;
	StringBuffer vStringBuffer = null;
	try{
		System.setProperty("https.protocols", "TLSv1");
		vUrl = new URL("https://secure.ccavenue.com/transaction/getRSAKey");
		if(vUrl.openConnection() instanceof HttpsURLConnection){
			vHttpUrlConnection = (HttpsURLConnection)vUrl.openConnection();
		}else if(vUrl.openConnection() instanceof com.sun.net.ssl.HttpsURLConnection){
			vHttpUrlConnection = (com.sun.net.ssl.HttpsURLConnection)vUrl.openConnection();
		}else{
			vHttpUrlConnection = (URLConnection)vUrl.openConnection();
		}
		vHttpUrlConnection.setDoInput(true);
		vHttpUrlConnection.setDoOutput(true);
		vHttpUrlConnection.setUseCaches(false);
		vHttpUrlConnection.connect();
		vPrintout = new DataOutputStream (vHttpUrlConnection.getOutputStream());
		vPrintout.writeBytes(vRequest.toString());
		vPrintout.flush();
		vPrintout.close();
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(vHttpUrlConnection.getInputStream()));
		vStringBuffer = new StringBuffer();
		String vRespData;
		while((vRespData = bufferedreader.readLine()) != null)
			if(vRespData.length() != 0)
				vStringBuffer.append(vRespData.trim()+"\n");
		bufferedreader.close();
		bufferedreader = null;
	}catch(Exception e){
		System.out.println("Bank gateway down.");
	}finally {  
		if (vInput != null)
			vInput.close();  
		if (vHttpUrlConnection != null)  
			vHttpUrlConnection = null;
	}
	response.setContentType("text/html");
	out.print(vStringBuffer.substring(0,vStringBuffer.length()-1));
%>
