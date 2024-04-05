package dbclpm.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dbclpm.dto.ThongKeDTO;
import dbclpm.entity.HoaDon;
import dbclpm.entity.LuongDienTieuThu;
import dbclpm.entity.Nam;
import dbclpm.entity.Thang;
import dbclpm.repository.HoaDonRepo;
import dbclpm.repository.LuongDienTieuThuRepo;
import dbclpm.repository.NamRepo;
import dbclpm.repository.ThangRepo;
import dbclpm.ultilities.tien_dien.TienDienUltility;

@RestController
@CrossOrigin
public class ListMonitorController {

	private final LuongDienTieuThuRepo luongDienTieuThuRepo;
	private final HoaDonRepo hoaDonRepo;
	private final ThangRepo thangRepo;
	private final NamRepo namRepo;
	private final TienDienUltility tienDienUltility;

	public ListMonitorController(LuongDienTieuThuRepo luongDienTieuThuRepo, ThangRepo thangRepo, NamRepo namRepo,
			HoaDonRepo hoaDonRepo) {
		this.luongDienTieuThuRepo = luongDienTieuThuRepo;
		this.hoaDonRepo = hoaDonRepo;
		this.thangRepo = thangRepo;
		this.namRepo = namRepo;
	}

	@PostMapping("api/list")
	public ResponseEntity<List<ThongKeDTO>> getThongKe(@RequestBody HashMap<String, Integer> requestParams) {
		/*
		 * TODO: - Xử lý case tỉnh ko có huyện, huyện ko có xã...
		 */
		long tinhId = requestParams.get("tinhId");
		long huyenId = requestParams.get("huyenId");
		long xaId = requestParams.get("xaId");
		long namId = requestParams.get("tinhId");
		long thangId = requestParams.get("thangId");

		Thang thang = thangRepo.findById(thangId).orElse(null);
		Nam nam = namRepo.findById(namId).orElse(null);
		LocalDate currentTime = LocalDate.now();
		
		if (thang == null || nam == null) {
			return null;
		}
			
		List<ThongKeDTO> dsThongKe = new ArrayList<>();

		// Nếu lấy danh sách thống kê trong quá khứ => đã có hóa đơn
		if (currentTime.getMonthValue() > Integer.valueOf(thang.getName())
				&& currentTime.getYear() >= Integer.valueOf(nam.getName())) {
			List<HoaDon> dsHoaDon = null;
			if (xaId != -1) {
				dsHoaDon = hoaDonRepo.findByKhachHangXaIdAndLuongDienTieuThuThangId(xaId, thangId);
			} else {
				if (huyenId != -1) {
					dsHoaDon = hoaDonRepo.findByKhachHangXaHuyenIdAndLuongDienTieuThuThangId(huyenId, thangId);
				} else {
					dsHoaDon = hoaDonRepo.findByKhachHangXaHuyenTinhIdAndLuongDienTieuThuThangId(tinhId, thangId);
				}
			}
			for (HoaDon hoaDon : dsHoaDon) {
				dsThongKe.add(new ThongKeDTO(hoaDon.getLuongDienTieuThu(), hoaDon));
			}
		} else {
			System.out.print("Ko co hoa don");	
			List<LuongDienTieuThu> dsLuongDienTieuThu = null;
			if (xaId != -1) {
				dsLuongDienTieuThu = luongDienTieuThuRepo.findByKhachHangXaIdAndThangId(xaId, thangId);
			} else {
				if (huyenId != -1) {
					dsLuongDienTieuThu = luongDienTieuThuRepo.findByKhachHangXaHuyenIdAndThangId(huyenId, thangId);
				} else {
					dsLuongDienTieuThu = luongDienTieuThuRepo.findByKhachHangXaHuyenTinhIdAndThangId(tinhId, thangId);
				}
			}
			for (LuongDienTieuThu luongDienTieuThu : dsLuongDienTieuThu) {
				dsThongKe.add(new ThongKeDTO(luongDienTieuThu, null));
			}
		}

		return ResponseEntity.ok(dsThongKe);
	}

}
