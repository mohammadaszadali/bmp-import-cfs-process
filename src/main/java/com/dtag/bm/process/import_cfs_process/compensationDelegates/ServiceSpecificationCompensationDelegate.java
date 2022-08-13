package com.dtag.bm.process.import_cfs_process.compensationDelegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
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

@Component("serviceSpecificationCompensationDelegate")
public class ServiceSpecificationCompensationDelegate implements JavaDelegate{

	private final Logger logger = LoggerFactory.getLogger(ServiceSpecificationCompensationDelegate.class);
	@Autowired
	RestClientAdaptor restClientAdaptor;
	
	@Value("${bmServiceSpecificationCompensationUrl}")
	private String bmServiceSpecificationCompensationUrl;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		logger.info("compensation Rollback for created services specification started for request ID: "+execution.getVariable("serviceReqId"));
		try {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		
		Object transformationServiceSpecificationResponse=execution.getVariableTyped("transformationServiceSpecificationResponse",false).getValue();
		   
		logger.info("reverting Service specification compensation Rollback via request: "+transformationServiceSpecificationResponse.toString());
		   
		   HttpEntity<Object> entity = new HttpEntity<>(transformationServiceSpecificationResponse.toString(), headers);

			ResponseEntity<String> response = restClientAdaptor.callDeleteRestApi(bmServiceSpecificationCompensationUrl, entity);
			
		logger.info(" services specification response"+response.getBody());
		logger.info("reverting created services specification completed..");
	}catch(Exception exception) {
		logger.error("Error occoured: "+exception.getMessage());
		exception.printStackTrace();
	}
	}
}