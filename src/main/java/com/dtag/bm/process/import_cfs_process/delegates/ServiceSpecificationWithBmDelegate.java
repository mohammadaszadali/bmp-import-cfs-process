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

@Component("serviceSpecificationWithBmDelegate")
public class ServiceSpecificationWithBmDelegate implements JavaDelegate{

	@Autowired
	RestClientAdaptor restClientAdaptor;
	
	@Value("${bmServiceSpecificationUrl}")
	private String bmServiceSpecificationUrl;
	
	private final Logger logger = LoggerFactory.getLogger(ServiceSpecificationWithBmDelegate.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		logger.info("--------------creating Service Specification With Bm core started for request ID: "+execution.getVariable("serviceReqId")+"..--------------");
		Object transformationServiceSpecificationResponse=null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);				
			
			transformationServiceSpecificationResponse=execution.getVariableTyped("transformationServiceSpecificationResponse",false).getValue();
			
			logger.info("BM Rest API for creating Service Specification URL :  " + bmServiceSpecificationUrl);
			
		   logger.info("crating Service specification via request: "+transformationServiceSpecificationResponse.toString());
				
			HttpEntity<Object> entity = new HttpEntity<>(transformationServiceSpecificationResponse.toString(), headers);

			ResponseEntity<String> response = restClientAdaptor.callPostRestApi(bmServiceSpecificationUrl, entity,String.class);
			
			JsonValue responseJsonValue = SpinValues.jsonValue(response.getBody()).create();
			logger.info("BM  Service Specification Response : "+responseJsonValue.getValue().toString());
			logger.info("--------------creating Service Specification With Bm core completed..--------------");
			execution.setVariable("bmServiceSpecificationResponse", responseJsonValue);
			}catch(Exception ex) {
				logger.error("Error Occoured in creating Service Specification With Bm core for service request ID: "+execution.getVariable("serviceReqId")+" for input request: "+transformationServiceSpecificationResponse.toString());
				ex.printStackTrace();
				throw new BpmnError("Error_Failed_To_Connect_Rollback");
			}
		
	}

}
