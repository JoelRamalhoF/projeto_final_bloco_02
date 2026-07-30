package com.generation.farmacia.model;

import java.time.LocalDate;
import java.time.Period;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

//cria a entidade usuario e a tabela tb_usuarios
@Entity
@Table(name = "tb_usuarios")
public class Usuario {

	// Cria o ID Primary Key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// cria o atributo nome
	@NotBlank(message = "O Atributo Nome é Obrigatório!")
	@Size(max = 255, message = "O nome não pode ser maior do que 255 caracteres")
	@Column(length = 255)
	private String nome;

	// cria o atributo usuario
	@NotBlank(message = "O Atributo Usuário é Obrigatório!")
	@Email(message = "O Atributo Usuário deve ser um email válido!")
	@Size(max = 255, message = "O usuario não pode ser maior do que 255 caracteres")
	@Column(length = 255)
	private String usuario;

	// cria o atributo senha
	@NotBlank(message = "O Atributo Senha é Obrigatório!")
	@Size(min = 8, max = 255, message = "A Senha deve ter no mínimo 8 caracteres")
	@Column(length = 255)
	private String senha;

//cria o atributo foto
	@Size(max = 255, message = "O link da foto não pode ser maior do que 255 caracteres")
	@Column(length = 255)
	private String foto;

	// cria o atributo data de nascimento
	@NotNull(message = "Atributo data de nascimento do cliente é obrigatório!")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Past(message = "A data de nascimento deve ser anterior à data atual!")
	@Column(name = "data_nascimento", nullable = false)
	private LocalDate dataNascimento;

	// Método para calcular a idade do usuário com base na data de nascimento
	public int calcularIdade() {
		return Period.between(this.dataNascimento, LocalDate.now()).getYears();
	}

//Método para verificar se o usuário é maior de idade (18 anos ou mais)
	public Boolean verificarIdade(int idade) {
		return idade >= 18;
	}

//Getters and Setters
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

}