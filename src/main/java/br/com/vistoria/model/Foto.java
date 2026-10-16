package br.com.vistoria.model;

public class Foto {

    private int id;
    private int vistoriaId;
    private String arquivo;
    private String descricao;
    private String localizacao;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVistoriaId() { return vistoriaId; }
    public void setVistoriaId(int vistoriaId) { this.vistoriaId = vistoriaId; }

    public String getArquivo() { return arquivo; }
    public void setArquivo(String arquivo) { this.arquivo = arquivo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
}
