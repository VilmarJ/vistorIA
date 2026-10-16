package br.com.vistoria.model;

public class ItemVistoria {

    private int id;
    private int vistoriaId;
    private String categoria;
    private String item;
    private String status;
    private String gravidade;
    private String observacao;

    private String edificacaoNome;
    private java.time.LocalDate dataVistoria;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVistoriaId() { return vistoriaId; }
    public void setVistoriaId(int vistoriaId) { this.vistoriaId = vistoriaId; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGravidade() { return gravidade; }
    public void setGravidade(String gravidade) { this.gravidade = gravidade; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public String getEdificacaoNome() { return edificacaoNome; }
    public void setEdificacaoNome(String edificacaoNome) { this.edificacaoNome = edificacaoNome; }

    public java.time.LocalDate getDataVistoria() { return dataVistoria; }
    public void setDataVistoria(java.time.LocalDate dataVistoria) { this.dataVistoria = dataVistoria; }
}
