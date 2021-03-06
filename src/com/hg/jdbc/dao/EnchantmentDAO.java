package com.hg.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hg.HgConfig;
import com.hg.jdbc.Conexao;
import com.hg.jdbc.dao.model.Enchantment;
import com.hg.jdbc.dao.model.Item;

public class EnchantmentDAO implements BaseDAO {

	private final Connection connection;

	public EnchantmentDAO(HgConfig hgConfig) throws SQLException {
		this(hgConfig.getIsMySQL(), hgConfig.getServer(), hgConfig.getDatabase(), hgConfig.getUser(), hgConfig.getPassword());
	}

	public EnchantmentDAO(Boolean isMySQL, String server, String database, String user, String password) throws SQLException {
		this.connection = Conexao.getConnection(isMySQL, server, database, user, password);
	}


	@Override
	public void closeConnection() throws SQLException {
		this.connection.close();
	}


	@Override
	public void createTableMySql() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS hg_enchantments ( ");
		sql.append("		id INT AUTO_INCREMENT, ");
		sql.append("		type VARCHAR(100), ");
		sql.append("		level INT, ");
		sql.append("		item_id INT, ");
		sql.append("		PRIMARY KEY (id), ");
		sql.append("		FOREIGN KEY (item_id) REFERENCES hg_items(id) ");
		sql.append(" ) ");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}

	}


	@Override
	public void createTableSqlite() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" CREATE TABLE IF NOT EXISTS hg_enchantments ( ");
		sql.append("		id INTEGER PRIMARY KEY AUTOINCREMENT,");
		sql.append("		type text, ");
		sql.append("		level INTEGER, ");
		sql.append("		item_id INTEGER, ");
		sql.append("		FOREIGN KEY (item_id) REFERENCES hg_items(id) ");
		sql.append(" ) ");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}

	}


	public void dropTable() throws SQLException {

		StringBuffer sql = new StringBuffer();

		sql.append(" DROP TABLE IF EXISTS hg_enchantments ");

		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}

	}


	@Override
	public void insert(Object object) throws SQLException {

		Enchantment enchantment = (Enchantment) object;
		String sql = "insert into hg_enchantments (type, level, item_id) values (?,?,?)";
		PreparedStatement stmt = connection.prepareStatement(sql);

		stmt.setString(1, enchantment.getType());
		stmt.setInt(2, enchantment.getLevel());
		stmt.setInt(3, enchantment.getItem().getId());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	@Override
	public List<Enchantment> listAll() throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from hg_enchantments");

		ResultSet rs = stmt.executeQuery();
		List<Enchantment> enchantments = new ArrayList<Enchantment>();

		while (rs.next()) {
			Enchantment enchantmentDatabase = new Enchantment();
			enchantmentDatabase.setId(rs.getInt("id"));
			enchantmentDatabase.setType(rs.getString("type"));
			enchantmentDatabase.setLevel(rs.getInt("level"));
			enchantmentDatabase.setItem(new Item(rs.getInt("item_id")));

			enchantments.add(enchantmentDatabase);
		}

		rs.close();
		stmt.close();
		return enchantments;
	}


	@Override
	public void delete(Integer id) throws SQLException {

		String sql = "delete from hg_enchantments where id = " + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}


	@Override
	public Object findById(Integer id) throws SQLException {

		String sql = "select * from hg_enchantments where id=" + id;
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();
		Enchantment enchantment = new Enchantment();

		while (rs.next()) {
			enchantment.setId(rs.getInt("id"));
			enchantment.setType(rs.getString("type"));
			enchantment.setLevel(rs.getInt("level"));
			enchantment.setItem(new Item(rs.getInt("item_id")));
		}

		rs.close();
		stmt.close();
		return enchantment;
	}


	public List<Enchantment> findByItemId(Integer itemId) throws SQLException {

		PreparedStatement stmt = connection.prepareStatement("select * from hg_enchantments where item_id=" + itemId);

		ResultSet rs = stmt.executeQuery();
		List<Enchantment> enchantments = new ArrayList<Enchantment>();

		while (rs.next()) {
			Enchantment enchantmentDatabase = new Enchantment();
			enchantmentDatabase.setId(rs.getInt("id"));
			enchantmentDatabase.setType(rs.getString("type"));
			enchantmentDatabase.setLevel(rs.getInt("level"));
			enchantmentDatabase.setItem(new Item(rs.getInt("item_id")));

			enchantments.add(enchantmentDatabase);
		}

		rs.close();
		stmt.close();
		return enchantments;

	}


	@Override
	public void update(Object object) throws SQLException {

		Enchantment enchantment = (Enchantment) object;
		String sql = "update hg_enchantments set type=?, level=?, item_id=? where id=?";
		PreparedStatement stmt = connection.prepareStatement(sql.toString());

		stmt.setString(1, enchantment.getType());
		stmt.setInt(2, enchantment.getLevel());
		stmt.setInt(3, enchantment.getItem().getId());
		stmt.setInt(4, enchantment.getId());

		try {
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
		}
	}

}
