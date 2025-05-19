package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.OrderDetailDao;
import com.devstack.pos.entity.OrderDetails;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailDaoImpl implements OrderDetailDao {
    @Override
    public boolean save(OrderDetails o) throws SQLException, ClassNotFoundException {
       return CrudUtil.execute("INSERT INTO order_detail VALUES(?,?,?,?,?,?)",
                o.getCode(),o.getIssueDate(),o.getTotalCost(),o.getCustomerEmail(),o.getDiscount(),o.getOperatorEmail());
    }

    @Override
    public boolean update(OrderDetails orderDetails) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetails find(Integer integer) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<OrderDetails> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }
}
