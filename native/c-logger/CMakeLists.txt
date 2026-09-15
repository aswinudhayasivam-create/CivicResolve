cmake_minimum_required(VERSION 3.20)
project(civic_audit C)
add_library(civic_audit SHARED src/audit_logger.c)
target_include_directories(civic_audit PUBLIC include)
