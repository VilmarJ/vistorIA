package br.com.vistoria.model;

public class Rota {

    private int id;
    private int plantaId;
    private String pontos;
    private String origem;
    private String destino;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPlantaId() { return plantaId; }
    public void setPlantaId(int plantaId) { this.plantaId = plantaId; }

    public String getPontos() { return pontos; }
    public void setPontos(String pontos) { this.pontos = pontos; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getPontosSvg() {
        if (pontos == null) return "";
        return pontos.replace(";", " ");
    }
}
