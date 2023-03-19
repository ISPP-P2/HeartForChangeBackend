package com.ispp.heartforchange.dto;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Grant;
import com.ispp.heartforchange.entity.GrantState;

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
public class GrantDTO implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy= GenerationType.AUTO)
	@JsonProperty("id")
	private Long id;
	
	@NotNull
	@JsonProperty("privateGrant")
	private Boolean privateGrant;
	
	@NotNull
	@JsonProperty("gubernamental")
	private Boolean gubernamental;
	
	@NotNull
	@JsonProperty("state")
	private GrantState state;
	
	@NotBlank
	@NotNull
	@Size(max = 1000)
	@JsonProperty("justification")
	private String justification;
	
	@Min(0)
	@JsonProperty("amount")
	private Double amount;
	
	public GrantDTO(Grant grantSaved) {
		this.amount = grantSaved.getAmount();
		this.gubernamental = grantSaved.getGubernamental();
		this.id = grantSaved.getId();
		this.justification = grantSaved.getJustification();
		this.privateGrant = grantSaved.getPrivateGrant();
		this.state = grantSaved.getState();
	}

}
