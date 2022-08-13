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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component("serviceSpecificationCharactWithBmDelegate")
public class ServiceSpecificationCharactWithBmDelegate implements JavaDelegate{

	@Autowired
	RestClientAdaptor restClientAdaptor;
	
	@Value("${bmServiceSpecificationCharactUrl}")
	private String bmServiceSpecificationCharactUrl;
	
	private final Logger logger = LoggerFactory.getLogger(ServiceSpecificationCharactWithBmDelegate.class);
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		   JsonObject serviceSpecificationCharat= new JsonObject();
		try {
			JsonParser jsonParser = new JsonParser();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);				
			logger.info("---------creating Service Specification characteristics With Bm core started for service request ID: "+execution.getVariable("serviceReqId")+"...----------");
			
			Object bmServiceSpecificationResponse= execution.getVariableTyped("bmServiceSpecificationResponse",false).getValue();
			 JsonElement bmServiceSpecificationResponseJson = jsonParser.parse(bmServiceSpecificationResponse.toString());
			
			 JsonArray list = new JsonArray();
             JsonObject servicespecificationcharacteristicsvalue =bmServiceSpecificationResponseJson.getAsJsonObject().get("servicespecification").getAsJsonArray().get(0).getAsJsonObject();
             JsonObject customerProperty = servicespecificationcharacteristicsvalue.get("servicespecattributegroups").getAsJsonArray().get(0).getAsJsonObject();
             JsonArray jsonArray= customerProperty.get("servicespecattributevalue").getAsJsonArray();
             for (JsonElement element : jsonArray) {
            	 JsonObject jsonObject = new JsonObject();
            	 JsonElement serviceAttributeServiceAttributeCode=element.getAsJsonObject().get("serviceAttributeServiceAttributeCode");
            	 JsonElement serviceSpecAttributeValue = element.getAsJsonObject().get("serviceSpecAttributeValue");
            	 jsonObject.addProperty("serviceAttributeServiceAttributeCode",serviceAttributeServiceAttributeCode.getAsString());
            	 jsonObject.addProperty("value", serviceSpecAttributeValue.getAsString());
            	 jsonObject.addProperty("valueTo", "");
            	 list.add(jsonObject);
             }
             String speccode =servicespecificationcharacteristicsvalue.get("serviceSpecID").getAsString();
             String serviceSpecCharName=servicespecificationcharacteristicsvalue.get("serviceSpecName").getAsString();
             
             logger.info("bmServiceSpecificationResponse received: "+bmServiceSpecificationResponse.toString());
          
             serviceSpecificationCharat.addProperty("serviceSpecCharName", serviceSpecCharName); 
             serviceSpecificationCharat.add("servicespecificationcharacteristicsvalue",list);

             JsonValue serviceSpecificationCharatJsonValue = SpinValues.jsonValue(serviceSpecificationCharat.toString()).create();
			 execution.setVariable("serviceSpecificationCharatPayload", serviceSpecificationCharatJsonValue);
			logger.info("created BM  Service Specification Characteristics request payload"+serviceSpecificationCharat.toString());
			StringBuffer urlBuffer = new StringBuffer(bmServiceSpecificationCharactUrl);
			urlBuffer.append(speccode);
			
			logger.info("BM Rest API for creating Service Specification characteristics With Bm URL :  " + urlBuffer.toString());
			
			HttpEntity<Object> entity = new HttpEntity<>(serviceSpecificationCharat.toString(), headers);

			ResponseEntity<String> response = restClientAdaptor.callPostRestApi(urlBuffer.toString(), entity,String.class);
			logger.info("creating BM  Service Specification Characteristics Response : "+response.getBody());
			logger.info("---------creating Service Specification characteristics With Bm core started...----------");
			
			
			}catch(Exception ex) {
				logger.error("Error Occoured in Service Specification characteristics With Bm core for service request ID: "+execution.getVariable("serviceReqId")+" for input request: "+serviceSpecificationCharat.toString());
				ex.printStackTrace();
				throw new BpmnError("Error_Failed_To_Connect_Rollback");
			}
		
	}

}
