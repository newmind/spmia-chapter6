package com.thoughtmechanix.licenses.repository;

import java.util.List;

import com.thoughtmechanix.licenses.model.License;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * LicenseRepository
 */
@Repository
public interface LicenseRepository extends CrudRepository<License, String> {

  public List<License> findByOrgranizationId(String organizationId);
  public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}