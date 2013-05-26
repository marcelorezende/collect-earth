package org.openforis.eye.springversion;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public abstract class JsonPocessorServlet extends DataAccessingServlet {


	protected Map<String, String> extractRequestData(HttpServletRequest request) {
		Map<String, String> collectedData = new HashMap<String, String>();

		@SuppressWarnings("rawtypes")
		Enumeration enParams = request.getParameterNames();
		while (enParams.hasMoreElements()) {
			String paramName = (String) enParams.nextElement();
			collectedData.put(paramName, request.getParameter(paramName).toString());
		}
		return collectedData;
	}

	protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;

	protected void setJsonResponse(HttpServletResponse response, Map<String, String> collectedData) throws IOException {
		if (collectedData != null && collectedData.size() > 0) {
			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(collectedData);

			setResponseHeaders(response);
			PrintWriter out = response.getWriter();

			out.println(json);

			out.close();
		}
	}

	private void setResponseHeaders(HttpServletResponse response) {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("Cache-control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Max-Age", "86400");
	}

	protected void setResult(boolean success, String message, Map<String, String> collectedData) {
		collectedData.put("type", success ? "success" : "error"); // success,error,warning
		collectedData.put("message", "The data has been saved");
	}

}