package com.meraproject.apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AggregatorController {
    private final AggregatorService aggregatorService;
    public AggregatorController(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }
    @GetMapping("/company/{companyId}")
    public CompanyDTO getCompanyWithJobs(@PathVariable String companyId){
        return aggregatorService.getCompanyWithJobs(companyId);
    }

}
