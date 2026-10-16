package br.com.vistoria.model;

public class Equipamento {

    public static final String TIPO_EXTINTOR = "EXTINTOR";
    public static final String TIPO_SAIDA = "SAIDA";
    public static final String TIPO_HIDRANTE = "HIDRANTE";
    public static final String TIPO_ILUMINACAO = "ILUMINACAO";
    public static final String TIPO_ALARME = "ALARME";
    public static final String TIPO_OBSERVACAO = "OBSERVACAO";

    public static final String STATUS_CONFORME = "CONFORME";
    public static final String STATUS_NAO_CONFORME = "NAO_CONFORME";
    public static final String STATUS_NAO_APLICAVEL = "NAO_APLICAVEL";

    private int id;
    private int plantaId;
    private String tipo;
    private double posX;
    private double posY;
    private String identificacao;
    private String campo1Label;
    private String campo1Valor;
    private String campo2Label;
    private String campo2Valor;
    private String localizacao;
    private String status = STATUS_CONFORME;
    private String observacoes;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPlantaId() { return plantaId; }
    public void setPlantaId(int plantaId) { this.plantaId = plantaId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getPosX() { return posX; }
    public void setPosX(double posX) { this.posX = posX; }

    public double getPosY() { return posY; }
    public void setPosY(double posY) { this.posY = posY; }

    public String getIdentificacao() { return identificacao; }
    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }

    public String getCampo1Label() { return campo1Label; }
    public void setCampo1Label(String campo1Label) { this.campo1Label = campo1Label; }

    public String getCampo1Valor() { return campo1Valor; }
    public void setCampo1Valor(String campo1Valor) { this.campo1Valor = campo1Valor; }

    public String getCampo2Label() { return campo2Label; }
    public void setCampo2Label(String campo2Label) { this.campo2Label = campo2Label; }

    public String getCampo2Valor() { return campo2Valor; }
    public void setCampo2Valor(String campo2Valor) { this.campo2Valor = campo2Valor; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public String getCssClasse() {
        switch (tipo) {
            case TIPO_EXTINTOR: return "extintor";
            case TIPO_SAIDA: return "saida";
            case TIPO_HIDRANTE: return "hidrante";
            case TIPO_ILUMINACAO: return "iluminacao";
            case TIPO_ALARME: return "alarme";
            default: return "observacao";
        }
    }
}
