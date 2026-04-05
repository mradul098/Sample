package com.meraproject.companymicro;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company createCompany(Company company) {
        if (company.getId() == null || company.getId().isEmpty()) {
            company.setId(java.util.UUID.randomUUID().toString());
        }
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
