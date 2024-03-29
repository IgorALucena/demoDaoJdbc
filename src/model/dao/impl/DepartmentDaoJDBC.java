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
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection coon = null;
	
	public DepartmentDaoJDBC(Connection coon) {
		this.coon = coon;
	}
	
	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = coon.prepareStatement("INSERT INTO Department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				else {
					throw new DbException("Unexpected error! No rows affected!");
				}
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		
		try {
			st = coon.prepareStatement("UPDATE Department SET Name = ? WHERE Id = ?");
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
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
			
			st = coon.prepareStatement("DELETE FROM Department WHERE Id = ?");
			st.setInt(1, id);
			
			st.execute();
			
		}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = coon.prepareStatement("SELECT * FROM Department WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				 Department dp = null;
				 dp = instanciateDepartment(rs);
				 return dp;
			}
			return null;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}	
		
	}

	private Department instanciateDepartment(ResultSet rs) throws SQLException {
		Department dp = new Department();
		dp.setName(rs.getString("Name"));
		dp.setId(rs.getInt("Id"));
		return dp;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		List<Department> list = new ArrayList();
		
		try {
			st = coon.prepareStatement("SELECT * FROM Department");
			rs = st.executeQuery();
			while(rs.next()) {
				list.add(instanciateDepartment(rs));
			}
			return list;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}	
	}

}
