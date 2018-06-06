package beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyDataBeans  implements Serializable {
	private int id;
	private int userId;
	private int totalPrice;
	private int deliveryMethodId;
	private Date buyDate;

	private String deliveryMethodName;
	private int deliveryMethodPrice;

	public BuyDataBeans() {

	}

	public BuyDataBeans(int id, int userId, int totalPrice, int deliveryMethodId, Date buyDate) {
		this.id = id;
		this.userId = userId;
		this.totalPrice = totalPrice;
		this.deliveryMethodId = deliveryMethodId;
		this.buyDate = buyDate;
	}

	public BuyDataBeans(int id,int totalPrice,int deliveryMethodId, Date buyDate,String deliveryMethodName) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.deliveryMethodId = deliveryMethodId;
		this.buyDate = buyDate;
		this.deliveryMethodName = deliveryMethodName;
	}

	public BuyDataBeans(String id,int totalPrice,int deliveryMethodId, Date buyDate,String deliveryMethodName) {
		this.id = Integer.parseInt(id);
		this.deliveryMethodId = deliveryMethodId;
		this.buyDate = buyDate;
		this.totalPrice = totalPrice;
		this.deliveryMethodName = deliveryMethodName;
	}

	public BuyDataBeans(int deliveryMethodPrice,String deliveryMethodName) {
		this.deliveryMethodPrice = deliveryMethodPrice;
		this.deliveryMethodName = deliveryMethodName;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}


	public int getDeliveryMethodId() {
		return deliveryMethodId;
	}
	public void setDeliveryMethodId(int deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}
	public Date getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}
	public String getDeliveryMethodName() {
		return deliveryMethodName;
	}
	public void setDeliveryMethodName(String deliveryMethodName) {
		this.deliveryMethodName = deliveryMethodName;
	}

	public String getFormatDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH時mm分");
		return sdf.format(buyDate);
	}
	public int getDeliveryMethodPrice() {
		return deliveryMethodPrice;
	}
	public void setDeliveryMethodPrice(int deliveryMethodPrice) {
		this.deliveryMethodPrice = deliveryMethodPrice;
	}


}
