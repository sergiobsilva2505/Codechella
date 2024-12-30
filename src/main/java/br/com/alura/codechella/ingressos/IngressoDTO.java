package br.com.alura.codechella.ingressos;

public record IngressoDTO(
        Long id,
        Long eventoId,
        TipoIngresso tipo,
        Double valor,
        int total
) {
    public static IngressoDTO toDto(Ingresso ingresso) {
        return new IngressoDTO(ingresso.getId(), ingresso.getEventoId(), ingresso.getTipo(), ingresso.getValor(), ingresso.getTotal());
    }
}
