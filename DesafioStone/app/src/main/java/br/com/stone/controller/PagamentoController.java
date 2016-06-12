package br.com.stone.controller;

import android.content.Context;
import android.support.annotation.UiThread;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.RestErrorHandler;
import org.greenrobot.eventbus.EventBus;
import org.springframework.core.NestedRuntimeException;

import br.com.stone.R;
import br.com.stone.domain.Resposta;
import br.com.stone.domain.Transacao;
import br.com.stone.io.PagamentoIO;
import br.com.stone.persistence.dao.TransacaoDAO;

/**
 * Created by Tiago on 11/06/2016.
 */
@EBean
public class PagamentoController implements RestErrorHandler {
    @RestService
    PagamentoIO pagamentoIO;
    @Bean
    TransacaoDAO transacaoDAO;
    @RootContext
    Context context;

    @AfterInject
    void afterInjectRepositoryController(){
        pagamentoIO.setRestErrorHandler(this);
    }

    @Background
    public void executaTransacao(Transacao transacao){
        Resposta resposta = pagamentoIO.executaTransacao(transacao);
        transacaoDAO.save(transacao);
        onResult(resposta);
    }

    @UiThread
    public void onResult(Resposta resposta){
        if(resposta != null)
            EventBus.getDefault().post(resposta);
        else
            EventBus.getDefault().post(context.getResources().getString(R.string.erro_requisicao));
    }

    @UiThread
    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException e) {
        e.printStackTrace();
    }
}
