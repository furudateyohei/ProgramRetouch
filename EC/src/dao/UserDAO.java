package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import base.DBManager;
import beans.BuyDataBeans;
import beans.ItemDataBeans;
import beans.UserDataBeans;
import ec.EcHelper;

/**
 *
 * @author d-yamaguchi
 *
 */
public class UserDAO {
	// インスタンスオブジェクトを返却させてコードの簡略化
	public static UserDAO getInstance() {
		return new UserDAO();
	}

	/**
	 * データの挿入処理を行う。現在時刻は挿入直前に生成
	 *
	 * @param user
	 *            対応したデータを保持しているJavaBeans
	 * @throws SQLException
	 *             呼び出し元にcatchさせるためにスロー
	 */
	public void insertUser(UserDataBeans udb) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();
			st = con.prepareStatement("INSERT INTO t_user(name,login_id,address,login_password,create_date) VALUES(?,?,?,?,?)");
			st.setString(1, udb.getName());
			st.setString(2, udb.getLoginId());
			st.setString(3, udb.getAddress());
			st.setString(4, EcHelper.getSha256(udb.getPassword()));
			st.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			st.executeUpdate();
			System.out.println("inserting user has been completed");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	/**
	 * ユーザーIDを取得
	 *
	 * @param loginId
	 *            ログインID
	 * @param password
	 *            パスワード
	 * @return int ログインIDとパスワードが正しい場合対象のユーザーID 正しくない||登録されていない場合0
	 * @throws SQLException
	 *             呼び出し元にスロー
	 */
	public static int getUserId(String loginId, String password) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();

			st = con.prepareStatement("SELECT * FROM t_user WHERE login_id = ?");
			st.setString(1, loginId);

			ResultSet rs = st.executeQuery();

			int userId = 0;
			while (rs.next()) {
				if (EcHelper.getSha256(password).equals(rs.getString("login_password"))) {
					userId = rs.getInt("id");
					System.out.println("login succeeded");
					break;
				}
			}

			System.out.println("searching userId by loginId has been completed");
			return userId;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	/**
	 * ユーザーIDからユーザー情報を取得する
	 *
	 * @param useId
	 *            ユーザーID
	 * @return udbList 引数から受け取った値に対応するデータを格納する
	 * @throws SQLException
	 *             呼び出し元にcatchさせるためスロー
	 */
	public static UserDataBeans getUserDataBeansByUserId(int userId) throws SQLException {
		UserDataBeans udb = new UserDataBeans();
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();
			st = con.prepareStatement("SELECT id,name, login_id, address FROM t_user WHERE id =" + userId);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				udb.setId(rs.getInt("id"));
				udb.setName(rs.getString("name"));
				udb.setLoginId(rs.getString("login_id"));
				udb.setAddress(rs.getString("address"));
			}

			st.close();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}

		System.out.println("searching UserDataBeans by userId has been completed");
		return udb;
	}

	/**
	 * ユーザー情報の更新処理を行う。
	 *
	 * @param user
	 *            対応したデータを保持しているJavaBeans
	 * @throws SQLException
	 *             呼び出し元にcatchさせるためにスロー
	 */
	public static void updateUser(UserDataBeans udb) throws SQLException {
		// 更新された情報をセットされたJavaBeansのリスト
		UserDataBeans updatedUdb = new UserDataBeans();
		Connection con = null;
		PreparedStatement st = null;

		try {

			con = DBManager.getConnection();
			st = con.prepareStatement("UPDATE t_user SET name=?, login_id=?, address=? WHERE id=?;");
			st.setString(1, udb.getName());
			st.setString(2, udb.getLoginId());
			st.setString(3, udb.getAddress());
			st.setInt(4, udb.getId());
			st.executeUpdate();
			System.out.println("update has been completed");

			st = con.prepareStatement("SELECT name, login_id, address FROM t_user WHERE id=" + udb.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				updatedUdb.setName(rs.getString("name"));
				updatedUdb.setLoginId(rs.getString("login_id"));
				updatedUdb.setAddress(rs.getString("address"));
			}

			st.close();
			System.out.println("searching updated-UserDataBeans has been completed");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	/**
	 * loginIdの重複チェック
	 *
	 * @param loginId
	 *            check対象のログインID
	 * @param userId
	 *            check対象から除外するuserID
	 * @return bool 重複している
	 * @throws SQLException
	 */
	public static boolean isOverlapLoginId(String loginId, int userId) throws SQLException {
		// 重複しているかどうか表す変数
		boolean isOverlap = false;
		Connection con = null;
		PreparedStatement st = null;

		try {
			con = DBManager.getConnection();
			// 入力されたlogin_idが存在するか調べる
			st = con.prepareStatement("SELECT login_id FROM t_user WHERE login_id = ? AND id != ?");
			st.setString(1, loginId);
			st.setInt(2, userId);
			ResultSet rs = st.executeQuery();

			System.out.println("searching loginId by inputLoginId has been completed");

			if (rs.next()) {
				isOverlap = true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}

		System.out.println("overlap check has been completed");
		return isOverlap;
	}

	public List<BuyDataBeans> buyhistory(int user_id){
		Connection con = null;
		PreparedStatement st = null;
		List<BuyDataBeans> buyhistory = new ArrayList <BuyDataBeans>();
		try {
			con = DBManager.getConnection();
			st = con.prepareStatement("SELECT t_buy.id, t_buy.create_date, t_buy.total_price, "
					+ "t_buy.delivery_method_id, m_delivery_method.name,m_delivery_method.price "
					+ "FROM t_buy INNER JOIN m_delivery_method "
					+ "ON t_buy.delivery_method_id = m_delivery_method.id "
					+ "WHERE t_buy.user_id = ? ");
			st.setInt(1,user_id);
			ResultSet rs = st.executeQuery();

			while(rs.next()) {
				int id = rs.getInt ("id");
				int total_price = rs.getInt("total_price");
				int delivery_method_id = rs.getInt("delivery_method_id");
				Date create_date = rs.getTimestamp("create_date");
				String name =rs.getString("name");

				BuyDataBeans user = new BuyDataBeans(id,total_price,delivery_method_id,create_date,name);

				buyhistory.add(user);
			}
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			if(con != null) {
				try {
					con.close();
				}catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}

			}
		return buyhistory;
		}

	public  BuyDataBeans getBuyDataBeansById(String id) {
		Connection con = null;
		try {
			con = DBManager.getConnection();
			String st = "SELECT t_buy.id, t_buy.create_date, t_buy.total_price, "
					+ "t_buy.delivery_method_id, m_delivery_method.name "
					+ "FROM t_buy INNER JOIN m_delivery_method "
					+ "ON t_buy.delivery_method_id = m_delivery_method.id "
					+ "WHERE t_buy.id = ? ";
			PreparedStatement pStmt = con.prepareStatement(st);
			pStmt.setString(1, id);
			ResultSet rs = pStmt.executeQuery();

			if(!rs.next()) {
            	return null;
            }

			int Buyid = rs.getInt ("id");
			int total_price = rs.getInt("total_price");
			int delivery_method_id = rs.getInt("delivery_method_id");
			Date create_date = rs.getTimestamp("create_date");
			String name =rs.getString("name");

			BuyDataBeans bdb = new BuyDataBeans(Buyid,total_price,delivery_method_id,create_date,name);
			return bdb;


		} catch(SQLException e) {
			e.printStackTrace();
			return null;

		}finally {
			if(con != null);{
				try {
					con.close();
				}catch (SQLException e) {
					e.printStackTrace();
					return null;
					}
				}
			}
		}
	public  List<ItemDataBeans> BuyDetailgetId(String id) {
		Connection con = null;
		PreparedStatement st = null;
		List<ItemDataBeans> BuyDetailgetId = new ArrayList <ItemDataBeans>();
		try {
			con = DBManager.getConnection();
			st = con.prepareStatement ("SELECT m_item.name,m_item.price "
					+  "FROM m_item "
					+  "INNER JOIN t_buy_detail "
					+  "ON m_item.id = t_buy_detail.item_id	"
					+  "WHERE t_buy_detail.buy_id = ? ");
			st.setString(1,id);
			ResultSet rs = st.executeQuery();

			while(rs.next())  {

			String name =rs.getString("name");
			int price = rs.getInt("price");

			ItemDataBeans item = new ItemDataBeans(name,price);

			BuyDetailgetId.add(item);
			}

		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			if(con != null) {
				try {
					con.close();
				}catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}

			}
		return BuyDetailgetId;
		}
	public  BuyDataBeans deliverydetail(String id) {
		Connection con = null;
		try {
			con = DBManager.getConnection();
			String st = "SELECT m_delivery_method.name,m_delivery_method.price "
					+ "FROM m_delivery_method INNER JOIN t_buy "
					+ "ON m_delivery_method.id = t_buy.delivery_method_id "
					+ "WHERE t_buy.id = ? ";
			PreparedStatement pStmt = con.prepareStatement(st);
			pStmt.setString(1, id);
			ResultSet rs = pStmt.executeQuery();

			if(!rs.next()) {
            	return null;
            }

			int price = rs.getInt("price");
			String name =rs.getString("name");

			BuyDataBeans deliverydetail = new BuyDataBeans(price,name);
			return deliverydetail;


		} catch(SQLException e) {
			e.printStackTrace();
			return null;

		}finally {
			if(con != null);{
				try {
					con.close();
				}catch (SQLException e) {
					e.printStackTrace();
					return null;
					}
				}
			}
		}
	}