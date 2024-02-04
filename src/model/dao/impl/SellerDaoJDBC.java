package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection coon;
	
	public SellerDaoJDBC (Connection coon) { // fazendo isso, basicamente todos os mÉtodos podem receber a conexão.
		this.coon = coon;	
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = coon.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}	
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = coon.prepareStatement(
					"UPDATE seller "
					+"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?  "
					+"WHERE Id = ? ");
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = coon.prepareStatement(
					"DELETE FROM seller "
					+ "WHERE Id = ?");
			st.setInt(1, id);
			
			st.execute();
			
		}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = coon.prepareStatement(
					"SELECT seller.*,department.Name as DepName "  
					+ "FROM seller INNER JOIN department "  
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ? ");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Department dep = instanciateDepartment(rs);
				Seller sel = instanciateSeller(rs, dep);
				return sel;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st); // não precisa fechar a conexão para reaproveitamento.	
		}
	}

	private Seller instanciateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sel = new Seller();
		sel.setId(rs.getInt("Id"));
		sel.setName(rs.getString("Name"));
		sel.setEmail(rs.getString("Email"));
		sel.setBaseSalary(rs.getDouble("BaseSalary"));
		sel.setBirthDate(rs.getDate("BirthDate"));
		sel.setDepartment(dep);
		return sel;
	}

	private Department instanciateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			coon = DB.getConnection();
			st = coon.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name ");
			rs = st.executeQuery();
			
			Map<Integer, Department> map = new HashMap<>(); 
			
			List<Seller> list = new ArrayList<>();
			
			while(rs.next()) {
				
				Department dp = map.get(rs.getInt("DepartmentId"));
				if(dp == null) {
					dp = instanciateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dp);
				}
				Seller sel = instanciateSeller(rs, dp);
				list.add(sel);
			}
			return list;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = coon.prepareStatement(
					"SELECT seller.*,department.Name as DepName "  
					+ "FROM seller INNER JOIN department "  
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name ");
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {
				
				Department dp = map.get(rs.getInt("DepartmentId"));
				
				if(dp == null) {
					dp = instanciateDepartment(rs);	 
					map.put(rs.getInt("DepartmentId"), dp); 
				}
				Seller sel = instanciateSeller(rs, dp); 
				list.add(sel);
			}
			return list;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st); 
		}
	}

}
