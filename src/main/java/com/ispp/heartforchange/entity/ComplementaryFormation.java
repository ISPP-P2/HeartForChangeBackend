package com.ispp.heartforchange.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.ispp.heartforchange.dto.ComplementaryFormationDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "complementary_formation")
public class ComplementaryFormation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@NotBlank
	private String name;
	
	@NotNull
	@NotBlank
	private String organization;


	private LocalDate date;

	@NotNull
	@NotBlank
	private String place;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "beneficiary_id")
	private Beneficiary beneficiary;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "volunteer_id")
	private Volunteer volunteer;
	
	


	public ComplementaryFormation(ComplementaryFormationDTO complementaryFormationDTO) {
		this.id = complementaryFormationDTO.getId();
		this.name = complementaryFormationDTO.getName();
		this.organization = complementaryFormationDTO.getOrganization();
		this.date = complementaryFormationDTO.getDate();
		this.place = complementaryFormationDTO.getPlace();
	}

}
