package com.cfx.restapi;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cfx.utils.CustomHttpClientException;
import com.cfx.utils.JSONUtils;
import com.cfx.utils.ServerConnector;
import com.cfx.utils.ServiceConfig;
import com.cfx.utils.ServiceExecutionException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.research.ws.wadl.HTTPMethods;


@Path("/services")
public class ServicesAPI {
	
	private final static Logger logger = LoggerFactory.getLogger(ServicesAPI.class);
	
	private String token = null;
	
	public String getToken() throws CustomHttpClientException, IOException {
		String user = ServiceConfig.getInstance().getUserid();
		String password = ServiceConfig.getInstance().getPassword();
		JsonObject loginDetails = new JsonObject();
		loginDetails.addProperty("user", user);
		loginDetails.addProperty("password", password);
		String response = ServerConnector.getInstance().gatewayLogin(loginDetails);
		JsonObject loginResponse = JSONUtils.getJsonObjectByString(response);
		token = loginResponse.get("apiGatewaySessionId").getAsString();
		return token;
	}
	
	@GET
	@Path("/office")
	public Response getOffice(@QueryParam("officeName") String officeName) throws CustomHttpClientException, IOException {
        String methodName = "getOffice";
        String paramsStr = String.format(" { %s : [ \"%s\" ] } ", "params", officeName);
        
        return invokeAPI(methodName, paramsStr);
	}
	
	@GET
	@Path("/offices")
	public Response getOffices(@QueryParam("zipCode") String zipCode) throws CustomHttpClientException, IOException {
        String methodName = "getOffices";
        String paramsStr = String.format(" { %s : [ \"%s\" ] } ", "params", zipCode);
        
        return invokeAPI(methodName, paramsStr);
	}

	private Response invokeAPI(String methodName, String paramsStr) {
		String serviceName = ServiceConfig.getInstance().getServiceName();
        String serviceNamespace = ServiceConfig.getInstance().getServiceNamespace();
        String serviceVersion = ServiceConfig.getInstance().getServiceVersion();
        String url = "/service/" + serviceNamespace + "/" + serviceName + "/" + serviceVersion + "/" + methodName;
        
        try {
            String response = ServerConnector.getInstance().execute(url, HTTPMethods.POST, paramsStr, false, getToken());
            JsonElement o = JSONUtils.getJsonElementByString(String.valueOf(response));
            return Response.ok(JSONUtils.jsonize(o)).build();
        } catch (ServiceExecutionException e) {
            JsonObject responseObj = new JsonObject();
            JsonElement o = JSONUtils.getJsonElementByString(String.valueOf(e.getMessage()));
            responseObj.add("serviceError", o);
            return Response.ok(JSONUtils.jsonize(responseObj)).build();
        } catch(Exception e){
            String errorMessage = "serviceError : " + e.getMessage();
            JsonObject responseObj = new JsonObject();
            JsonElement o = JSONUtils.getJsonElementByString(String.valueOf(errorMessage));
            responseObj.add("serviceError", o);
            return Response.ok(JSONUtils.jsonize(responseObj)).build();
        }
	}
}
