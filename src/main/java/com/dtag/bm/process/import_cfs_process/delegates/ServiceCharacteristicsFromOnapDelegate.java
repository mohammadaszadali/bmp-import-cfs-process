package com.dtag.bm.process.import_cfs_process.delegates;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.dtag.bm.process.import_cfs_process.RestClientAdaptor;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


@Component("serviceCharacteristicsFromOnapDelegate")
public class ServiceCharacteristicsFromOnapDelegate implements JavaDelegate{
	
	@Autowired
	RestClientAdaptor restClientAdaptor;
	
	@Value("${onapServiceCharactRequestUrl}")
	private String onapServiceCharactRequestUrl;
	
	private final Logger logger = LoggerFactory.getLogger(ServiceCharacteristicsFromOnapDelegate.class);
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		logger.info(" -------------ONAP Rest API for getting Service Characteristics started...------------");
		JsonParser parser = new JsonParser();
		try {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);				
		logger.info("Import CFS Event Payload in JSON format Captured :  " + execution.getVariable("payload"));
		String onapServiceCharactRequestIdPayload=(String) execution.getVariable("payload");
		JsonObject jsonElement =(JsonObject)parser.parse(onapServiceCharactRequestIdPayload);
		
		logger.info("Import CFS Event Id Captured :  " + jsonElement.get("onapServiceCharactRequestId").getAsString());
		
		
		StringBuffer urlBuffer = new StringBuffer(onapServiceCharactRequestUrl);
		urlBuffer.append(jsonElement.get("onapServiceCharactRequestId").getAsString()).append("/specificationInputSchema");
		logger.info("ONAP Rest API for getting Service Characteristics URL :  " + onapServiceCharactRequestUrl);
		ResponseEntity<String> onapServiceCharactResponse = restClientAdaptor.callGetRestApi(urlBuffer.toString(), String.class);
		 JsonValue onapServiceCharactResponseJsonValue = SpinValues.jsonValue(onapServiceCharactResponse.getBody()).create();
		 logger.info("onapServiceCharactResponse: "+onapServiceCharactResponseJsonValue.getValue().toString());
			logger.info("-------------ONAP Rest API for getting Service Characteristics completed for service ID: "+jsonElement.get("onapServiceCharactRequestId").getAsString()+"...--------------");
		execution.setVariable("onapServiceCharactResponse", onapServiceCharactResponseJsonValue);
		execution.setVariable("serviceReqId", jsonElement.get("onapServiceCharactRequestId").getAsString());
	
		}catch(JsonSyntaxException syntaxException) {
			logger.error("Error Occoured: "+syntaxException.getMessage());
			throw new BpmnError("Error_Invaild_paylod");
		}
		catch(Exception ex) {
			logger.error("Error Occoured in getting Service Characteristics from ONAP for service request ID: "+execution.getVariable("serviceReqId"));
			ex.printStackTrace();
			throw new BpmnError("Error_Failed_To_Connect");
		}
	}

}
