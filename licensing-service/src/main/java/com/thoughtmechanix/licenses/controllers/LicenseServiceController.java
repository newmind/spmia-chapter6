package com.thoughtmechanix.licenses.controllers;

import com.thoughtmechanix.licenses.model.License;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * LicenseServiceController
 */
@RestController
@RequestMapping(value="v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

  @GetMapping("/{licenseId}")
  public License getLicenses(
    @PathVariable("organizationId") String organizationId,
    @PathVariable("licenseId") String licenseId) {
      return new License()
        .withId(licenseId)
        .withOrganizationId(organizationId)
        .withProductName("Teleco")
        .withLicenseType("Seat");
  }
}