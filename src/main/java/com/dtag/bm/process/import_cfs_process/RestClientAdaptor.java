package com.dtag.bm.process.import_cfs_process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClientAdaptor {

	@Autowired
	RestTemplate restTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(RestClientAdaptor.class);
	public ResponseEntity<String> callPostRestApi(String url, @Nullable Object request, Class<String> responseType){
		ResponseEntity<String> response=null;
		try {
			logger.info("calling POST API for URL: "+url);
			response = restTemplate.postForEntity(url, request, responseType);
			logger.info("calling POST API for URL: "+url+" completed successfully..");
		}catch(ResourceAccessException accessException) {
			logger.error("Error calling POST API for URL:"+url+" got error: "+accessException.getMessage());
			throw new ResourceAccessException("calling Resource not avaialble: "+url);
		}catch(RestClientException clientException) {
			logger.error("Error calling POST API for URL:"+url+" got error: "+clientException.getMessage());
			throw new RestClientException("Error calling POST API for URL:"+url);
		}
		return response;
	}
	
   public ResponseEntity<String> callGetRestApi(String url, Class<String> responseType){
	   ResponseEntity<String> response=null;
	   try {
		   logger.info("calling GET API for URL: "+url);
			response = restTemplate.getForEntity(url,responseType);
			logger.info("calling GET API for URL: "+url+" completed successfully..");
		}catch(ResourceAccessException accessException) {
			logger.error("Error calling GET API for URL:"+url+" got error: "+accessException.getMessage());
			throw new ResourceAccessException("calling Resource not avaialble: "+url);
		}catch(RestClientException clientException) {
			logger.error("Error calling GET API for URL:"+url+" got error: "+clientException.getMessage());
			throw new RestClientException("Error calling POST API for URL:"+url);
		}
	   return response;
	}

public ResponseEntity<String> callDeleteRestApi(String masterAttributeBmCompensationUrl, HttpEntity<Object> entity) throws Exception {
	 ResponseEntity<String> response=null;
	try {
		 logger.info("calling Delete API for URL: "+masterAttributeBmCompensationUrl);
		 response= restTemplate.exchange(masterAttributeBmCompensationUrl, HttpMethod.DELETE, entity, String.class);
		 logger.info("calling Delete API for URL: "+masterAttributeBmCompensationUrl+" completed successfully..");
	}catch(ResourceAccessException accessException) {
		logger.error("Error calling GET API for URL:"+masterAttributeBmCompensationUrl+" got error: "+accessException.getMessage());
		throw new ResourceAccessException("calling Resource not avaialble: "+masterAttributeBmCompensationUrl);
	}catch(RestClientException clientException) {
		logger.error("Error calling GET API for URL:"+masterAttributeBmCompensationUrl+" got error: "+clientException.getMessage());
		throw new RestClientException("Error calling POST API for URL:"+masterAttributeBmCompensationUrl);
	}catch(Exception ex) {
		throw new Exception("Exception Occoured"+ex.getMessage());
	}
	return response;
}
}
