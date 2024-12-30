package br.com.alura.codechella.traducao;

import br.com.alura.codechella.eventos.EventoService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TraducaoDeTextosService {
    private EventoService eventoService;
    public static Mono<String> obterTraducao(String texto, String idioma) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api-free.deepl.com/v2/translate")
                .build();

        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("text", texto);
        req.add("target_lang", idioma);

        return webClient.post()
                .header( "Authorization", "DeepL-Auth-Key " + "65c114b3-9728-4fc9-a2f9-25c742218524:fx")
                .body(BodyInserters.fromFormData(req))
                .retrieve()
                .bodyToMono(Traducao.class)
                .map(Traducao::getTexto);
    }
}
