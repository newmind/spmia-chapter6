package com.thoughtmechanix.licenses.controllers;

import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.services.LisenseService;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private LisenseService licenseService;

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

  @GetMapping("/{licenseId/{clientType}")
  public License getLicenseWithCllient(
    @PathVariable("organizationId") String organizationId,
    @PathVariable("licenseId") String licenseId,
    @PathVariable("clientType") String clientType) {
      //clientType : Discovery | Rest | Feign
    return licenseService.getLicense(organizationId, licenseId, clientType);
  }

}