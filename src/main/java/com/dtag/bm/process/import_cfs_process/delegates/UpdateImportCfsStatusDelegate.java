package com.dtag.bm.process.import_cfs_process.delegates;

import org.camunda.bpm.engine.delegate.BpmnError;
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
import com.dtag.bm.process.import_cfs_process.models.UpdateServiceRequest;

@Component("updateImportCfsStatusDelegate")
public class UpdateImportCfsStatusDelegate implements JavaDelegate{

	@Autowired
	RestClientAdaptor restClientAdaptor;
	
	@Value("${updateCfsImportUrl}")
	private String updateCfsImportUrl;
	private final Logger logger = LoggerFactory.getLogger(UpdateImportCfsStatusDelegate.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		logger.info("---------------CFS import updating started for service request ID: "+execution.getVariable("serviceReqId")+"......-------------");
		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);				
			
			String serviceReqId=(String) execution.getVariable("serviceReqId");
			
			logger.info("updating CFS import back via URL :  " + updateCfsImportUrl);
			UpdateServiceRequest updateServiceRequest = new UpdateServiceRequest();
			updateServiceRequest.setOnapServiceCharactRequestId(serviceReqId);
			updateServiceRequest.setStatus("Completed");
			HttpEntity<Object> entity = new HttpEntity<>(updateServiceRequest, headers);

			ResponseEntity<String> response = restClientAdaptor.callPostRestApi(updateCfsImportUrl, entity,String.class);
			
			execution.setVariable("updateCfsImportResponse", response.getBody());
			logger.info("CFS import updating Response : "+response.getBody());
			logger.info("----------------CFS import updated successfully for request ID: "+serviceReqId+"......---------------");
			}catch(Exception ex) {
				logger.error("error occoured: "+ex.getMessage());
				ex.printStackTrace();
				throw new BpmnError("Error_Failed_To_Connect_Rollback");
			}
		
		
		
	}

}
