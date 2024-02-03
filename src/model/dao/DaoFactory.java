package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	public static SellerDao createSellerDao() { // posso chamar esse metodo sem precisar instanciá-lo, e ele chamar o metedo SelerDaoJBC, que tem justamente acesso a minha interface.
		return new SellerDaoJDBC(DB.getConnection());
	}

}
