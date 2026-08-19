package com.myproject.AgritradeHub.Controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.AgritradeHub.Model.ProductCategory;
import com.myproject.AgritradeHub.Model.Products;
import com.myproject.AgritradeHub.Model.Products.ProductStatus;
import com.myproject.AgritradeHub.Model.Users;
import com.myproject.AgritradeHub.Model.Users.userRole;
import com.myproject.AgritradeHub.Repository.AddCategoryRepository;
import com.myproject.AgritradeHub.Repository.EnquiryRepository;
import com.myproject.AgritradeHub.Repository.OrderRepository;
import com.myproject.AgritradeHub.Repository.PaymentRepository;
import com.myproject.AgritradeHub.Repository.ProductRepository;
import com.myproject.AgritradeHub.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Farmer")
public class FarmerController {

	@Autowired
	private EnquiryRepository enquiryRepo;

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private PaymentRepository paymentRepo;
	
	@Autowired
	private AddCategoryRepository categoryRepo;

	@Autowired
	private HttpSession session;

	@GetMapping("/FarmerDashboard")
	public String ShowFarmerDashboard() {
		if (session.getAttribute("loggedInFarmer") == null) {
			return "redirect:/FarmerLogin";
		}

		return "Farmer/FarmerDashboard";
	}

	

	@GetMapping("/AddProduct")
	public String showAddProduct(Model model) {
		if (session.getAttribute("loggedInFarmer") == null) {
			return "redirect:/AddProduct";
		}

		List<ProductCategory> categories = categoryRepo.findAll();
		model.addAttribute("categories", categories);
		Products product = new Products();
		model.addAttribute("product", product);
		return "Farmer/AddProduct";
	}

	@PostMapping("/AddProduct")
	public String AddProduct(@ModelAttribute("products") Products product, @RequestParam("Image") MultipartFile file,
			RedirectAttributes attributes) {
		try {
			String storageFileName = "Product_Image_" + System.currentTimeMillis() + file.getOriginalFilename();
			String uploadDir = "Public/ProductImage/";
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
			}
			Users farmer = (Users) session.getAttribute("loggedInFarmer");

			product.setFarmer(farmer);
			product.setProductImage(storageFileName);
			product.setStatus(ProductStatus.AVAILABLE);

			productRepo.save(product);
			attributes.addFlashAttribute("msg", "Product Successfully Added!");

			return "redirect:/Farmer/AddProduct";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			System.err.println("Error :" + e.getMessage());
			return "redirect:/Farmer/AddProduct";
		}

	}
	
	
	
	@GetMapping("/ChangePassword")
	public String ShowChangePassword() {
		if (session.getAttribute("loggedInFarmer") == null) {
			return "redirect:/ChangePassword";
		}
		return "Farmer/ChangePassword";
	}

	@PostMapping("/ChangePassword")
	public String ChangePassword(HttpServletRequest request, RedirectAttributes attributes) {
		try {
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confirmPass = request.getParameter("confirmPassword");

			if (!newPass.equals(confirmPass)) {
				attributes.addFlashAttribute("msg", "New Password And confirm Password Are Not same!");
				return "redirect:/Farmer/ChangePassword";
			}
			Users farmer = (Users) session.getAttribute("loggedInFarmer");

			if (oldPass.equals(farmer.getPassword())) {
				farmer.setPassword(confirmPass);
				userRepo.save(farmer);
				session.removeAttribute("loggedInFarmer");
				attributes.addFlashAttribute("msg", "Password Change Succsfully");
				return "redirect:/FarmerLogin";

			} else {
				attributes.addFlashAttribute("msg", "Invalid old Password!");
			}

			return "redirect:/Farmer/ChangePassword";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error :" + e.getMessage());
			return "redirect:/Farmer/ChangePassword";
		}
	}

	// logout
		@GetMapping("/logout")
		public String logout(RedirectAttributes attributes) {
			session.removeAttribute("loggedinFarmer");
			attributes.addFlashAttribute("msg", "Logged Out Successfully!");
			return "redirect:/FarmerLogin";
		}
}