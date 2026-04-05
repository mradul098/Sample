package com.meraproject.companymicro;

import java.util.Optional;

public interface CompanyService {
    Company createCompany(Company company);
    Optional<Company> getCompanyById(String id);

    void deleteCompany(String id);
}
