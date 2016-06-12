package br.com.stone.io;

import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import br.com.stone.domain.Resposta;
import br.com.stone.domain.Transacao;

/**
 * Created by Tiago on 11/06/2016.
 */
@Rest(rootUrl = "http://demo0600344.mockable.io", converters = {GsonHttpMessageConverter.class})
public interface PagamentoIO extends RestClientErrorHandling {

    @Post("/save")
    public Resposta executaTransacao(Transacao transacao);
}
