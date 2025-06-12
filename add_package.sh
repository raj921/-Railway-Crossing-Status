#!/bin/bash

# Directory containing the Java files
JAVA_DIR="src/main/java/com/example/userregistration"

# Add package declaration to each Java file
for file in "$JAVA_DIR"/*.java; do
    # Skip if not a regular file
    [ ! -f "$file" ] && continue
    
    # Add package declaration if not already present
    if ! grep -q '^package ' "$file"; then
        # Create a temporary file
        tmp_file="${file}.tmp"
        
        # Add package declaration
        echo 'package com.example.userregistration;' > "$tmp_file"
        echo '' >> "$tmp_file"
        
        # Add the rest of the file
        cat "$file" >> "$tmp_file"
        
        # Replace the original file
        mv "$tmp_file" "$file"
        
        echo "Added package to $file"
    else
        echo "Package already exists in $file"
    fi
done
