package com.ispp.heartforchange.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ispp.heartforchange.dto.PersonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "volunteer")
public class Volunteer extends Person{

	private static final long serialVersionUID = 1L;

	@NotNull
	@NotBlank
	@Size(max = 50)
	private String hourOfAvailability;
	
	@NotNull
	private Boolean sexCrimes;
	

	public Volunteer(PersonDTO personDto, @NotNull @NotBlank @Size(max = 50) String hourOfAvailability,
			@NotNull Boolean sexCrimes) {
		super(personDto);
		this.hourOfAvailability = hourOfAvailability;
		this.sexCrimes = sexCrimes;
	} 
}
