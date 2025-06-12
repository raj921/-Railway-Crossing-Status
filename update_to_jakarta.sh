#!/bin/bash

# Find all Java files and replace javax.servlet with jakarta.servlet
find /Users/rajkumar/akilaassi/src/main/java -type f -name "*.java" -exec sed -i '' 's/javax\.servlet/jakarta.servlet/g' {} \;

echo "All Java files have been updated to use jakarta.servlet"
