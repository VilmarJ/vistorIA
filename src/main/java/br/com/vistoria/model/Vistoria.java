package br.com.vistoria.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Vistoria {

    public static final String STATUS_PENDENTE = "PENDENTE";
    public static final String STATUS_ANDAMENTO = "ANDAMENTO";
    public static final String STATUS_CONCLUIDA = "CONCLUIDA";

    private int id;
    private int edificacaoId;
    private String edificacaoNome;
    private int usuarioId;
    private String usuarioNome;
    private Integer plantaId;
    private LocalDate dataVistoria;
    private String tipo = "PERIODICA";
    private String status = STATUS_PENDENTE;
    private String observacoes;
    private List<ItemVistoria> itens = new ArrayList<>();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEdificacaoId() { return edificacaoId; }
    public void setEdificacaoId(int edificacaoId) { this.edificacaoId = edificacaoId; }

    public String getEdificacaoNome() { return edificacaoNome; }
    public void setEdificacaoNome(String edificacaoNome) { this.edificacaoNome = edificacaoNome; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }

    public Integer getPlantaId() { return plantaId; }
    public void setPlantaId(Integer plantaId) { this.plantaId = plantaId; }

    public LocalDate getDataVistoria() { return dataVistoria; }
    public void setDataVistoria(LocalDate dataVistoria) { this.dataVistoria = dataVistoria; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public List<ItemVistoria> getItens() { return itens; }
    public void setItens(List<ItemVistoria> itens) { this.itens = (itens != null) ? itens : new ArrayList<>(); }

    public long getTotalItens() { return itens.size(); }

    public long getTotalConformes() {
        return itens.stream().filter(i -> "CONFORME".equals(i.getStatus())).count();
    }

    public long getTotalNaoConformes() {
        return itens.stream().filter(i -> "NAO_CONFORME".equals(i.getStatus())).count();
    }

    public long getTotalNaoAplicaveis() {
        return itens.stream().filter(i -> "NAO_APLICAVEL".equals(i.getStatus())).count();
    }
}
