package br.com.alura.codechella.ingressos;

import br.com.alura.codechella.vendas.CompraDTO;
import br.com.alura.codechella.vendas.Venda;
import br.com.alura.codechella.vendas.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class IngressoService {
    private final IngressoRepository ingressoRepository;
    private final VendaRepository vendaRepository;

    public IngressoService(IngressoRepository ingressoRepository, VendaRepository vendaRepository) {
        this.ingressoRepository = ingressoRepository;
        this.vendaRepository = vendaRepository;
    }

    public Flux<IngressoDTO> obterTodos() {
        return ingressoRepository.findAll()
                .map(IngressoDTO::toDto);
    }

    public Mono<IngressoDTO> obterPorId(Long id) {
        return ingressoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND)))
                .map(IngressoDTO::toDto);
    }

    public Mono<IngressoDTO> salvar(IngressoRequest ingressoRequest) {
        Ingresso entity = ingressoRequest.toEntity();
        return ingressoRepository.save(entity)
                .map(IngressoDTO::toDto);
    }

    public Mono<Void> excluir(Long id) {
        return ingressoRepository.findById(id)
                .flatMap(ingressoRepository::delete);
    }

    public Mono<IngressoDTO> alterar(Long id, IngressoRequest request) {
        return ingressoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND)))
                .flatMap(ingresso -> {
                    ingresso.setEventoId(request.eventoId());
                    ingresso.setTipo(request.tipo());
                    ingresso.setValor(request.valor());
                    ingresso.setTotal(request.total());
                    return ingressoRepository.save(ingresso);
                })
                .map(IngressoDTO::toDto);
    }

    @Transactional
    public Mono<IngressoDTO> comprar(CompraDTO dto) {
        return ingressoRepository.findById(dto.ingressoId())
                .flatMap(ingresso -> {
                    Venda venda = new Venda();
                    venda.setIngressoId(ingresso.getId());
                    venda.setTotal(dto.total());
                    return vendaRepository.save(venda).then(Mono.defer(() -> {
                        ingresso.setTotal(ingresso.getTotal() - dto.total());
                        return ingressoRepository.save(ingresso);
                    }));
                }).map(IngressoDTO::toDto);
    }
}
