package com.ispp.heartforchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SigninDTO {
	
	private String token;
	private String refresh;
}
