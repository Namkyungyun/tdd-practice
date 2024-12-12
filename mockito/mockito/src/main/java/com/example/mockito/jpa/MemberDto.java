package com.example.mockito.jpa;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto implements Serializable {
    private Long id;
    private String email;
    private String name;
}
