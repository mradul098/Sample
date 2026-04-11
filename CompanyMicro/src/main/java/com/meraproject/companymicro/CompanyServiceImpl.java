package com.meraproject.companymicro;

import org.springframework.stereotype.Service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.*;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;
    private RabbitTemplate rabbitTemplate;

    public CompanyServiceImpl(CompanyRepository companyRepository, RabbitTemplate rabbitTemplate) {
        this.companyRepository = companyRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Company createCompany(Company company) {
        if (company.getId() == null || company.getId().isEmpty()) {
            company.setId(java.util.UUID.randomUUID().toString());
        }
        rabbitTemplate.convertAndSend("hello-queue", "Hello from Company Microservice");
        rabbitTemplate.convertAndSend("fanout-exchange", "", "Hello from Company Microservice Fanout");
        rabbitTemplate.convertAndSend(
            "main-exchange",   // goes to main exchange
            "main-key",        // routing key → main queue
            "Hello DLQ Flow"
        );

        return companyRepository.save(company);
    }

    @Override
    public Optional<Company> getCompanyById(String id) {
        return companyRepository.findById(id);
    }

    @Override
    public void deleteCompany(String id) {
        companyRepository.deleteById(id);
    }
}
