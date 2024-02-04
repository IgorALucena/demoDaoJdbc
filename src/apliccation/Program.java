package apliccation;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("TEST 1 => seller findById:"); // primeiro a perisitência para depois intanciar.
		
		Seller rows = sellerDao.findById(3);
		
		System.out.println(rows);
		
		System.out.println("TEST 2 => seller findByDepartment:");
		Department department = new Department(2, null);
		
		List<Seller> list = sellerDao.findByDepartment(department);
		for(Seller li: list) {
			System.out.println(li);
		}
		
		System.out.println("TEST 3 => seller findAll:");
		
		list = sellerDao.findAll();
		
		for(Seller sel: list) {
			System.out.println(sel);
		}
		
		System.out.println("\n=== TEST 4: seller insert ====="); // primeiro instancio para persistência
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted! New id = " + newSeller.getId());
		
		System.out.println("\n=== TEST 5: seller update =====");
		
		Seller seller = sellerDao.findById(1);
		seller.setName("Martha");
		sellerDao.update(seller);
		
		System.out.println("update completed");
		
		System.out.println("\n=== TEST 6: seller delete =====");
		
		seller = sellerDao.findById(1);
		sellerDao.deleteById(seller.getId());
		
		System.out.println("Delete completed");

		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("\n=== TEST 1: department Insert =====");
		
		department = new Department(null,"Drinks");
		departmentDao.insert(department);
		
		System.out.println("New Department: " + department.getName() + " Id: " + department.getId());
		
		System.out.println("\n=== TEST 2: department findById and Update =====");
		
		Department findByidDe = departmentDao.findById(1);
		
		findByidDe.setName("Computerss");
		
		departmentDao.update(findByidDe);
		
		System.out.println("Updated!");
		
		System.out.println("\n=== TEST 2: department delete =====");
		
		departmentDao.deleteById(5);
		
		System.out.println("deleted");
		
		System.out.println("\n=== TEST 2: department findAll =====");
		
		List<Department> dpFindAll = departmentDao.findAll();
		
		for(Department dp: dpFindAll) {
			System.out.println(dp);
		}
		

	}

}
