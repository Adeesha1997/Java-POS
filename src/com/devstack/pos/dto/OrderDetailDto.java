package com.devstack.pos.dto;

import com.devstack.pos.entity.SuperEntity;

import java.util.Date;
import java.util.List;

public class OrderDetailDto implements SuperEntity {
    private int code;
    private Date issueDate;
    private double totalCost;
    private String customerEmail;
    private double discount;
    private String operatorEmail;

    private List<ItemDetailDto> itemDetailDto;
    public OrderDetailDto() {
    }

    public OrderDetailDto(int code, Date issueDate, double totalCost, String customerEmail, double discount, String operatorEmail, List<ItemDetailDto> itemDetailDto) {
        this.code = code;
        this.issueDate = issueDate;
        this.totalCost = totalCost;
        this.customerEmail = customerEmail;
        this.discount = discount;
        this.operatorEmail = operatorEmail;
        this.itemDetailDto = itemDetailDto;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getOperatorEmail() {
        return operatorEmail;
    }

    public void setOperatorEmail(String operatorEmail) {
        this.operatorEmail = operatorEmail;
    }

    public List<ItemDetailDto> getItemDetailDto() {
        return itemDetailDto;
    }

    public void setItemDetailDto(List<ItemDetailDto> itemDetailDto) {
        this.itemDetailDto = itemDetailDto;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "code=" + code +
                ", issueDate=" + issueDate +
                ", totalCost=" + totalCost +
                ", customerEmail='" + customerEmail + '\'' +
                ", discount=" + discount +
                ", operatorEmail='" + operatorEmail + '\'' +
                ", itemDetailDto=" + itemDetailDto +
                '}';
    }
}
