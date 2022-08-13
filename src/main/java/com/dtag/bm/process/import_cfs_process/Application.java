package com.dtag.bm.process.import_cfs_process;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableProcessApplication
@ComponentScan("com.dtag.bm.*")
public class Application {
    
	/*@Value("${apiVersion}")	
    private String apiVersion;*/
	
	 @Bean
		public RestTemplate restTemplate() {
		    return new RestTemplate();
		}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	/**
	 * Bean for Swagger. 
	 * @return
	 */
	
	/*@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Order Management Process").apiInfo(apiInfo()).select()
				.paths(regex("/v1/.*")).build().directModelSubstitute(XMLGregorianCalendar.class, MixIn.class);
	}

	public static interface MixIn {
		@JsonIgnore
		public void setYear(int year);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Order Management Process REST APIs").description("Order Management Process REST APIs")
				.termsOfServiceUrl("http://....").contact("TechMahindra").license("TechMahindra Licensed")
				.licenseUrl("https://techmahindra.com").version(apiVersion).build();
	}*/	


	/*@Bean public ProcessEngineConfigurationImpl processEngineConfigurationImpl(List<ProcessEnginePlugin> processEnginePlugins){            

        final SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
       // configuration.setJdbcBatchProcessing(false);
        configuration.setFailedJobRetryTimeCycle(camundaCustomConfiguration.getFailedJobRetryTimeCycle());
        configuration.setJobExecutorActivate(true);
        //configuration.getJobExecutor().setMaxJobsPerAcquisition(12);
        configuration.setJobExecutorDeploymentAware(true);
        configuration.getProcessEnginePlugins().add(new CompositeProcessEnginePlugin(processEnginePlugins));
        configuration.setAuthorizationEnabled(true);
        
        //configuration.setBatchOperationsForHistoryCleanup();
        configuration.setHistoryCleanupBatchSize(camundaCustomConfiguration.getHistoryCleanupBatchSize());
        configuration.setBatchOperationHistoryTimeToLive(camundaCustomConfiguration.getBatchOperationHistoryTimeToLive());
        configuration.setHistoryCleanupBatchWindowStartTime(camundaCustomConfiguration.getHistoryCleanupBatchWindowStartTime());
        configuration.setHistoryCleanupBatchWindowEndTime(camundaCustomConfiguration.getHistoryCleanupBatchWindowEndTime());
        return configuration;
}*/

}