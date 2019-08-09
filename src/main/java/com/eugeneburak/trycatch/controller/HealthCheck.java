package com.eugeneburak.trycatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
  @GetMapping("/health")
  public String healthyCheck() {
    return "I am alive!";
  }
}
