package apliccation;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("TEST 1 => findById:"); // primeiro a perisitência para depois intanciar.
		
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
		
		Seller seller = sellerDao.findById(1);
		sellerDao.deleteById(seller.getId());
		
		System.out.println("Delete completed");

	}

}
