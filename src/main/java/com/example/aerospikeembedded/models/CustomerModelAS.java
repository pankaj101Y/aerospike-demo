package com.example.aerospikeembedded.models;


import lombok.Data;

import java.util.UUID;

@Data
public class CustomerModelAS {

    public String firstName = "FIRST NAME  =" + UUID.randomUUID();

    public String lastName = "LAST NAME  =" + UUID.randomUUID();
}
