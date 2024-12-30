package br.com.alura.codechella.ingressos;

import br.com.alura.codechella.vendas.CompraDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@Tag(name = "Ingressos")
@RestController
@RequestMapping("/ingressos")
public class IngressoController {
    private final IngressoService ingressoService;
    private final Sinks.Many<IngressoDTO> ingressoSink;

    public IngressoController(IngressoService ingressoService) {
        this.ingressoService = ingressoService;
        this.ingressoSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping
    @Operation(summary = "Listar todos os ingressos")
    public Flux<IngressoDTO> findAll() {
        return ingressoService.obterTodos();
    }

    @GetMapping(value = "/{id}/disponivel", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Listar ingressos disponíveis")
    public Flux<IngressoDTO> totalDisponivel(@PathVariable Long id) {
        return Flux.merge(ingressoService.obterPorId(id), ingressoSink.asFlux())
                .delayElements(Duration.ofSeconds(4));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter ingresso por ID")
    public Mono<IngressoDTO> obterPorId(@PathVariable Long id) {
        return ingressoService.obterPorId(id);
    }

    @PostMapping
    @Operation(summary = "Salvar um ingresso")
    public Mono<IngressoDTO> save(@RequestBody IngressoRequest request) {
        return ingressoService.salvar(request);
    }

    @PostMapping("/compra")
    @Operation(summary = "Comprar um ingresso")
    public Mono<IngressoDTO> comprar(@RequestBody CompraDTO dto) {
        return ingressoService.comprar(dto)
                .doOnSuccess(ingressoSink::tryEmitNext);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um ingresso")
    public Mono<Void> deleteById(@PathVariable Long id) {
        return ingressoService.excluir(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Alterar um ingresso")
    public Mono<IngressoDTO> update(@PathVariable Long id, @RequestBody IngressoRequest request) {
        return ingressoService.alterar(id, request);
    }
}
