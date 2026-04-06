package com.meraproject.apigateway;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.meraproject.apigateway.FeignClients.CompanyClient;

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
        CompanyDTO company = companyClient.getCompanyById(companyId);

        //Get jobs for the company from JobService
        JobDTO[] jobs = restTemplate.getForObject("http://localhost:8080/jobs/company/" + companyId, JobDTO[].class);

        //Set jobs in the company DTO
        company.setJobs(List.of(jobs));
        System.out.println(company);
        System.out.println(jobs);

        return company;
    }
}
