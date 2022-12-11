package com.kelompok4.pagu.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Nama should not be empty")
    private String nama;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    @NotEmpty(message = "Program Kerja should not be empty")
    private String programKerja;
}