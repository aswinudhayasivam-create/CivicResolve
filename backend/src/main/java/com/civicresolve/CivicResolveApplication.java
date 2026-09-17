package com.civicresolve;
import com.civicresolve.model.Category;
import com.civicresolve.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication public class CivicResolveApplication {
 public static void main(String[] args){SpringApplication.run(CivicResolveApplication.class,args);}

 /**
  * Keeps the reference data required for complaint submission present in every
  * deployment. This is idempotent: it only inserts categories whose names are
  * absent, so it never changes existing categories or production complaints.
  */
 @Bean CommandLineRunner seedCategories(CategoryRepository categories) {
  return args -> {
   String[][] defaults = {
    {"Roads", "Potholes, damaged roads and signage"},
    {"Street Lights", "Broken public lighting"},
    {"Water Supply", "Water supply and pipeline issues"},
    {"Garbage", "Waste collection and sanitation"},
    {"Drainage", "Drainage and flooding"},
    {"Electricity", "Public electrical infrastructure"},
    {"Public Safety", "Civic safety issues"},
    {"Transportation", "Public transport issues"},
    {"Other", "Other grievances"}
   };
   for (String[] category : defaults) {
    if (!categories.existsByName(category[0])) {
     Category entity = new Category();
     entity.name = category[0];
     entity.description = category[1];
     categories.save(entity);
    }
   }
  };
 }
}
