package com.eugeneburak.trycatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/fragments/trycatch")
public class FragmentsController {

  /**
   * Returns content (HTML code) of fragment depending on needed resource name, format and feature
   * toggle.
   *
   * @return HTML code
   */
  @GetMapping("/{fileName}")
  public String getFragmentHtml(
      @PathVariable String fileName, @RequestParam(name = "format") String format) {

    return "some resource from resources package";
  }
}
