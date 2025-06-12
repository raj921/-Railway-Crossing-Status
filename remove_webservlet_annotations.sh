#!/bin/bash

# Find all Java files with @WebServlet annotation and remove the annotation
find /Users/rajkumar/akilaassi/src/main/java -type f -name "*.java" -exec sed -i '' '/@WebServlet("/d' {} \;

# Also remove the import statement
find /Users/rajkumar/akilaassi/src/main/java -type f -name "*.java" -exec sed -i '' '/import jakarta.servlet.annotation.WebServlet;/d' {} \;

echo "All @WebServlet annotations and imports have been removed from Java files."
