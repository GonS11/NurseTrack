package com.nurse_track_back.nurse_track_back.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleResponse
{
    private Long id;
    private String fullName;
    private String username;
}
