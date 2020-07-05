package test;

import br.com.caelum.pm73.dao.CriadorDeSessao;
import br.com.caelum.pm73.dao.LeilaoDao;
import br.com.caelum.pm73.dao.UsuarioDao;
import br.com.caelum.pm73.dominio.Leilao;
import br.com.caelum.pm73.dominio.Usuario;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LeilaoDaoTest {
    private UsuarioDao usuarioDao;
    private Session session;
    private LeilaoDao leilaoDao;

    @Before
    public void antes(){
        session = new CriadorDeSessao().getSession();
        usuarioDao = new UsuarioDao(session);

        leilaoDao = new LeilaoDao(session);

        session.beginTransaction();

        System.out.println("abrindo sessão...");
    }

    @After
    public void depois(){
        session.getTransaction().rollback();
        session.close();

        System.out.println("fazendo rollback...");
        System.out.println("fechando sessão...");
    }

    @Test
    public void deveContarLeiloesNaoEncerradors() {

        Usuario mauricio = new Usuario("Mauricio", "mauricio@mauricio.com.br");

        Leilao ativo = new Leilao("Geladeira",1500.0,mauricio,false);
        Leilao encerrado = new Leilao("XBox",700.0,mauricio,false);
        encerrado.encerra();

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(ativo);
        leilaoDao.salvar(encerrado);

        long total = leilaoDao.total();

        Assert.assertEquals(1L,total);

    }


}
