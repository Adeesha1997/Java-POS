package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.custom.ProductDao;
import com.devstack.pos.db.DbConnection;
import com.devstack.pos.entity.Product;
import com.devstack.pos.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public boolean saveProduct(Product product) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO product VALUES (?,?)";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, product.getCode());
        preparedStatement.setString(2, product.getDescription());

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean updateProduct(Product product) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE product SET description=? WHERE code=?";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, product.getDescription());
        preparedStatement.setInt(2, product.getCode());

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean deleteProduct(int code) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM product WHERE code=?";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, code);
        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public Product findProduct(int code) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM product WHERE code=?";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setInt(1, code);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new Product(
                    resultSet.getInt(1),
                    resultSet.getString(2)
            );
        }
        return null;
    }

    @Override
    public List<Product> findAllProduct() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM product";
        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sql);

        List<Product> productList = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            productList.add(new Product(
                    resultSet.getInt(1),
                    resultSet.getString(2)
            ));
        }
        return productList;
    }
}
