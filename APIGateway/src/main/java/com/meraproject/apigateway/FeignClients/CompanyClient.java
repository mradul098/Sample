package com.meraproject.apigateway.FeignClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.meraproject.apigateway.CompanyDTO;

@FeignClient(name = "company-microservice", url = "http://localhost:8081")
public interface CompanyClient {
    @GetMapping("/companies/{id}")
    CompanyDTO getCompanyById(@PathVariable String id);
}