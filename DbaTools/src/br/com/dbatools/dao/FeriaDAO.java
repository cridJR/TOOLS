package br.com.dbatools.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.dbatools.domain.Empresa;
import br.com.dbatools.domain.InstalacaoConfig;
import br.com.dbatools.domain.Perfil;
import br.com.dbatools.domain.QtdDia;
import br.com.dbatools.domain.TipoConfig;
import br.com.dbatools.domain.Usuario;
import br.com.dbatools.domain.Feria;
import br.com.dbatools.domain.Cmdb;
import br.com.dbatools.factory.ConexaoFactory;

public class FeriaDAO {
	
	public void salvar(Feria p,String user) throws SQLException {
		int i = 1;
		DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO tb_planilha_ferias ");
		sql.append(" (cod_planilha_ferias,cod_usuario,qtidade_dias,data_inicio,substituto1,substituto2,substituto3,data_fim) ");
		sql.append(" values (seq_planilha_ferias.nextval,(select cod_usuario from tb_usuario where usuario = ? ),?,to_date(?,'DD/MM/YYYY'),?,?,?, to_date(?,'DD/MM/YYYY') + ? ) ");

		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());
		comando.setString(i++, user);
        //comando.setLong(1, p.getCod_usuario().getCod_usuario());	
        comando.setLong(2, p.getQtidade_dias().getQtd_dias());
        
        System.out.println(p.getData_inicio());
       // System.out.println(format.format(p.getData_inicio()));
      
        comando.setString(3, p.getData_inicio());
        
        
        comando.setLong(4, p.getSubstituto1().getCod_usuario());
        comando.setLong(5, p.getSubstituto2().getCod_usuario());
        comando.setLong(6, p.getSubstituto3().getCod_usuario());

        System.out.println(p.getQtidade_dias().getQtd_dias());
        
        comando.setString(7,p.getData_inicio());
        comando.setLong(8, p.getQtidade_dias().getQtd_dias());
        
        
	   comando.executeUpdate();

	}
	
	public ArrayList<Feria> listar(String user, String computador) throws SQLException {
		StringBuilder sql = new StringBuilder();
		int i = 1;
		sql.append(" select ");
		sql.append(" s.cod_planilha_ferias,  ");
		
		
        sql.append(" substr(u.nom_usuario,1,instr(u.nom_usuario,' ')-1)||' '|| ");
        sql.append(" substr(u.nom_usuario, instr(u.nom_usuario, ' ') + 1, instr(substr(u.nom_usuario, instr(u.nom_usuario, ' ') + 1, length(u.nom_usuario)), ' ') - 1) ");
        sql.append(" nom_usuario, ");

		
		sql.append(" s.cod_usuario, ");
		sql.append(" s.substituto1, ");
		sql.append(" s.substituto2, ");
		sql.append(" s.substituto3, ");
		
		sql.append("  (select ");
		sql.append("  substr(z.nom_usuario,1,instr(z.nom_usuario,' ')-1)||' '||substr(z.nom_usuario, instr(z.nom_usuario, ' ') + 1, instr(substr(z.nom_usuario, instr(z.nom_usuario, ' ') + 1, length(z.nom_usuario)), ' ') - 1)  ");
		sql.append("from tb_usuario z where z.cod_usuario = s.substituto1) NOM_SUBSTITUTO1,  ");

		sql.append("  (select  ");
		sql.append("  substr(z.nom_usuario,1,instr(z.nom_usuario,' ')-1)||' '||substr(z.nom_usuario, instr(z.nom_usuario, ' ') + 1, instr(substr(z.nom_usuario, instr(z.nom_usuario, ' ') + 1, length(z.nom_usuario)), ' ') - 1)  ");
		sql.append("from tb_usuario z where z.cod_usuario = s.substituto2) NOM_SUBSTITUTO2,  ");

		sql.append("  (select  ");
		sql.append("  substr(z.nom_usuario,1,instr(z.nom_usuario,' ')-1)||' '||substr(z.nom_usuario, instr(z.nom_usuario, ' ') + 1, instr(substr(z.nom_usuario, instr(z.nom_usuario, ' ') + 1, length(z.nom_usuario)), ' ') - 1)  ");
		sql.append("from tb_usuario z where z.cod_usuario = s.substituto3) NOM_SUBSTITUTO3,  ");

		
		
		sql.append(" d.qtd_dias qtidade_dias, ");
		sql.append(" to_char(s.data_inicio,'DD/MM/YYYY') data_inicio, ");
		sql.append(" to_char(s.data_fim,'DD/MM/YYYY') data_fim ");
 		sql.append(" from tb_planilha_ferias s, tb_usuario u, tb_qtd_dias d ");
 		sql.append(" where ");
 		sql.append(" s.cod_usuario = u.cod_usuario ");
 		sql.append(" and s.qtidade_dias = d.qtd_dias ");
 		sql.append(" and u.cod_empresa = (select cod_empresa from tb_usuario where usuario = ? ) ");


		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());
		
		comando.setString(i++, user);
		
		ResultSet resultado = comando.executeQuery();

		ArrayList<Feria> itens = new ArrayList<Feria>();

		while (resultado.next()) {
			
            Feria u = new Feria();
			u.setCod_planilha_ferias(resultado.getLong("cod_planilha_ferias"));
			u.setData_inicio(resultado.getString("data_inicio"));
			u.setData_fim(resultado.getString("data_fim"));
            
            
            Usuario p = new Usuario();
            p.setCod_usuario(resultado.getLong("cod_usuario"));
            p.setNom_usuario(resultado.getString("nom_usuario"));

            QtdDia d = new QtdDia();
            d.setQtidade_dias(resultado.getLong("qtidade_dias"));

            
            Usuario q = new Usuario();
            q.setCod_usuario(resultado.getLong("cod_usuario"));
            q.setNom_usuario(resultado.getString("nom_substituto1"));
            
            Usuario r = new Usuario();
            r.setCod_usuario(resultado.getLong("cod_usuario"));
            r.setNom_usuario(resultado.getString("nom_substituto2"));

            Usuario s = new Usuario();
            s.setCod_usuario(resultado.getLong("cod_usuario"));
            s.setNom_usuario(resultado.getString("nom_substituto3"));

            u.setCod_usuario(p);
            u.setSubstituto1(q);
            u.setSubstituto2(r);
            u.setSubstituto3(s);
            u.setQtidade_dias(d);
            itens.add(u);

		}
		return itens;

	}

	public void excluir(Feria p,String user) throws SQLException {
		StringBuilder sql = new StringBuilder();
		int i = 1;
		
		sql.append("DELETE FROM tb_planilha_ferias ");
		sql.append("WHERE cod_planilha_ferias = ? ");
		sql.append("and cod_usuario = (select cod_usuario from tb_usuario where usuario = ? ) ");

		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());
		comando.setLong(1, p.getCod_planilha_ferias());
		comando.setString(2, user);
		
		comando.executeUpdate();

	}

    public void editar(Feria p,String user) throws SQLException {
	StringBuilder sql = new StringBuilder();
	int i = 1;
	sql.append("UPDATE tb_planilha_ferias ");
	sql.append("set cod_usuario = (select cod_usuario from tb_usuario where usuario = ? ), qtidade_dias = ?, data_inicio = ?, substituto1 = ?, substituto2 = ?, substituto3 = ?, data_fim = (to_date(?,'DD/MM/YYYY') + ?)  ");
	sql.append("WHERE ");
	sql.append("cod_planilha_ferias = ? ");
	sql.append("and cod_usuario =  (select cod_usuario from tb_usuario where usuario = ? ) ");

	Connection conexao = ConexaoFactory.conectar();

	PreparedStatement comando = conexao.prepareStatement(sql.toString());
	comando.setString(1, user);
    //comando.setLong(1, p.getCod_usuario().getCod_usuario());
    comando.setLong(2, p.getQtidade_dias().getQtd_dias());
    comando.setString(3, p.getData_inicio());
    comando.setLong(4, p.getSubstituto1().getCod_usuario());
    comando.setLong(5, p.getSubstituto2().getCod_usuario());
    comando.setLong(6, p.getSubstituto3().getCod_usuario());

    comando.setString(7, p.getData_inicio());
    comando.setLong(8, p.getQtidade_dias().getQtd_dias());

    
    comando.setLong(9, p.getCod_planilha_ferias());
    comando.setString(10, user);
    
	comando.executeUpdate();

}
	

}
