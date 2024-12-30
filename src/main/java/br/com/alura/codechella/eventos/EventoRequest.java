package br.com.alura.codechella.eventos;

import java.time.LocalDate;

public record EventoRequest(
        TipoEvento tipo,
        String nome,
        LocalDate data,
        String descricao
) {

    public Evento toEntity() {
        return new Evento(this.tipo, this.nome, this.data, this.descricao);
    }
}
