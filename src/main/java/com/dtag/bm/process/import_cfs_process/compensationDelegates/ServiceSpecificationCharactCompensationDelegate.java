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

@Component("serviceSpecificationCharactCompensationDelegate")
public class ServiceSpecificationCharactCompensationDelegate implements JavaDelegate{

	private final Logger logger = LoggerFactory.getLogger(ServiceSpecificationCharactCompensationDelegate.class);

	@Autowired
	RestClientAdaptor restClientAdaptor;
	
	@Value("${bmServiceSpecificationCharactCompensationUrl}")
	private String bmServiceSpecificationCharactCompensationUrl;
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		logger.info("compensation Rollback for created services specification charact started for request ID: "+execution.getVariable("serviceReqId"));
		try {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		
		Object serviceSpecificationCharatPayload=execution.getVariableTyped("serviceSpecificationCharatPayload",false).getValue();
		   
		logger.info("crating Service specification compensation Rollback via request: "+serviceSpecificationCharatPayload.toString());
		   
		   HttpEntity<Object> entity = new HttpEntity<>(serviceSpecificationCharatPayload.toString(), headers);

			ResponseEntity<String> response = restClientAdaptor.callDeleteRestApi(bmServiceSpecificationCharactCompensationUrl, entity);
			
		logger.info(" services specification charact response"+response.getBody());
		logger.info("reverting created services specification charact completed..");
	}catch(Exception exception) {
		logger.error("Error occoured: "+exception.getMessage());
		exception.printStackTrace();
	}
}
	

}
