package apliccation;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		Seller rows = sellerDao.findById(3);
		
		System.out.println("TEST 1 => findById:");
		
		System.out.println(rows);

	}

}
