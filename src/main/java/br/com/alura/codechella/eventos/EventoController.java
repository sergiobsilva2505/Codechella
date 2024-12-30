package br.com.alura.codechella.eventos;

import br.com.alura.codechella.traducao.TraducaoDeTextosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@Tag(name = "Eventos")
@RestController
@RequestMapping("/eventos")
public class EventoController {
    private EventoService eventoService;
    private Sinks.Many<Evento> eventoSink;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
        this.eventoSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping
    @Operation(summary = "Listar todos os eventos")
    public Flux<EventoDTO> findAll() {
        return eventoService.findAll().map(EventoDTO::toDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter evento por ID")
    public Mono<EventoDTO> findById(@PathVariable Long id) {
        return eventoService.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(EventoDTO::toDto);
    }

    @GetMapping("{id}/traduzir/{idioma}")
    @Operation(summary = "Obter evento por ID")
    public Mono<String> obterTraducao(@PathVariable Long id, @PathVariable String idioma) {
        return eventoService.findById(id)
                .flatMap(e -> TraducaoDeTextosService.obterTraducao(e.getDescricao(), idioma));
    }

    @GetMapping(value = "/tipo/{tipo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Listar eventos por tipo")
    public Flux<EventoDTO> findByTipo(@PathVariable String tipo) {
        return Flux.merge(eventoService.findByTipo(tipo), eventoSink.asFlux())
                .map(EventoDTO::toDto)
                .delayElements(Duration.ofSeconds(3));
    }

    @PostMapping
    @Operation(summary = "Salvar um evento")
    public Mono<EventoDTO> save(@RequestBody EventoRequest request) {
        Evento evento = request.toEntity();
        return eventoService.save(evento)
                .doOnSuccess(eventoSink::tryEmitNext)
                .map(EventoDTO::toDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um evento")
    public Mono<Void> deleteById(@PathVariable Long id) {
        return eventoService.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Alterar um evento")
    public Mono<EventoDTO> update(@PathVariable Long id, @RequestBody EventoRequest request) {
        return eventoService.update(id, request.toEntity()).map(EventoDTO::toDto);
    }

}
