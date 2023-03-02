package com.ispp.heartforchange.dto;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Account;
import com.ispp.heartforchange.entity.Ong;

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
public class OngDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long id;
	
	@NotNull
	@NotBlank
	@Size(max = 50)
	@JsonProperty("name")
	private String name;
	
	@NotNull
	@JsonProperty("cif")
	private String cif;
	
	@NotNull
	@NotBlank
	@Size(max = 250)
	@JsonProperty("description")
	private String description;
	
	public OngDTO( Ong ong ) {
		super();
		this.id = ong.getId();
		this.name = ong.getName();
		this.cif = ong.getCif();
		this.description = ong.getDescription();
	}
	
}
