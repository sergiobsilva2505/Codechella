package br.com.alura.codechella.eventos;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EventoService {
    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Flux<Evento> findAll() {
        return eventoRepository.findAll();
    }

    public Mono<Evento> findById(Long id) {
        return eventoRepository.findById(id);
    }

    public Mono<Evento> save(Evento evento) {
        return eventoRepository.save(evento);
    }

    public Mono<Void> deleteById(Long id) {
        return eventoRepository.findById(id)
                .flatMap(eventoRepository::delete);
    }

    public Mono<Evento> update(Long id, Evento entity) {
        return eventoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(evento -> {
                    evento.setTipo(entity.getTipo());
                    evento.setNome(entity.getNome());
                    evento.setData(entity.getData());
                    evento.setDescricao(entity.getDescricao());
                    return evento;
                })
                .flatMap(eventoRepository::save);
    }

    public Flux<Evento> findByTipo(String tipo) {
        TipoEvento tipoEvento = TipoEvento.valueOf(tipo.toUpperCase());
        return eventoRepository.findByTipo(tipoEvento);
    }
}
