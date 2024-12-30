package br.com.alura.codechella.vendas;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface VendaRepository extends ReactiveCrudRepository<Venda, Long> {
}
