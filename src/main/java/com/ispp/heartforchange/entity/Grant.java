package com.ispp.heartforchange.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ispp.heartforchange.dto.GrantDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grant")
public class Grant implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private Boolean privateGrant;

	@NotNull
	private Boolean gubernamental;

	@NotNull
	private GrantState state;

	@NotBlank
	@NotNull
	@Size(max = 1000)
	private String justification;

	@Min(0)
	private Double amount;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ong_id")
	private Ong ong;

	public Grant(GrantDTO grantDTO) {
		this.id = grantDTO.getId();
		this.amount = grantDTO.getAmount();
		this.gubernamental = grantDTO.getGubernamental();
		this.justification = grantDTO.getJustification();
		this.privateGrant = grantDTO.getPrivateGrant();
		this.state = grantDTO.getState();
	}

}
