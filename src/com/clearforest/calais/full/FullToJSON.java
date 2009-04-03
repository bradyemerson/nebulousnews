/*
 * Copyright (c) 2008, ClearForest Ltd.
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * 		- 	Redistributions of source code must retain the above 
 * 			copyright notice, this list of conditions and the 
 * 			following disclaimer.
 * 
 * 		- 	Redistributions in binary form must reproduce the above 
 * 			copyright notice, this list of conditions and the 
 * 			following disclaimer in the documentation and/or other 
 * 			materials provided with the distribution. 
 * 
 * 		- 	Neither the name of ClearForest Ltd. nor the names of 
 * 			its contributors may be used to endorse or promote 
 * 			products derived from this software without specific prior 
 * 			written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.clearforest.calais.full;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clearforest.calais.common.JSONUtils;


/**
 * Servlet implementation class for Servlet: FullToJSON
 *
 */
 public class FullToJSON extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	 
	private static final long serialVersionUID = 1L;
	
    /* 
	 * Constructor
	 */
	public FullToJSON() {
		super();
		
	}   	 	
	
	/* 
	 * Process HTTP POST request
	 * 
	 * This is the entry point of the Servlet. The content to be analyzed
	 * is provided in the 'content' parameter
	 * 
	 * The printable parameter is optional and defaults to 
	 * false. If set to true the resulting json is printable
	 * within an HTML page (indentation etc.)
	 * 
	 * The returned response includes the Full Format JSON
	 * or an error string
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		CalaisJSONIf 	jsonIf		= null;
		String 			result 		= null;
		boolean 		printable	= false;

		/*
		 * A new instance for each POST to avoid synchronization
		 * issues
		 */
		ServletConfig conf = getServletConfig();
		jsonIf = new CalaisJSONIf(
				conf.getInitParameter("fulljson_API_KEY"),
				conf.getInitParameter("fulljson_InvokeEnlightenTimeout"),
				conf.getInitParameter("fulljson_OverrideDefaultURL"),
				conf.getInitParameter("fulljson_CalaisWebServiceURL"),
				conf.getInitParameter("fulljson_VerifySSLCertificates")
				);
		
		/*
		 * Response type is JSON by default
		 */
		response.setContentType("application/json");

		/*
		 * Retrieve the content from request
		 */
		String content = request.getParameter("content");
		String printableStr = request.getParameter("printable");
		if (printableStr != null && printableStr.equals("true"))
		{
			printable = true;
		}

		/*
		 * Call the JSON interface class 
		 */
		result = jsonIf.getFullJSON(content);


		if (jsonIf.isLastErr())
		{
			/*
			 * An error occurred - return an object with a 
			 * single string called error
			 */
			getServletContext().log("ERROR retrieving Full JSON Format: " + jsonIf.getLastErr());
			result = "{ \"error\":\"" + 
				JSONUtils.escapeForJSON(jsonIf.getLastErr()) + 
				"\" }";
			if (printable)
			{
				response.setContentType("text/html");
				result = JSONUtils.convertJSONToPrintableHTML(result);
			}
			response.getWriter().print(result);
		}
		else
		{
			/*
			 * Successful operation - return the JSON (in
			 * printable format if needed)
			 */
			if (printable)
			{
				response.setContentType("text/html");
				result = JSONUtils.convertJSONToPrintableHTML(result);
				result = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" + 
					"<html><head><title>Calais Full JSON Format</title></head><body>" + 
						result + "</body></html>";
			}
			response.getWriter().print(result);
		}
		
	}   	  	
	
	/* 
	 * Return information about the Servlet
	 */
	public String getServletInfo() {
		return "Servlet allowing retrieval of semantic " +
		"data from the Calais Web service in full RDF " +
		"converted into simplified JSON format (by ClearForest Ltd.)";
	}     
}