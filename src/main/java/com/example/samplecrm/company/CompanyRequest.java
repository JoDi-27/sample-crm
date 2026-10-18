package com.example.samplecrm.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyRequest(@NotBlank @Size(max = 200) String name, @Size(max = 500) String website) {}
