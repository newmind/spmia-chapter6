package com.thoughtmechanix.licenses.services;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.thoughtmechanix.licenses.clients.OrganizationDiscoveryClient;
import com.thoughtmechanix.licenses.clients.OrganizationFeignClient;
import com.thoughtmechanix.licenses.clients.OrganizationRestTemplateClient;
import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.LicenseRepository;
import com.thoughtmechanix.licenses.utils.UserContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * LisenseService
 */
@Service
public class LicenseService {

  private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

  @Autowired
  private LicenseRepository licenseRepository;

  @Autowired
  OrganizationFeignClient organizationFeignClient;

  @Autowired
  OrganizationRestTemplateClient organizationRestClient;

  @Autowired
  OrganizationDiscoveryClient organizationDiscoveryClient;


  @Autowired
  ServiceConfig config;

  public License getLicense(String organizationId, String licenseId) {
    License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

    Organization org = getOrganization(organizationId);
    return license
      .withOrganizationName(org.getName())
      .withContactName(org.getContactName())
      .withContactEmail(org.getContactEmail())
      .withContactPhone(org.getContactPhone())
      .withComment(config.getExampleProperty());
  }

  @HystrixCommand
  private Organization getOrganization(String organizationId) {
    return organizationRestClient.getOrganization(organizationId);
  }

  private void randomlyRunLong() {
    // 데이터베이스 호출 3회중 1회 지연시키는 함수
    Random rand = new Random();

    int randomNum = rand.nextInt((3-1) + 1) + 1;
    if (randomNum == 3) sleep();
  }

  private void sleep() {
    try {
      Thread.sleep(11000); // 테스트로 11초 쉬게함
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @HystrixCommand(
      commandProperties={
          @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="12000")  // 테스트를 위해 12초 대기하게 해서, 11초가 걸려도 통과되게
      }
  )
  public List<License> getLicensesByOrg(String organizationId) {
    logger.debug("LiceseService.getLicensesByOrg Correlation id: {}",
      UserContextHolder.getContext().getCorrelationId());

    randomlyRunLong();
    return licenseRepository.findByOrganizationId(organizationId);
  }

  public void saveLicense(License license) {
    license.withId(UUID.randomUUID().toString());

    licenseRepository.save(license);
  }

  public void updateLicense(License license) {
    licenseRepository.save(license);
  }

  public void deleteLicense(License license) {
    licenseRepository.delete(license);
  }

  private Organization retrieveOrgInfo(String organizationId, String clientType){
    Organization organization = null;

    switch (clientType) {
        case "feign":
            System.out.println("I am using the feign client");
            organization = organizationFeignClient.getOrganization(organizationId);
            break;
        case "rest":
            System.out.println("I am using the rest client");
            organization = organizationRestClient.getOrganization(organizationId);
            break;
        case "discovery":
            System.out.println("I am using the discovery client");
            organization = organizationDiscoveryClient.getOrganization(organizationId);
            break;
        default:
            organization = organizationRestClient.getOrganization(organizationId);
    }

    return organization;
  }

  public License getLicense(String organizationId, String licenseId, String clientType) {
    License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    Organization org = retrieveOrgInfo(organizationId, clientType);
    return license
      .withOrganizationName(org.getName())
      .withContactName(org.getContactName())
      .withContactEmail(org.getContactEmail())
      .withContactPhone(org.getContactPhone())
      .withComment(config.getExampleProperty());
  }
}