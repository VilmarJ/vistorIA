package br.com.vistoria.model;

public class Edificacao {

    private int id;
    private int usuarioId;
    private String nome;
    private String razaoSocial;
    private String cnpj;
    private String tipoOcupacao;
    private String endereco;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private double areaConstruida;
    private int pavimentos;
    private double altura;
    private int ocupantes;
    private String tipoEdificacao;
    private String usoEdificacao;
    private double areaPavimento;
    private int qtdBlocos = 1;
    private boolean temSubsolo;
    private boolean temMezanino;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getTipoOcupacao() { return tipoOcupacao; }
    public void setTipoOcupacao(String tipoOcupacao) { this.tipoOcupacao = tipoOcupacao; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public double getAreaConstruida() { return areaConstruida; }
    public void setAreaConstruida(double areaConstruida) { this.areaConstruida = areaConstruida; }

    public int getPavimentos() { return pavimentos; }
    public void setPavimentos(int pavimentos) { this.pavimentos = pavimentos; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }

    public int getOcupantes() { return ocupantes; }
    public void setOcupantes(int ocupantes) { this.ocupantes = ocupantes; }

    public String getTipoEdificacao() { return tipoEdificacao; }
    public void setTipoEdificacao(String tipoEdificacao) { this.tipoEdificacao = tipoEdificacao; }

    public String getUsoEdificacao() { return usoEdificacao; }
    public void setUsoEdificacao(String usoEdificacao) { this.usoEdificacao = usoEdificacao; }

    public double getAreaPavimento() { return areaPavimento; }
    public void setAreaPavimento(double areaPavimento) { this.areaPavimento = areaPavimento; }

    public int getQtdBlocos() { return qtdBlocos; }
    public void setQtdBlocos(int qtdBlocos) { this.qtdBlocos = qtdBlocos; }

    public boolean isTemSubsolo() { return temSubsolo; }
    public void setTemSubsolo(boolean temSubsolo) { this.temSubsolo = temSubsolo; }

    public boolean isTemMezanino() { return temMezanino; }
    public void setTemMezanino(boolean temMezanino) { this.temMezanino = temMezanino; }

    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        if (endereco != null) sb.append(endereco);
        if (numero != null && !numero.isBlank()) sb.append(", ").append(numero);
        if (bairro != null && !bairro.isBlank()) sb.append(" — ").append(bairro);
        if (cidade != null) sb.append(", ").append(cidade);
        if (estado != null) sb.append("/").append(estado);
        return sb.toString();
    }
}
