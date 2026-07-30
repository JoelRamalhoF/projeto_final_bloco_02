package com.generation.farmacia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//cria a entidade categoria e a tabela categorias
@Entity
@Table(name = "tb_categorias")
public class Categoria {

	// Cria o ID Primary Key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// cria o atributo tipo
		@NotBlank(message = "O atributo tipo é obrigatório!")
		@Size(min = 5, max = 100, message = "O atributo tipo deve conter no mínimo 5 e no máximo 100 caracteres!")
		@Column(length = 100, nullable = false)
		private String tipo;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTipo() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
}
