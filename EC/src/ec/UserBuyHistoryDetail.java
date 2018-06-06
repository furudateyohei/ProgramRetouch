package ec;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.BuyDataBeans;
import beans.ItemDataBeans;
import dao.UserDAO;

/**
 * 購入履歴画面
 * @author d-yamaguchi
 *
 */
@WebServlet("/UserBuyHistoryDetail")
public class UserBuyHistoryDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String id = request.getParameter("buy_id");
			UserDAO UserDAO = new UserDAO();
			BuyDataBeans bdb = UserDAO.getBuyDataBeansById(id);

			request.setAttribute("bdb", bdb);

			UserDAO userDao = new UserDAO();
			List<ItemDataBeans> BuyDetailgetId = userDao.BuyDetailgetId(id);

			request.setAttribute("BuyDetailgetId", BuyDetailgetId);

			UserDAO UserDao = new UserDAO();
			BuyDataBeans deliverydetail = UserDao.deliverydetail(id);

			request.setAttribute("deliverydetail", deliverydetail);

			request.getRequestDispatcher(EcHelper.USER_BUY_HISTORY_DETAIL_PAGE).forward(request, response);


	}
}
