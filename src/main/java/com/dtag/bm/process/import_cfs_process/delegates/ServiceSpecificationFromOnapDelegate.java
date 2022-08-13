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

@Component("serviceSpecificationFromOnapDelegate")
public class ServiceSpecificationFromOnapDelegate implements JavaDelegate{

	@Autowired
	RestClientAdaptor restClientAdaptor;
	
	@Value("${onapServiceSpecificationUrl}")
	private String onapServiceSpecificationUrl;
	
	private final Logger logger = LoggerFactory.getLogger(ServiceSpecificationFromOnapDelegate.class);
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		logger.info("------------getting Service Specification From ONAP started for request ID: "+execution.getVariable("serviceReqId")+".....----------");
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);				
			
			logger.info("Import CFS Event ID Captured :  " + execution.getVariable("serviceReqId"));
			logger.info("ONAP Rest API for getting Service Characteristics URL :  " + onapServiceSpecificationUrl);
			
			StringBuffer urlBuffer = new StringBuffer(onapServiceSpecificationUrl);
			urlBuffer.append(execution.getVariable("serviceReqId").toString());
			
			ResponseEntity<String> onapServiceSpecificationResponse = restClientAdaptor.callGetRestApi(urlBuffer.toString(), String.class);
			
			
			 JsonValue onapServiceSpecificationResponseJsonValue = SpinValues.jsonValue(onapServiceSpecificationResponse.getBody()).create();
			logger.info("onap Service Specification Response: "+onapServiceSpecificationResponseJsonValue.getValue().toString());
				logger.info("------------getting Service Specification From ONAP completed.....----------");
			execution.setVariable("onapServiceSpecificationResponse", onapServiceSpecificationResponseJsonValue);
			
		
			}catch(Exception ex) {
				logger.error("Error Occoured in getting Service Specification From ONAP for request ID: "+execution.getVariable("serviceReqId")+" is "+ex.getMessage());
				ex.printStackTrace();
				throw new BpmnError("Error_Failed_To_Connect_Rollback");
			}
	}

}
