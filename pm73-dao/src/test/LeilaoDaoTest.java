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

import java.util.Calendar;
import java.util.List;

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

        ativo.encerra();
        encerrado.encerra();

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(ativo);
        leilaoDao.salvar(encerrado);

        long total = leilaoDao.total();

        Assert.assertEquals(0L,total);

    }

    @Test
    public void deveRetornarLeiloesDeProdutosNovos() {
        Usuario mauricio = new Usuario("Mauricio Aniche",
                "mauricio@aniche.com.br");

        Leilao produtoNovo =
                new Leilao("XBox", 700.0, mauricio, false);
        Leilao produtoUsado =
                new Leilao("Geladeira", 1500.0, mauricio,true);

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(produtoNovo);
        leilaoDao.salvar(produtoUsado);

        List<Leilao> novos = leilaoDao.novos();

        Assert.assertEquals(1, novos.size());
        Assert.assertEquals("XBox", novos.get(0).getNome());
    }

    @Test
    public void deveTrazerSomenteLeiloesAntigos() {
        Usuario mauricio = new Usuario("Mauricio Aniche",
                "mauricio@aniche.com.br");

        Leilao recente =
                new Leilao("XBox", 700.0, mauricio, false);
        Leilao antigo =
                new Leilao("Geladeira", 1500.0, mauricio,true);

        Calendar dataRecente = Calendar.getInstance();
        Calendar dataAntiga = Calendar.getInstance();
        dataAntiga.add(Calendar.DAY_OF_MONTH, -10);

        recente.setDataAbertura(dataRecente);
        antigo.setDataAbertura(dataAntiga);

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(recente);
        leilaoDao.salvar(antigo);

        List<Leilao> antigos = leilaoDao.antigos();

        Assert.assertEquals(1, antigos.size());
        Assert.assertEquals("Geladeira", antigos.get(0).getNome());
    }

    @Test
    public void deveTrazerSomenteLeiloesAntigosHaMaisDe7Dias() {
        Usuario mauricio = new Usuario("Mauricio Aniche",
                "mauricio@aniche.com.br");

        Leilao noLimite =
                new Leilao("XBox", 700.0, mauricio, false);

        Calendar dataAntiga = Calendar.getInstance();
        dataAntiga.add(Calendar.DAY_OF_MONTH, -7);

        noLimite.setDataAbertura(dataAntiga);

        usuarioDao.salvar(mauricio);
        leilaoDao.salvar(noLimite);

        List<Leilao> antigos = leilaoDao.antigos();

        Assert.assertEquals(1, antigos.size());
    }


}
