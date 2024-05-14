package dbclpm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dbclpm.entity.BacDien;
import dbclpm.entity.NhanVien;

public interface NhanVienRepo extends JpaRepository<NhanVien, Long>{
	List<NhanVien> findAll();
}
