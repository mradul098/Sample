package com.meraproject.apigateway;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.meraproject.apigateway.FeignClients.CompanyClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.amqp.rabbit.core.*;

import java.util.List;

@Service
public class AggregatorServiceImpl implements AggregatorService{
    private final RestTemplate restTemplate;
    private final CompanyClient companyClient;

    public AggregatorServiceImpl(RestTemplate restTemplate, CompanyClient companyClient) {
        this.restTemplate = restTemplate;
        this.companyClient = companyClient;
    }
    //JobService is on localhost:8080 and CompanyService is on localhost:8081
    @Override
    public CompanyDTO getCompanyWithJobs(String companyId){
        //Get company details from CompanyService
        // CompanyDTO company = restTemplate.getForObject("http://localhost:8081/companies/" + companyId, CompanyDTO.class);
        System.out.println("Fetching company details for companyId: " + companyId);
        CompanyDTO company = companyClient.getCompanyById(companyId);
        
        //Get jobs for the company from JobService
        JobDTO[] jobs = restTemplate.getForObject("http://localhost:8080/jobs/company/" + companyId, JobDTO[].class);

        //Set jobs in the company DTO
        company.setJobs(List.of(jobs));
        System.out.println(company);
        System.out.println(jobs);

        return company;
    }

     public CompanyDTO getCompanyWithJobsFallback(String companyId, Throwable t) {
        System.out.println("Circuit breaker fallback triggered! Reason: " + t.getMessage());
        CompanyDTO fallback = new CompanyDTO();
        fallback.setName("Service Unavailable");
        fallback.setJobs(List.of());
        return fallback;
    }

    public CompanyDTO rateLimiterFallback(String companyId, Throwable t) {
        System.out.println("Rate limiter fallback triggered! Reason: " + t.getMessage());
        CompanyDTO fallback = new CompanyDTO();
        fallback.setName("Rate Limit Exceeded");
        fallback.setJobs(List.of());
        return fallback;
    }
}
