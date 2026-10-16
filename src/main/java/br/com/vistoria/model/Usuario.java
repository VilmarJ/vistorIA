package br.com.vistoria.model;

import java.time.LocalDateTime;

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senhaHash;
    private String telefone;
    private String identificacao;
    private LocalDateTime dataCadastro;

    public Usuario() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getIdentificacao() { return identificacao; }
    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public String getIniciais() {
        if (nome == null || nome.isBlank()) return "?";
        String[] partes = nome.trim().split("\\s+");
        String iniciais = String.valueOf(partes[0].charAt(0));
        if (partes.length > 1) {
            iniciais += partes[partes.length - 1].charAt(0);
        }
        return iniciais.toUpperCase();
    }
}
