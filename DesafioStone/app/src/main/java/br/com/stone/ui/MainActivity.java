package br.com.stone.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.devmarvel.creditcardentry.library.CreditCard;
import com.devmarvel.creditcardentry.library.CreditCardForm;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

import br.com.stone.BuildConfig;
import br.com.stone.R;
import br.com.stone.controller.PagamentoController;
import br.com.stone.domain.Resposta;
import br.com.stone.domain.Transacao;
import br.com.stone.util.ExportDatabaseFile;
import br.com.stone.util.InternetReceive;
import br.com.stone.util.InternetUtil;
import br.com.stone.util.Load;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements InternetReceive.OnInternetListener{
    @ViewById(R.id.toolbar)
    Toolbar mActionBarToolbar;
    @ViewById(R.id.cartaoCredito)
    CreditCardForm creditCardForm;
    @ViewById
    EditText nome;
    @ViewById
    EditText valor;
    Transacao transacao;
    @Bean
    PagamentoController pagamentoController;
    Load load;

    @AfterViews
    void onInicioMain(){
        EventBus.getDefault().register(this);
        transacao = new Transacao();
        load = new Load(this);
        InternetReceive.getInstance(this).register(this);

        if(BuildConfig.BUILD_TYPE.equals("debug"))
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                PermissionGen.with(this).addRequestCode(100).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).request();
            else
                ExportDatabaseFile.export(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void permissaoEscritaConcedida(){
        ExportDatabaseFile.export(this);
    }

    @PermissionFail(requestCode = 100)
    public void permissaoEscritaNegada(){
        Toast.makeText(this, "É necessario emitir permissão", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRespostaSarvidor(final Resposta resposta){
        load.hide();
        Toast.makeText(this, resposta.getMensagem(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebClientError(final String message){
        load.hide();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Click
    void efetuarTransacao(){
        if(TextUtils.isEmpty(nome.getText())){
            onWebClientError(getResources().getString(R.string.nome_obrigatorio));
            return;
        }

        if(!creditCardForm.isCreditCardValid()){
            onWebClientError(getResources().getString(R.string.cartao_invalido));
            return;
        }

        if(TextUtils.isEmpty(valor.getText())){
            onWebClientError(getResources().getString(R.string.valor_obrigatorio));
            return;
        }

        CreditCard cartaoCredito = creditCardForm.getCreditCard();
        transacao.setNumero(cartaoCredito.getCardNumber());
        transacao.setAno(cartaoCredito.getExpYear());
        transacao.setMes(cartaoCredito.getExpMonth());
        transacao.setBandeira(cartaoCredito.getCardType().name);
        transacao.setNome(nome.getText().toString());
        transacao.setCvv(cartaoCredito.getSecurityCode());
        transacao.setValor(new BigDecimal(valor.getText().toString()));

        if(InternetUtil.isOnline(this)){
            pagamentoController.executaTransacao(transacao);
            load.show();
        }else{
            onWebClientError(getResources().getString(R.string.sem_conexao));
        }
    }

    @Override
    public void onConnect() {
        Toast.makeText(this, getResources().getString(R.string.conexao_restabelecida), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnect() {
        Toast.makeText(this, getResources().getString(R.string.sem_conexao), Toast.LENGTH_SHORT).show();
    }
}
