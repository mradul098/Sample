package com.meraproject.apigateway;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AggregatorServiceImpl implements AggregatorService{
    private final RestTemplate restTemplate;
    public AggregatorServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    //JobService is on localhost:8080 and CompanyService is on localhost:8081
    @Override
    public CompanyDTO getCompanyWithJobs(String companyId){
        //Get company details from CompanyService
        CompanyDTO company = restTemplate.getForObject("http://localhost:8081/companies/" + companyId, CompanyDTO.class);

        //Get jobs for the company from JobService
        JobDTO[] jobs = restTemplate.getForObject("http://localhost:8080/jobs/company/" + companyId, JobDTO[].class);

        //Set jobs in the company DTO
        company.jobs = List.of(jobs);

        return company;
    }
}
