package com.generation.farmacia.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produtos")

public class Produto {
//Cria o ID primary key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// cria o atributo nome
	@NotBlank(message = "O atributo nome é obrigatório!")
	@Size(min = 5, max = 255, message = "O atributo nome deve conter no mínimo 5 e no máximo 255 caracteres!")
	@Column(length = 255, nullable = false)
	private String nome;

	// Cria o atributo preco com BigDecimal para valor monetário
	@NotNull(message = "Atributo preço é obrigatório!")
	@Positive(message = "O preço deve ser positivo!")
	@Digits(integer = 10, fraction = 2)
	private BigDecimal preco;

	// cria o atributo foto
	@NotBlank(message = "O link da foto é obrigatório!")
	@Size(min = 5, max = 255, message = "O atributo foto deve conter um URL com  no mínimo 5 e no máximo 255 caracteres!")
	@Column(length = 255, nullable = false)
	private String foto;

	@UpdateTimestamp // Cria e Atualiza a data/hora
	private LocalDateTime data;

	// cria relacionamento com Categoria, Varios produtos para 1 categoria
	@ManyToOne
	@JsonIgnoreProperties("produtos")
	private Categoria categoria;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}
