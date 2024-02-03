package apliccation;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("TEST 1 => findById:");
		
		Seller rows = sellerDao.findById(3);
		
		System.out.println(rows);
		
		System.out.println("TEST 2 => findByDepartment:");
		Department department = new Department(2, null);
		
		List<Seller> list = sellerDao.findByDepartment(department);
		for(Seller li: list) {
			System.out.println(li);
		}
		
		System.out.println("TEST 3 => findAll:");
		
		list = sellerDao.findAll();
		
		for(Seller sel: list) {
			System.out.println(sel);
		}
		

	}

}
