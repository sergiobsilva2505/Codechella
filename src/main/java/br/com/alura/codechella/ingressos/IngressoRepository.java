package br.com.alura.codechella.ingressos;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface IngressoRepository extends ReactiveCrudRepository<Ingresso, Long> {
    Flux<Ingresso> findByTipo(TipoIngresso tipoIngresso);
}
