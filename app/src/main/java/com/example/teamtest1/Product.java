package com.example.teamtest1;

public class Product {

    private String title;
    private String detail;
    private String date;
    private String deadline;
    private String price;
    private String category;
    private String status;
    private String image;
    private String seller;
    private String buyer;
    private String pid;
    private String bidder;
    private int bidCount;



    private String estiStatus; // 평가 상태



    private int bid;

    private String unique;


    private int count;

    public Product(){}


    public Product(String title, String detail, String price, int bid, String image,
                   int count,String unique,String date,String deadline,String seller,
                   String status,String estiStatus,String bidder,String category,int bidCount) {
        this.title = title;
        this.detail = detail;
        this.price = price;
        this.bid = bid;
        this.count = count;
        this.image = image;
        this.unique= unique;
        this.date = date;
        this.deadline = deadline;
        this.seller = seller;
        this.status = status;
        this.estiStatus = estiStatus;
        this.bidder = bidder;
        this.category = category;
        this.bidCount = bidCount;
    }

    public int getBidCount() {
        return bidCount;
    }

    public void setBidCount(int bidCount) {
        this.bidCount = bidCount;
    }
    public String getBidder() {
        return bidder;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

    public String getEstiStatus() {
        return estiStatus;
    }

    public void setEstiStatus(String estiStatus) {
        this.estiStatus = estiStatus;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

//    public String getPid() {
//        return pid;
//    }
//
//    public void setPid(String pid) {
//        this.pid = pid;
//    }
}
