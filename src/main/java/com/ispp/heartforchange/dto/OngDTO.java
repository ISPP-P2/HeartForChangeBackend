package com.ispp.heartforchange.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ispp.heartforchange.entity.Ong;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class OngDTO extends AccountDTO {

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
		super(ong.getUsername(), ong.getEmail(), ong.getPassword(),ong.getRolAccount());
		this.id = ong.getId();
		this.name = ong.getName();
		this.cif = ong.getCif();
		this.description = ong.getDescription();
	}
}
