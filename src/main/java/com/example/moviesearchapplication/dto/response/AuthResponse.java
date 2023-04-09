package com.example.moviesearchapplication.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
public class AuthResponse {
    private String token;
}
