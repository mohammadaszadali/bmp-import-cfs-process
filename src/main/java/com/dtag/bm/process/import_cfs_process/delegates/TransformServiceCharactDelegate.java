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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.dtag.bm.process.import_cfs_process.RestClientAdaptor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



@Component("transformServiceCharactDelegate")
public class TransformServiceCharactDelegate implements JavaDelegate{


	@Autowired
	RestClientAdaptor restClientAdaptor;
	
	@Value("${transformationServiceCharactUrl}")
	private String transformationServiceCharactUrl;
	
	private final Logger logger = LoggerFactory.getLogger(TransformServiceCharactDelegate.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		JsonParser jsonParser = new JsonParser();
		try {
			logger.info("---------Transformation of Service Characteristics started for service ID: "+execution.getVariable("serviceReqId")+"...---------");
			 Object onapServiceCharactResponse = execution.getVariableTyped("onapServiceCharactResponse",false).getValue();
			
			 JsonElement onapServiceCharactResponseJson = jsonParser.parse(onapServiceCharactResponse.toString());
		logger.info("Transformation of Service Charact Rest API  URL :  " + transformationServiceCharactUrl);
		logger.info("Transformation of Service Characteristics via captured input: "+onapServiceCharactResponseJson.getAsJsonObject().toString());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);		
		JsonObject onapServiceCharactResponseJsonObject=onapServiceCharactResponseJson.getAsJsonObject();
		 String serviceReqId=(String) execution.getVariable("serviceReqId");
		onapServiceCharactResponseJsonObject.addProperty("onapServiceCharactRequestId", serviceReqId.toString());
		
		logger.info("Transformation of Service Characteristics via modified input: "+onapServiceCharactResponseJsonObject.getAsJsonObject().toString());
		HttpEntity<Object> entity = new HttpEntity<>(onapServiceCharactResponseJsonObject.toString(), headers);

		ResponseEntity<String> response = restClientAdaptor.callPostRestApi(transformationServiceCharactUrl, entity,String.class);
		
		  JsonValue transformationServiceCharactResponseJsonValue = SpinValues.jsonValue(response.getBody()).create();
		  logger.info("Transformation of Service Characteristics Response : "+transformationServiceCharactResponseJsonValue.getValue().toString());
		  logger.info("---------Transformation of Service Characteristics for service ID: "+serviceReqId+"completed...---------"); 
		execution.setVariable("masterAttributeBmResponse", transformationServiceCharactResponseJsonValue);
		
		
		}catch(Exception ex) {
			logger.error("Error Occoured: "+ex.getMessage());
			ex.printStackTrace();
			throw new BpmnError("Error_Failed_To_Connect");
		}
		
	}

}