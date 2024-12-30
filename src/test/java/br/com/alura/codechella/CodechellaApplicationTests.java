package br.com.alura.codechella;

import br.com.alura.codechella.eventos.EventoDTO;
import br.com.alura.codechella.eventos.TipoEvento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodechellaApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void cadastraNovoEvento() {
        EventoDTO evento = new EventoDTO(
                null,
                TipoEvento.SHOW,
                "Algum evento legal",
                LocalDate.of(2022, 10, 10),
                "Descrição do evento");

        webTestClient.post()
                .uri("/eventos")
                .bodyValue(evento)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EventoDTO.class)
                .value(response -> {
                    assertNotNull(response.eventoId());
                    assertEquals(response.tipo(), evento.tipo());
                    assertEquals(response.nome(), evento.nome());
                    assertEquals(response.data(), evento.data());
                    assertEquals(response.descricao(), evento.descricao());
                });
    }

    @Test
    void buscarEvento() {
        EventoDTO evento = new EventoDTO(
                20L,
                TipoEvento.WORKSHOP,
                "Café com Ideias",
                LocalDate.of(2025, 1, 25),
                "Um evento que ensina técnicas de brainstorming e priorização para alavancar projetos.");

        webTestClient.post()
                .uri("/eventos")
                .bodyValue(evento)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(EventoDTO.class)
                .value(response -> {
                    EventoDTO eventoDTO = response.getLast();
                    assertEquals(eventoDTO.eventoId(), evento.eventoId());
                    assertEquals(eventoDTO.tipo(), evento.tipo());
                    assertEquals(eventoDTO.nome(), evento.nome());
                    assertEquals(eventoDTO.data(), evento.data());
                    assertEquals(eventoDTO.descricao(), evento.descricao());
                });
    }

}
