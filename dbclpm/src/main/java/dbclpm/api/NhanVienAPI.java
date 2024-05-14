package dbclpm.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dbclpm.entity.KhachHang;
import dbclpm.entity.NhanVien;
import dbclpm.repository.KhachHangRepo;
import dbclpm.repository.NhanVienRepo;
import jakarta.persistence.EntityManager;

@RestController
@CrossOrigin
@RequestMapping("/nhan-vien")
public class NhanVienAPI {
	@Autowired
	private NhanVienRepo nhanVienRepo;
	@Autowired
	private EntityManager entityManager;

	@GetMapping("/api/nhan-vien")
	public ResponseEntity<List<NhanVien>> getDsNhanVien() {
		List<NhanVien> ds = nhanVienRepo.findAll();

		return ResponseEntity.ok(ds);
	}
}
