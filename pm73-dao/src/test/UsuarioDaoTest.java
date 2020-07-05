package test;


import br.com.caelum.pm73.dao.CriadorDeSessao;
import br.com.caelum.pm73.dao.LeilaoDao;
import br.com.caelum.pm73.dao.UsuarioDao;
import br.com.caelum.pm73.dominio.Usuario;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UsuarioDaoTest {

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
    public void deveEncontrarPeloNomeEEmailMockado() {

        Usuario novoUsuario = new Usuario("Joao da Silva", "joao@dasilva.com.br");
        usuarioDao.salvar(novoUsuario);

        Usuario usuario = usuarioDao.porNomeEEmail("Joao da Silva", "joao@dasilva.com.br");

        Assert.assertEquals("Joao da Silva", usuario.getNome());
        Assert.assertEquals("joao@dasilva.com.br", usuario.getEmail());
    }

    @Test
    public void deveRetornarNuloSeNaoEncontrarUsuario() {

        Usuario usuario = usuarioDao.porNomeEEmail("Joao Joaquim", "joao@joaquim.com.br");

        Assert.assertNull(usuario);
    }


}
