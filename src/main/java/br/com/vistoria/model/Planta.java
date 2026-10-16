package br.com.vistoria.model;

import java.time.LocalDateTime;

public class Planta {

    private int id;
    private int edificacaoId;
    private String edificacaoNome;
    private String nome;
    private String pavimento;
    private String arquivo;
    private LocalDateTime dataUpload;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEdificacaoId() { return edificacaoId; }
    public void setEdificacaoId(int edificacaoId) { this.edificacaoId = edificacaoId; }

    public String getEdificacaoNome() { return edificacaoNome; }
    public void setEdificacaoNome(String edificacaoNome) { this.edificacaoNome = edificacaoNome; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPavimento() { return pavimento; }
    public void setPavimento(String pavimento) { this.pavimento = pavimento; }

    public String getArquivo() { return arquivo; }
    public void setArquivo(String arquivo) { this.arquivo = arquivo; }

    public LocalDateTime getDataUpload() { return dataUpload; }
    public void setDataUpload(LocalDateTime dataUpload) { this.dataUpload = dataUpload; }

    public String getDataUploadFormatada() {
        return dataUpload == null ? "" : dataUpload.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public boolean isImagem() {
        if (arquivo == null) return false;
        String ext = arquivo.toLowerCase();
        return ext.endsWith(".png") || ext.endsWith(".jpg") || ext.endsWith(".jpeg");
    }
}
