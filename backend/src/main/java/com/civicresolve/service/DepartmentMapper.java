package com.civicresolve.service;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service public class DepartmentMapper {
 private static final Map<String,String> MAP=Map.ofEntries(
  Map.entry("Roads","Road Maintenance"),Map.entry("Street Lights","Electrical Department"),Map.entry("Water Supply","Water Department"),
  Map.entry("Garbage","Sanitation"),Map.entry("Waste Management","Sanitation"),Map.entry("Drainage","Water Department"),
  Map.entry("Education","Education Department"),Map.entry("Transportation","Transport Department"),Map.entry("Public Transport","Transport Department"),
  Map.entry("Bus Stops","Transport Department"),Map.entry("Electricity","Electrical Department"));
 public String forCategory(String category){return MAP.getOrDefault(category,"Civic Services");}
}
