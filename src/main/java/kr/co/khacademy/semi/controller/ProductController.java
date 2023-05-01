package kr.co.khacademy.semi.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.khacademy.semi.dao.ProductDao;
import kr.co.khacademy.semi.model.Product;

@WebServlet("/product/*")
public class ProductController extends HttpServlet {

    private static final ProductDao productDao = ProductDao.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            if ("/register".equals(pathInfo)) {
                resp.sendRedirect("/WEB-INF/views/product/register.jsp");
            } else if ("/list".equals(pathInfo)) {
                List<Product> products = productDao.read();
                req.setAttribute("products", products);
                req.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(req, resp);
            } else if ("/item".equals(pathInfo)) {
                Long id = Long.parseLong(req.getParameter("id"));
                Product product = productDao.read(id);
                req.setAttribute("product", product);
                req.getRequestDispatcher("/WEB-INF/views/product/item.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            resp.sendRedirect("/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            if ("/register".equals(pathInfo)) {
                Product product = Product.of(req);
                productDao.create(product);    
                resp.sendRedirect("/product/list") ;
            } else if ("/modify".equals(pathInfo)) {
                Product product = Product.of(req);
                productDao.update(product) ;
                String location = String.format("/product/item?id=%d", product.getId());
                resp.sendRedirect(location);
            } else if ("/delete".equals(pathInfo)) {
                Long id = Long.parseLong(req.getParameter("id"));
                productDao.delete(id);
                resp.sendRedirect("/product/list");
            }
        } catch (SQLException e) {
            resp.sendRedirect("/error");
        }
    }
}