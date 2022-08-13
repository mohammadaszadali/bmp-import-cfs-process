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



@Component("transformServiceSpecificationDelegate")
public class TransformServiceSpecificationDelegate implements JavaDelegate{

	@Autowired
	RestClientAdaptor restClientAdaptor;
	
	@Value("${transformationServiceSpecificationUrl}")
	private String transformationServiceSpecificationUrl;
	
	private final Logger logger = LoggerFactory.getLogger(TransformServiceSpecificationDelegate.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
	logger.info("----------transformation for ONAP service specification for request ID: "+execution.getVariable("serviceReqId")+" started...-------------");
	try {
		JsonParser jsonParser = new JsonParser();
		Object onapServiceSpecificationResponse=execution.getVariableTyped("onapServiceSpecificationResponse",false).getValue();
		Object masterAttributeBmResponse=execution.getVariableTyped("masterAttributeBmResponse",false).getValue();
		Object onapServiceCharactResponse=execution.getVariableTyped("onapServiceCharactResponse",false).getValue();
		JsonElement onapServiceSpecificationResponseJson = jsonParser.parse(onapServiceSpecificationResponse.toString());
		 JsonElement masterAttributeBmResponseJson = jsonParser.parse(masterAttributeBmResponse.toString());
		
		 logger.info("Transformation of Service Charact Rest API  URL :  " +transformationServiceSpecificationUrl);
		
		 JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("BmMasterAttribute", masterAttributeBmResponseJson.toString());
		jsonObject.addProperty("OnapServiceSpecification", onapServiceSpecificationResponseJson.toString());
		jsonObject.addProperty("OnapServiceCharacteristics", onapServiceCharactResponse.toString());
		logger.info("combination done");
		logger.info("Transformation of Service Charact via  Captured input :  " +jsonObject.toString());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);				
		HttpEntity<Object> entity = new HttpEntity<>(jsonObject.toString(), headers);

		ResponseEntity<String> response = restClientAdaptor.callPostRestApi(transformationServiceSpecificationUrl, entity,String.class);
		JsonValue responseJsonValue = SpinValues.jsonValue(response.getBody()).create();
		logger.info("Transformation of Service Specification Response : "+responseJsonValue.getValue().toString());
		logger.info("----------transformation for ONAP service specification completed...-------------");
		execution.setVariable("transformationServiceSpecificationResponse", responseJsonValue);
		
		}catch(Exception ex) {
			logger.error("error occoured:"+ex.getMessage());
			ex.printStackTrace();
			throw new BpmnError("Error_Failed_To_Connect_Rollback");
		}
		
	}

}
