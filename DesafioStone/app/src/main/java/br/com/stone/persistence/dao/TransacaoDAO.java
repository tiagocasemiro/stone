package br.com.stone.persistence.dao;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.sql.SQLException;

import br.com.stone.domain.Transacao;
import br.com.stone.persistence.dao.helper.DatabaseHelper;

/**
 * Created by tiago.casemiro on 15/01/2016.
 */
@EBean
public class TransacaoDAO {
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Transacao, Long> dao;

    @Background
    public void save(Transacao transacao) {
        try {
            dao.create(transacao);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
