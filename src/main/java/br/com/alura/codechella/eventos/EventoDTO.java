package br.com.alura.codechella.eventos;

import java.time.LocalDate;

public record EventoDTO(
        Long eventoId,
        TipoEvento tipo,
        String nome,
        LocalDate data,
        String descricao
) {
    public static EventoDTO toDto(Evento evento) {
        return new EventoDTO(evento.getId(), evento.getTipo(), evento.getNome(), evento.getData(), evento.getDescricao());
    }
}
