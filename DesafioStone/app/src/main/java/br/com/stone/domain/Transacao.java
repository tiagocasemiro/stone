package br.com.stone.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * Created by Tiago on 11/06/2016.
 */
@Data
@DatabaseTable(tableName = "transacao")
public class Transacao implements Serializable {
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String nome;
    @DatabaseField
    private String numero;
    @DatabaseField
    private Integer ano;
    @DatabaseField
    private Integer mes;
    @DatabaseField
    private String bandeira;
    @DatabaseField
    private String cvv;
    @DatabaseField
    private BigDecimal valor;
}
