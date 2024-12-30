package br.com.alura.codechella.ingressos;

public record IngressoRequest(
        Long eventoId,
        TipoIngresso tipo,
        Double valor,
        int total
) {
    public Ingresso toEntity() {
        return new Ingresso(eventoId, tipo, valor, total);
    }
}
