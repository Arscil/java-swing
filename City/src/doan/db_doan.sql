
CREATE TABLE ChuThai (
    MaChuThai      NUMBER PRIMARY KEY,
    HoTen          VARCHAR2(100),
    DiaChi         VARCHAR2(200),
    Sdt            VARCHAR2(15),
    Email          VARCHAR2(100),
    LoaiChuThai    VARCHAR2(50),
    Username VARCHAR2(50) UNIQUE,
    Password VARCHAR2(255)
);

CREATE TABLE DonViThuGom (
    MaDv           NUMBER PRIMARY KEY,
    TenDv          VARCHAR2(100),
    KhuVucPhuTrach VARCHAR2(100)
);
CREATE TABLE NhanVienThuGom (
    MaNvtg         NUMBER PRIMARY KEY,
    MaDv           NUMBER,
    TenNvtg        VARCHAR2(100),
    GioiTinh       VARCHAR2(10),
    Sdt            VARCHAR2(15),
    MaTruongNhom   NUMBER,
    CONSTRAINT FkTruongNhom FOREIGN KEY (MaTruongNhom) 
        REFERENCES NhanVienThuGom (MaNvtg),
    Username VARCHAR2(50) UNIQUE,
    Password VARCHAR2(255)
);
CREATE TABLE NhanVienDieuPhoi (
    MaNvdp         NUMBER PRIMARY KEY,
    TenNvdp        VARCHAR2(100),
    Username       VARCHAR2(50) UNIQUE,
    Password       VARCHAR2(255),
    GioiTinh       VARCHAR2(10),
    Sdt            VARCHAR2(15),
    VaiTro         VARCHAR2(50)
);

CREATE TABLE HopDong (
    MaHopDong      NUMBER PRIMARY KEY,
    MaChuThai      NUMBER,
    LoaiHopDong    VARCHAR2(50),
    NgBatDau       DATE,
    NgKetThuc      DATE,
    DiaChiThuGom  VARCHAR2(200),
    MoTa           CLOB,
    TrangThai      VARCHAR2(50)
);
CREATE TABLE DichVu(
    MaDichVu      NUMBER PRIMARY KEY,
    TenDichVu     VARCHAR2(100),
    DonViTinh     VARCHAR2(50),
    DonGia        NUMBER(15, 2)
);
CREATE TABLE ChiTietHopDong (
    MaHopDong     NUMBER,
    MaDichVu      NUMBER,
    KhoiLuong     NUMBER,
    ThanhTien     NUMBER,
    GhiChu        VARCHAR2(255),
    PRIMARY KEY (MaHopDong, MaDichVu)
);
-- DROP TABLE HOADON
CREATE TABLE HoaDon (
    MaHoaDon       NUMBER PRIMARY KEY,
    MaHopDong      NUMBER,
    MaNvdp         NUMBER,
    NgLap          DATE,
    SoTien         NUMBER(15, 2),
    TinhTrang      VARCHAR2(50)
);
CREATE TABLE LichThuGom (
    MaLich         NUMBER PRIMARY KEY,
    MaNvdp         NUMBER,
    MaTuyen        NUMBER,
    NgThu          VARCHAR2(2),
    GioThu         VARCHAR2(5),
    TrangThai      VARCHAR2(50)
);

CREATE TABLE YeuCauDatLich (
    MaYc           NUMBER PRIMARY KEY,
    MaChuThai      NUMBER,
    MaLich         NUMBER,
    ThoiGianYc     DATE,
    GhiChu         CLOB,
    TrangThai      VARCHAR2(50)
);

CREATE TABLE PhanCong (
    MaPC           NUMBER PRIMARY KEY,
    MaNvdp         NUMBER,
    MaLich         NUMBER,
    MaNvtg         NUMBER
);
CREATE TABLE ThongKePhanCong (
    MaNvtg NUMBER PRIMARY KEY,
    SoTuyenDaNhan NUMBER,
    CONSTRAINT FK_TKPC_NVTG FOREIGN KEY (MaNvtg)
        REFERENCES NhanVienThuGom(MaNvtg)
);
CREATE TABLE Quan(
    MaQuan NUMBER PRIMARY KEY,
    TenQuan VARCHAR2(50)
);
CREATE TABLE TuyenDuongThuGom (
    MaTuyen        NUMBER PRIMARY KEY,
    MaDv           NUMBER,
    TenTuyen       VARCHAR2(100),
    KhuVuc         NUMBER REFERENCES Quan(MaQuan)
);

CREATE TABLE ChamCong (
    MaCc           NUMBER PRIMARY KEY,
    MaNvdp         NUMBER,
    MaNvtg         NUMBER,
    NgayCong       DATE,
    GhiChu         VARCHAR2(255),
    TrangThai      VARCHAR2(20)
);
CREATE TABLE PhanAnh (
    MaPA           NUMBER PRIMARY KEY,
    MaChuThai      NUMBER,
    NoiDung        CLOB NOT NULL,
    ThoiGianGui    DATE DEFAULT SYSDATE,
    TrangThai      VARCHAR2(50)
);

CREATE TABLE TtPhanLoaiRac (
    MaLoaiRac      NUMBER PRIMARY KEY,
    TenLoaiRac     VARCHAR2(100),
    MoTa           VARCHAR2(255),
    HdPhanLoai     VARCHAR2(100),
    HinhAnh        BLOB
);

-- Ràng buộc khóa ngoại
ALTER TABLE YeuCauDatLich 
ADD CONSTRAINT FkYcChuThai FOREIGN KEY (MaChuThai) REFERENCES ChuThai(MaChuThai);
ALTER TABLE YeuCauDatLich 
ADD CONSTRAINT FkYcLich FOREIGN KEY (MaLich) REFERENCES LichThuGom(MaLich);

ALTER TABLE ChiTietHopDong
ADD CONSTRAINT fkCTHD  FOREIGN KEY (MaHopDong) REFERENCES HopDong(MaHopDong);
ALTER TABLE ChiTietHopDong
ADD CONSTRAINT fkCTHD_DV  FOREIGN KEY (MaDichVu) REFERENCES DichVu (MaDichVu);

ALTER TABLE HopDong
ADD CONSTRAINT fkHdChuThai FOREIGN KEY (MaChuThai) REFERENCES ChuThai(MaChuThai);

ALTER TABLE LichThuGom 
ADD CONSTRAINT FkLichNvdp FOREIGN KEY (MaNvdp) REFERENCES NhanVienDieuPhoi(MaNvdp);
ALTER TABLE LichThuGom
ADD CONSTRAINT FkLichTuyen FOREIGN KEY (MaTuyen) REFERENCES TuyenDuongThuGom(MaTuyen);

ALTER TABLE TuyenDuongThuGom 
ADD CONSTRAINT FkTuyenDv FOREIGN KEY (MaDv) REFERENCES DonViThuGom(MaDv);

ALTER TABLE NhanVienThuGom 
ADD CONSTRAINT FkNvtgDv FOREIGN KEY (MaDv) REFERENCES DonViThuGom(MaDv);

ALTER TABLE HoaDon 
ADD CONSTRAINT FkHdHopDong FOREIGN KEY (MaHopDong) REFERENCES HopDong(MaHopDong);
ALTER TABLE HoaDon 
ADD CONSTRAINT FkHdNvdp FOREIGN KEY (MaNvdp) REFERENCES NhanVienDieuPhoi(MaNvdp);

ALTER TABLE PhanCong 
ADD CONSTRAINT FkPcNvdp FOREIGN KEY (MaNvdp) REFERENCES NhanVienDieuPhoi(MaNvdp);
ALTER TABLE PhanCong 
ADD CONSTRAINT FkPcLich FOREIGN KEY (MaLich) REFERENCES LichThuGom(MaLich);
ALTER TABLE PhanCong 
ADD CONSTRAINT FkPcNvtg FOREIGN KEY (MaNvtg) REFERENCES NhanVienThuGom(MaNvtg);

ALTER TABLE ChamCong 
ADD CONSTRAINT FkCcNvdp FOREIGN KEY (MaNvdp) REFERENCES NhanVienDieuPhoi(MaNvdp);
ALTER TABLE ChamCong 
ADD CONSTRAINT FkCcNvtg FOREIGN KEY (MaNvtg) REFERENCES NhanVienThuGom(MaNvtg);

ALTER TABLE PhanAnh
ADD CONSTRAINT FkPaChuThai FOREIGN KEY (MaChuThai) REFERENCES ChuThai(MaChuThai);

-- Tạo seqence cho mã: 
-- Mã chủ thải
CREATE SEQUENCE SeqChuThai START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgChuThai
BEFORE INSERT ON ChuThai
FOR EACH ROW
BEGIN
    SELECT SeqChuThai.NEXTVAL INTO :NEW.MaChuThai FROM dual;
END;
/

-- Mã đơn vị thu gom
CREATE SEQUENCE SeqDonViThuGom START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgDonViThuGom
BEFORE INSERT ON DonViThuGom
FOR EACH ROW
BEGIN
    SELECT SeqDonViThuGom.NEXTVAL INTO :NEW.MaDv FROM dual;
END;
/
-- Mã nhân viên thu gom
CREATE SEQUENCE SeqNvtg START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgNvtg
BEFORE INSERT ON NhanVienThuGom
FOR EACH ROW
BEGIN
    SELECT SeqNvtg.NEXTVAL INTO :NEW.MaNvtg FROM dual;
END;
/
--Mã hóa đơn
CREATE SEQUENCE SeqHoaDon START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgHoaDon
BEFORE INSERT ON HoaDon
FOR EACH ROW
BEGIN
    SELECT SeqHoaDon.NEXTVAL INTO :NEW.MaHoaDon FROM dual;
END;
/

-- Mã nhân viên điều phối
CREATE SEQUENCE SeqNvdp START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgNvdp
BEFORE INSERT ON NhanVienDieuPhoi
FOR EACH ROW    
BEGIN
    SELECT SeqNvdp.NEXTVAL INTO :NEW.MaNvdp FROM dual;
END;
/

-- Mã hợp đồng
CREATE SEQUENCE SeqHopDong START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgHopDong
BEFORE INSERT ON HopDong
FOR EACH ROW
BEGIN
    SELECT SeqHopDong.NEXTVAL INTO :NEW.MaHopDong FROM dual;
END;
/
-- Mã dịch vụ
CREATE SEQUENCE SeqDichVu START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgDichVu
BEFORE INSERT ON DichVu
FOR EACH ROW
BEGIN
    SELECT SeqDichVu.NEXTVAL INTO :NEW.MaDichVu FROM dual;
END;
/
-- Mã lịch thu gom
CREATE SEQUENCE SeqLichThuGom START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgLichThuGom
BEFORE INSERT ON LichThuGom
FOR EACH ROW
BEGIN
    SELECT SeqLichThuGom.NEXTVAL INTO :NEW.MaLich FROM dual;
END;
/

-- Mã yêu cầu đặt lịch
CREATE SEQUENCE SeqYeuCauDatLich START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgYeuCauDatLich
BEFORE INSERT ON YeuCauDatLich
FOR EACH ROW
BEGIN
    SELECT SeqYeuCauDatLich.NEXTVAL INTO :NEW.MaYc FROM dual;
END;
/

-- Mã phân công
CREATE SEQUENCE SeqPhanCong START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgPhanCong
BEFORE INSERT ON PhanCong
FOR EACH ROW
BEGIN
    SELECT SeqPhanCong.NEXTVAL INTO :NEW.MaPc FROM dual;
END;
/

-- Mã tuyến đường
CREATE SEQUENCE SeqTuyen START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgTuyen
BEFORE INSERT ON TuyenDuongThuGom
FOR EACH ROW
BEGIN
    SELECT SeqTuyen.NEXTVAL INTO :NEW.MaTuyen FROM dual;
END;
/

-- Mã chấm công
CREATE SEQUENCE SeqChamCong START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgChamCong
BEFORE INSERT ON ChamCong
FOR EACH ROW
BEGIN
    SELECT SeqChamCong.NEXTVAL INTO :NEW.MaCc FROM dual;
END;
/

-- Mã thông tin loại rác
CREATE SEQUENCE SeqLoaiRac START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgLoaiRac
BEFORE INSERT ON TtPhanLoaiRac
FOR EACH ROW
BEGIN
    SELECT SeqLoaiRac.NEXTVAL INTO :NEW.MaLoaiRac FROM dual;
END;
/

-- Mã phản ánh
CREATE SEQUENCE SeqPhanAnh START WITH 1 INCREMENT BY 1;
CREATE OR REPLACE TRIGGER TrgPhanAnh
BEFORE INSERT ON PhanAnh
FOR EACH ROW
BEGIN
    SELECT SeqPhanAnh.NEXTVAL INTO :NEW.MaPa FROM dual;
END;
/
-- ========= TRIGGER ============
--MaTruongNhom phải thuộc cùng đơn vị với nhân viên
CREATE OR REPLACE TRIGGER trg_check_truong_nhom_cung_don_vi
BEFORE INSERT OR UPDATE ON NhanVienThuGom
FOR EACH ROW
WHEN (NEW.MaTruongNhom IS NOT NULL)
DECLARE
    v_MaDvTruongNhom NUMBER;
BEGIN
    
    SELECT MaDv INTO v_MaDvTruongNhom
    FROM NhanVienThuGom
    WHERE MaNvtg = :NEW.MaTruongNhom;

   
    IF v_MaDvTruongNhom != :NEW.MaDv THEN
        RAISE_APPLICATION_ERROR(-20001, 'Trưởng nhóm phải cùng đơn vị thu gom với nhân viên.');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20002, 'Mã trưởng nhóm không tồn tại.');
END;
/
--Không cho phép tạo hợp đồng có ngày kết thúc nhỏ hơn ngày bắt đầu
CREATE OR REPLACE TRIGGER Trg_KiemTraNgayHopDong
BEFORE INSERT OR UPDATE ON HopDong
FOR EACH ROW
BEGIN
    IF :NEW.NgKetThuc < :NEW.NgBatDau THEN
        RAISE_APPLICATION_ERROR(-20003, 'Ngày kết thúc phải sau ngày bắt đầu của hợp đồng.');
    END IF;
END;
/
-- Không cho phép một NhanVienThuGom bị phân công 2 lần vào cùng một lịch
CREATE OR REPLACE TRIGGER Trg_KiemTraTrungLichPhanCong
FOR INSERT OR UPDATE ON PhanCong
COMPOUND TRIGGER

    TYPE PhanCongRec IS RECORD (
        MaPc   PhanCong.MaPc%TYPE,
        MaNvtg PhanCong.MaNvtg%TYPE,
        MaLich PhanCong.MaLich%TYPE
    );

    TYPE PhanCongTab IS TABLE OF PhanCongRec;
    v_data PhanCongTab := PhanCongTab();

AFTER EACH ROW IS
BEGIN
    -- Lưu lại các bản ghi vừa insert/update vào biến tạm
    v_data.EXTEND;
    v_data(v_data.LAST).MaPc := :NEW.MaPc;
    v_data(v_data.LAST).MaNvtg := :NEW.MaNvtg;
    v_data(v_data.LAST).MaLich := :NEW.MaLich;
END AFTER EACH ROW;

AFTER STATEMENT IS
    v_count NUMBER;
BEGIN
    FOR i IN 1 .. v_data.COUNT LOOP
        SELECT COUNT(*) INTO v_count
        FROM PhanCong
        WHERE MaNvtg = v_data(i).MaNvtg
          AND MaLich = v_data(i).MaLich
          AND MaPc != v_data(i).MaPc;

        IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20004,
                'Nhân viên thu gom đã được phân công cho lịch này (trùng MaLich = ' || v_data(i).MaLich || ').');
        END IF;
    END LOOP;
END AFTER STATEMENT;

END Trg_KiemTraTrungLichPhanCong;
/


--Không cho phép nhập ngày công chấm công lớn hơn ngày hiện tại
CREATE OR REPLACE TRIGGER Trg_KiemTraNgayChamCong
BEFORE INSERT OR UPDATE ON ChamCong
FOR EACH ROW
BEGIN
    IF :NEW.NgayCong > TRUNC(SYSDATE) THEN
        RAISE_APPLICATION_ERROR(-20010, 'Ngày chấm công không được vượt quá ngày hiện tại.');
    END IF;
END;
/
--Ngày lập hóa đơn phải từ ngày bắt đầu đến trước ngày kết thúc hợp đồng.
CREATE OR REPLACE TRIGGER trg_check_ngaylap_hoadon
BEFORE INSERT OR UPDATE ON HoaDon
FOR EACH ROW
DECLARE
    v_ngbatdau  DATE;
    v_ngketthuc DATE;
BEGIN
   
    SELECT NgBatDau, NgKetThuc INTO v_ngbatdau, v_ngketthuc
    FROM HopDong
    WHERE MaHopDong = :NEW.MaHopDong;

    
    IF :NEW.NgLap < v_ngbatdau OR :NEW.NgLap >= v_ngketthuc THEN
        RAISE_APPLICATION_ERROR(-20002, 
            'Ngày lập hóa đơn phải từ ngày bắt đầu đến trước ngày kết thúc hợp đồng.');
    END IF;
END;
/
CREATE OR REPLACE TRIGGER trg_kiemtra_sua_ngay_hopdong
BEFORE UPDATE OF NgBatDau, NgKetThuc ON HopDong
FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
   
    SELECT COUNT(*) INTO v_count
    FROM HoaDon
    WHERE MaHopDong = :OLD.MaHopDong
      AND (NgLap < :NEW.NgBatDau OR NgLap >= :NEW.NgKetThuc);

    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20011,
            'Không thể cập nhật NgBatDau hoặc NgKetThuc vì đã tồn tại hóa đơn có NgLap nằm ngoài phạm vi mới.');
    END IF;
END;
/
-- Cập nhật tăng khi phân công thu gom ở quan hệ ThongKeThuGom
CREATE OR REPLACE TRIGGER Trg_LostUpdate_Insert
AFTER INSERT ON PhanCong
FOR EACH ROW
DECLARE
    v_sotuyen NUMBER;
BEGIN
    -- Đọc giá trị cũ
    SELECT SoTuyenDaNhan INTO v_sotuyen
    FROM ThongKePhanCong
    WHERE MaNvtg = :NEW.MaNvtg;

    -- Ghi lại dựa trên giá trị đọc được → dễ bị ghi đè
    UPDATE ThongKePhanCong
    SET SoTuyenDaNhan = v_sotuyen + 1
    WHERE MaNvtg = :NEW.MaNvtg;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        INSERT INTO ThongKePhanCong (MaNvtg, SoTuyenDaNhan)
        VALUES (:NEW.MaNvtg, 1);
END;
/

-- -- Cập nhật giảm khi phân công thu gom ở quan hệ ThongKeThuGom
CREATE OR REPLACE TRIGGER Trg_CapNhatThongKe_Delete
AFTER DELETE ON PhanCong
FOR EACH ROW
BEGIN
    -- Trừ số tuyến đi 1 khi xóa một phân công
    UPDATE ThongKePhanCong
    SET SoTuyenDaNhan = SoTuyenDaNhan - 1
    WHERE MaNvtg = :OLD.MaNvtg;
    
    -- Nếu sau khi trừ còn 0 → có thể xóa khỏi thống kê nếu muốn (tuỳ yêu cầu)
END;
/
-- Trigger cập nhật thành tiền cho bảng ChiTietHoaDon
CREATE OR REPLACE TRIGGER trg_tinh_thanhtien_cthd
AFTER INSERT ON ChiTietHopDong
DECLARE
BEGIN
    -- Cập nhật ThanhTien cho các dòng chưa có (NULL)
    UPDATE ChiTietHopDong cthd
    SET ThanhTien = (
        SELECT dv.DonGia * cthd.KhoiLuong
        FROM DichVu dv
        WHERE dv.MaDichVu = cthd.MaDichVu
    )
    WHERE ThanhTien IS NULL;
END;
/


-- Cập nhật số tiền khi tạo hóa đơn
CREATE OR REPLACE TRIGGER trg_tinh_sotien_hoadon
BEFORE INSERT ON HoaDon
FOR EACH ROW
DECLARE
    v_sotien NUMBER(15, 2);
BEGIN
    -- Tính tổng ThanhTien từ ChiTietHopDong tương ứng MaHopDong
    SELECT SUM(ThanhTien)
    INTO v_sotien
    FROM ChiTietHopDong
    WHERE MaHopDong = :NEW.MaHopDong;

    -- Gán vào cột SoTien của Hóa đơn
    :NEW.SoTien := NVL(v_sotien, 0);
END;
/

-- Thêm dữ liệu vào bảng ChuThai
INSERT INTO ChuThai (HoTen, DiaChi, Sdt, Email, LoaiChuThai, Username, Password) 
VALUES ('Nguyễn Văn An', '123 Nguyễn Huệ, Q1, TPHCM', '0901234567', 'nguyenvanan@email.com', 'Cá nhân', 'ct_user1', '123');

INSERT INTO ChuThai (HoTen, DiaChi, Sdt, Email, LoaiChuThai, Username, Password) 
VALUES ('Trần Thị Bình', '456 Lê Lợi, Q1, TPHCM', '0912345678', 'tranthib@email.com', 'Doanh nghiệp', 'ct_user2', '123');

INSERT INTO ChuThai (HoTen, DiaChi, Sdt, Email, LoaiChuThai, Username, Password) 
VALUES ('Phạm Văn Cảnh', '789 Hàm Nghi, Q1, TPHCM', '0923456789', 'phamvanc@email.com', 'Cá nhân', 'ct_user3', '123');

INSERT INTO ChuThai (HoTen, DiaChi, Sdt, Email, LoaiChuThai, Username, Password) 
VALUES ('Lê Thị Dung', '321 Lý Tự Trọng, Q1, TPHCM', '0934567890', 'lethid@email.com', 'Cá nhân', 'ct_user4', '123');

INSERT INTO ChuThai (HoTen, DiaChi, Sdt, Email, LoaiChuThai, Username, Password) 
VALUES ('Hoàng Văn Em', '654 Đồng Khởi, Q1, TPHCM', '0945678901', 'hoangvane@email.com', 'Doanh nghiệp', 'ct_user5', '123');


-- Thêm dữ liệu vào bảng DonViThuGom
INSERT INTO DonViThuGom (TenDv, KhuVucPhuTrach) VALUES ('Công ty TNHH Môi trường Xanh', 'Quận 1');
INSERT INTO DonViThuGom (TenDv, KhuVucPhuTrach) VALUES ('Công ty CP Vệ sinh Đô thị', 'Quận 2');
INSERT INTO DonViThuGom (TenDv, KhuVucPhuTrach) VALUES ('Xí nghiệp Môi trường Sạch', 'Quận 3');
INSERT INTO DonViThuGom (TenDv, KhuVucPhuTrach) VALUES ('Công ty TNHH Thu gom Rác', 'Quận 4');
INSERT INTO DonViThuGom (TenDv, KhuVucPhuTrach) VALUES ('Doanh nghiệp Vệ sinh Môi trường', 'Quận 5');


-- Thêm dữ liệu vào bảng NhanVienThuGom
INSERT INTO NhanVienThuGom (MaDv, TenNvtg, GioiTinh, Sdt, MaTruongNhom, Username, Password) 
VALUES (1, 'Nguyễn Văn Hùng', 'Nam', '0901111111', NULL, 'nvtg_user1', '123');

INSERT INTO NhanVienThuGom (MaDv, TenNvtg, GioiTinh, Sdt, MaTruongNhom, Username, Password) 
VALUES (1, 'Trần Văn Mạnh', 'Nam', '0902222222', 1, 'nvtg_user2', '123');

INSERT INTO NhanVienThuGom (MaDv, TenNvtg, GioiTinh, Sdt, MaTruongNhom, Username, Password) 
VALUES (4, 'Lê Thị Nga', 'Nữ', '0903333333', NULL, 'nvtg_user3', '123');

INSERT INTO NhanVienThuGom (MaDv, TenNvtg, GioiTinh, Sdt, MaTruongNhom, Username, Password) 
VALUES (4, 'Phạm Văn Phúc', 'Nam', '0904444444', 3, 'nvtg_user4', '123');

INSERT INTO NhanVienThuGom (MaDv, TenNvtg, GioiTinh, Sdt, MaTruongNhom, Username, Password) 
VALUES (3, 'Hoàng Thị Quỳnh', 'Nữ', '0905555555', NULL, 'nvtg_user5', '123');


-- Thêm dữ liệu vào bảng NhanVienDieuPhoi
INSERT INTO NhanVienDieuPhoi (TenNvdp, Username, Password, GioiTinh, Sdt, VaiTro) 
VALUES ('Trần Văn Quang', 'nvdp_user1', '123', 'Nam', '0911111111', 'Quản lý');

INSERT INTO NhanVienDieuPhoi (TenNvdp, Username, Password, GioiTinh, Sdt, VaiTro) 
VALUES ('Lê Thị Mai', 'nvdp_user2', '123', 'Nữ', '0922222222', 'Điều phối');

INSERT INTO NhanVienDieuPhoi (TenNvdp, Username, Password, GioiTinh, Sdt, VaiTro) 
VALUES ('Phạm Văn Đức', 'nvdp_user3', '123', 'Nam', '0933333333', 'Quản lý');

INSERT INTO NhanVienDieuPhoi (TenNvdp, Username, Password, GioiTinh, Sdt, VaiTro) 
VALUES ('Nguyễn Thị Hồng', 'nvdp_user4', '123', 'Nữ', '0944444444', 'Điều phối');

INSERT INTO NhanVienDieuPhoi (TenNvdp, Username, Password, GioiTinh, Sdt, VaiTro) 
VALUES ('Hoàng Văn Tuấn', 'nvdp_user5', '123', 'Nam', '0955555555', 'Quản lý'); 


-- Thêm dữ liệu vào bảng HopDong
INSERT INTO HopDong (MaChuThai, LoaiHopDong, NgBatDau, NgKetThuc, DiaChiThuGom, MoTa, TrangThai)
VALUES (1, 'Dài hạn', TO_DATE('2024-01-01', 'YYYY-MM-DD'), TO_DATE('2024-12-31', 'YYYY-MM-DD'),'123 Lê Lợi, Quận 1', NULL, 'Hoạt động');

INSERT INTO HopDong (MaChuThai, LoaiHopDong, NgBatDau, NgKetThuc, DiaChiThuGom, MoTa, TrangThai)
VALUES (2, 'Ngắn hạn', TO_DATE('2024-01-01', 'YYYY-MM-DD'), TO_DATE('2024-06-30', 'YYYY-MM-DD'), 'KCN Tân Bình, Quận Tân Phú', NULL, 'Hoạt động');

INSERT INTO HopDong (MaChuThai, LoaiHopDong, NgBatDau, NgKetThuc,  DiaChiThuGom, MoTa, TrangThai)
VALUES (3, 'Dài hạn', TO_DATE('2024-02-01', 'YYYY-MM-DD'), TO_DATE('2024-12-31', 'YYYY-MM-DD'),'45 Nguyễn Huệ, Quận 1', NULL, 'Hoạt động');

INSERT INTO HopDong (MaChuThai, LoaiHopDong, NgBatDau, NgKetThuc, DiaChiThuGom, MoTa, TrangThai)
VALUES (4, 'Ngắn hạn', TO_DATE('2024-03-01', 'YYYY-MM-DD'), TO_DATE('2024-08-31', 'YYYY-MM-DD'),'80 Trường Chinh, Q12', NULL, 'Tạm dừng');

INSERT INTO HopDong (MaChuThai, LoaiHopDong, NgBatDau, NgKetThuc, DiaChiThuGom, MoTa, TrangThai)
VALUES (5, 'Dài hạn', TO_DATE('2024-01-15', 'YYYY-MM-DD'), TO_DATE('2024-12-31', 'YYYY-MM-DD'),'Công viên Gia Định', NULL, 'Hoạt động');

-- Thêm dữ liệu vào bảng DichVu
INSERT INTO DichVu (TenDichVu, DonViTinh, DonGia) VALUES ('Thu gom rác sinh hoạt', 'Kg', 40000);
INSERT INTO DichVu (TenDichVu, DonViTinh, DonGia) VALUES ('Thu gom rác công nghiệp', 'Kg', 800000);
INSERT INTO DichVu (TenDichVu, DonViTinh, DonGia) VALUES ('Thu gom rác điện tử', 'Kg', 20000);
INSERT INTO DichVu (TenDichVu, DonViTinh, DonGia) VALUES ('Thu gom rác xây dựng', 'Kg', 150000);
INSERT INTO DichVu (TenDichVu, DonViTinh, DonGia) VALUES ('Thu gom rác tái chế', 'Kg', 50000);

-- Thêm dữ liệu vào bảng ChiTietHopDong
INSERT INTO ChiTietHopDong (MaHopDong, MaDichVu, KhoiLuong, GhiChu)
SELECT SeqHopDong.CURRVAL, SeqDichVu.CURRVAL, 30, 'Thu gom rác sinh hoạt' FROM DUAL;

INSERT INTO ChiTietHopDong (MaHopDong, MaDichVu, KhoiLuong, GhiChu)
SELECT SeqHopDong.CURRVAL - 1, SeqDichVu.CURRVAL - 1, 5, 'Thu gom rác công nghiệp' FROM DUAL;

INSERT INTO ChiTietHopDong (MaHopDong, MaDichVu, KhoiLuong, GhiChu)
SELECT SeqHopDong.CURRVAL - 2, SeqDichVu.CURRVAL - 2, 100, 'Thu gom rác điện tử' FROM DUAL;

INSERT INTO ChiTietHopDong (MaHopDong, MaDichVu, KhoiLuong, GhiChu)
SELECT SeqHopDong.CURRVAL - 3, SeqDichVu.CURRVAL - 3, 2, 'Thu gom rác xây dựng' FROM DUAL;

INSERT INTO ChiTietHopDong (MaHopDong, MaDichVu, KhoiLuong, GhiChu)
SELECT SeqHopDong.CURRVAL - 4, SeqDichVu.CURRVAL - 4, 10, 'Thu gom rác tái chế' FROM DUAL;


-- Thêm dữ liệu vào bảng HoaDon
INSERT INTO HoaDon (MaHopDong, MaNvdp, NgLap, TinhTrang)
SELECT SeqHopDong.CURRVAL, SeqNvdp.CURRVAL, TO_DATE('2024-01-31', 'YYYY-MM-DD'), 'Đã thanh toán' FROM DUAL;

INSERT INTO HoaDon (MaHopDong, MaNvdp, NgLap, TinhTrang)
SELECT SeqHopDong.CURRVAL-1, SeqNvdp.CURRVAL-1, TO_DATE('2024-03-28', 'YYYY-MM-DD'), 'Chưa thanh toán' FROM DUAL;

INSERT INTO HoaDon (MaHopDong, MaNvdp, NgLap, TinhTrang)
SELECT SeqHopDong.CURRVAL-2, SeqNvdp.CURRVAL-2, TO_DATE('2024-02-29', 'YYYY-MM-DD'), 'Đã thanh toán' FROM DUAL;

INSERT INTO HoaDon (MaHopDong, MaNvdp, NgLap, TinhTrang)
SELECT SeqHopDong.CURRVAL-3, SeqNvdp.CURRVAL-3, TO_DATE('2024-03-31', 'YYYY-MM-DD'), 'Đã thanh toán' FROM DUAL;

INSERT INTO HoaDon (MaHopDong, MaNvdp, NgLap, TinhTrang)
SELECT SeqHopDong.CURRVAL-4, SeqNvdp.CURRVAL-4, TO_DATE('2024-01-31', 'YYYY-MM-DD'), 'Chưa thanh toán' FROM DUAL;

-- Thêm dữ liệu vào bảng Quan
INSERT INTO Quan (MaQuan, TenQuan) VALUES (1, 'Quận 1');
INSERT INTO Quan (MaQuan, TenQuan) VALUES (2, 'Quận 3');
INSERT INTO Quan (MaQuan, TenQuan) VALUES (3, 'Quận 5');
INSERT INTO Quan (MaQuan, TenQuan) VALUES (4, 'Quận Bình Thạnh');
INSERT INTO Quan (MaQuan, TenQuan) VALUES (5, 'Quận Gò Vấp');

-- Thêm dữ liệu vào bảng TuyenDuongThuGom
INSERT INTO TuyenDuongThuGom (MaDv, TenTuyen, KhuVuc)
VALUES (1, 'Tuyến Nguyễn Huệ - Lê Lợi', 1);

INSERT INTO TuyenDuongThuGom (MaDv, TenTuyen, KhuVuc)
VALUES (2, 'Tuyến Lê Duẩn - Nam Kỳ Khởi Nghĩa', 2);

INSERT INTO TuyenDuongThuGom (MaDv, TenTuyen, KhuVuc)
VALUES (3, 'Tuyến Võ Văn Tần - Cao Thắng', 3);

INSERT INTO TuyenDuongThuGom (MaDv, TenTuyen, KhuVuc)
VALUES (4, 'Tuyến Điện Biên Phủ - Hai Bà Trưng', 4);

INSERT INTO TuyenDuongThuGom (MaDv, TenTuyen, KhuVuc)
VALUES (5, 'Tuyến Nguyễn Thị Minh Khai - Cách Mạng Tháng 8', 5);


-- Thêm dữ liệu vào bảng LichThuGom
INSERT INTO LichThuGom (MaNvdp, MaTuyen, NgThu, GioThu, TrangThai)
SELECT SeqNvdp.CURRVAL, SeqNvdp.CURRVAL, '2', '07:00', 'Hoạt động' FROM DUAL;

INSERT INTO LichThuGom (MaNvdp, MaTuyen, NgThu, GioThu, TrangThai)
SELECT SeqNvdp.CURRVAL, SeqNvdp.CURRVAL-1, '3', '08:00', 'Hoạt động' FROM DUAL;

INSERT INTO LichThuGom (MaNvdp, MaTuyen, NgThu, GioThu, TrangThai)
SELECT SeqNvdp.CURRVAL, SeqNvdp.CURRVAL-2, '4', '07:30', 'Hoạt động' FROM DUAL;

INSERT INTO LichThuGom (MaNvdp, MaTuyen, NgThu, GioThu, TrangThai)
SELECT SeqNvdp.CURRVAL, SeqNvdp.CURRVAL-3, '5', '08:30', 'Tạm dừng' FROM DUAL;

INSERT INTO LichThuGom (MaNvdp, MaTuyen, NgThu, GioThu, TrangThai)
SELECT SeqNvdp.CURRVAL, SeqNvdp.CURRVAL-4, '6', '07:00', 'Hoạt động' FROM DUAL;


-- Thêm dữ liệu vào bảng YeuCauDatLich
INSERT INTO YeuCauDatLich (MaChuThai, MaLich, ThoiGianYc, GhiChu, TrangThai)
SELECT SeqChuThai.CURRVAL, SeqLichThuGom.CURRVAL, TO_DATE('2024-01-15', 'YYYY-MM-DD'), 'Thu gom rác sinh hoạt', 'Đã duyệt' FROM DUAL;

INSERT INTO YeuCauDatLich (MaChuThai, MaLich, ThoiGianYc, GhiChu, TrangThai)
SELECT SeqChuThai.CURRVAL-1, SeqLichThuGom.CURRVAL-1, TO_DATE('2024-01-16', 'YYYY-MM-DD'), 'Thu gom rác công nghiệp', 'Đang xử lý' FROM DUAL;

INSERT INTO YeuCauDatLich (MaChuThai, MaLich, ThoiGianYc, GhiChu, TrangThai)
SELECT SeqChuThai.CURRVAL-2, SeqLichThuGom.CURRVAL-2, TO_DATE('2024-01-17', 'YYYY-MM-DD'), 'Thu gom rác tái chế', 'Đã duyệt' FROM DUAL;

INSERT INTO YeuCauDatLich (MaChuThai, MaLich, ThoiGianYc, GhiChu, TrangThai)
SELECT SeqChuThai.CURRVAL-3, SeqLichThuGom.CURRVAL-3, TO_DATE('2024-01-18', 'YYYY-MM-DD'), 'Thu gom rác sinh hoạt', 'Từ chối' FROM DUAL;

INSERT INTO YeuCauDatLich (MaChuThai, MaLich, ThoiGianYc, GhiChu, TrangThai)
SELECT SeqChuThai.CURRVAL-4, SeqLichThuGom.CURRVAL-4, TO_DATE('2024-01-19', 'YYYY-MM-DD'), 'Thu gom rác công nghiệp', 'Đã duyệt' FROM DUAL;


-- Thêm dữ liệu vào bảng PhanCong
INSERT INTO PhanCong (MaNvdp, MaLich, MaNvtg)
SELECT SeqNvdp.CURRVAL, SeqLichThuGom.CURRVAL, SeqNvtg.CURRVAL FROM DUAL;

INSERT INTO PhanCong (MaNvdp, MaLich, MaNvtg)
SELECT SeqNvdp.CURRVAL-1, SeqLichThuGom.CURRVAL-1, SeqNvtg.CURRVAL-1 FROM DUAL;

INSERT INTO PhanCong (MaNvdp, MaLich, MaNvtg)
SELECT SeqNvdp.CURRVAL-2, SeqLichThuGom.CURRVAL-2, SeqNvtg.CURRVAL-2 FROM DUAL;

INSERT INTO PhanCong (MaNvdp, MaLich, MaNvtg)
SELECT SeqNvdp.CURRVAL-3, SeqLichThuGom.CURRVAL-3, SeqNvtg.CURRVAL-3 FROM DUAL;

INSERT INTO PhanCong (MaNvdp, MaLich, MaNvtg)
SELECT SeqNvdp.CURRVAL-4, SeqLichThuGom.CURRVAL-4, SeqNvtg.CURRVAL-4 FROM DUAL;

-- Thêm dữ liệu vào bảng ChamCong
INSERT INTO ChamCong (MaNvdp, MaNvtg, NgayCong, GhiChu, TrangThai)
SELECT SeqNvdp.CURRVAL, SeqNvtg.CURRVAL, TO_DATE('2024-01-15', 'YYYY-MM-DD'), 'Đi làm đúng giờ', 'Đã xác nhận' FROM DUAL;

INSERT INTO ChamCong (MaNvdp, MaNvtg, NgayCong, GhiChu, TrangThai)
SELECT SeqNvdp.CURRVAL-1, SeqNvtg.CURRVAL-1, TO_DATE('2024-01-15', 'YYYY-MM-DD'), 'Đi trễ 15 phút', 'Đã xác nhận' FROM DUAL;

INSERT INTO ChamCong (MaNvdp, MaNvtg, NgayCong, GhiChu, TrangThai)
SELECT SeqNvdp.CURRVAL-2, SeqNvtg.CURRVAL-2, TO_DATE('2024-01-15', 'YYYY-MM-DD'), 'Làm việc tốt', 'Đã xác nhận' FROM DUAL;

INSERT INTO ChamCong (MaNvdp, MaNvtg, NgayCong, GhiChu, TrangThai)
SELECT SeqNvdp.CURRVAL-3, SeqNvtg.CURRVAL-3, TO_DATE('2024-01-15', 'YYYY-MM-DD'), 'Nghỉ có phép', 'Đã xác nhận' FROM DUAL;

INSERT INTO ChamCong (MaNvdp, MaNvtg, NgayCong, GhiChu, TrangThai)
SELECT SeqNvdp.CURRVAL-4, SeqNvtg.CURRVAL-4, TO_DATE('2024-01-15', 'YYYY-MM-DD'), 'Làm thêm giờ', 'Đã xác nhận' FROM DUAL;


-- Thêm dữ liệu vào bảng PhanAnh
INSERT INTO PhanAnh (MaChuThai, NoiDung, ThoiGianGui, TrangThai)
SELECT SeqChuThai.CURRVAL, 'Thu gom trễ giờ đã hẹn', SYSDATE, 'Đang xử lý' FROM DUAL;

INSERT INTO PhanAnh (MaChuThai, NoiDung, ThoiGianGui, TrangThai)
SELECT SeqChuThai.CURRVAL-1, 'Nhân viên thu gom không mang đủ dụng cụ', SYSDATE, 'Đã xử lý' FROM DUAL;

INSERT INTO PhanAnh (MaChuThai, NoiDung, ThoiGianGui, TrangThai)
SELECT SeqChuThai.CURRVAL-2, 'Rác không được phân loại đúng quy định', SYSDATE, 'Đang xử lý' FROM DUAL;

INSERT INTO PhanAnh (MaChuThai, NoiDung, ThoiGianGui, TrangThai)
SELECT SeqChuThai.CURRVAL-3, 'Thái độ phục vụ chưa tốt', SYSDATE, 'Đã xử lý' FROM DUAL;

INSERT INTO PhanAnh (MaChuThai, NoiDung, ThoiGianGui, TrangThai)
SELECT SeqChuThai.CURRVAL-4, 'Không thu gom đúng lịch hẹn', SYSDATE, 'Đang xử lý' FROM DUAL;


-- Thêm dữ liệu vào bảng TtPhanLoaiRac
INSERT INTO TtPhanLoaiRac (TenLoaiRac, MoTa, HdPhanLoai, HinhAnh)
VALUES ('Rác hữu cơ', 'Rác thải sinh hoạt dễ phân hủy', 'Bỏ vào thùng màu xanh', EMPTY_BLOB());

INSERT INTO TtPhanLoaiRac (TenLoaiRac, MoTa, HdPhanLoai, HinhAnh)
VALUES ('Rác vô cơ', 'Rác thải khó phân hủy', 'Bỏ vào thùng màu đen', EMPTY_BLOB());

INSERT INTO TtPhanLoaiRac (TenLoaiRac, MoTa, HdPhanLoai, HinhAnh)
VALUES ('Rác tái chế', 'Rác có thể tái chế được', 'Bỏ vào thùng màu vàng', EMPTY_BLOB());

INSERT INTO TtPhanLoaiRac (TenLoaiRac, MoTa, HdPhanLoai, HinhAnh)
VALUES ('Rác nguy hại', 'Rác thải độc hại cần xử lý đặc biệt', 'Bỏ vào thùng màu đỏ', EMPTY_BLOB());

INSERT INTO TtPhanLoaiRac (TenLoaiRac, MoTa, HdPhanLoai, HinhAnh)
VALUES ('Rác xây dựng', 'Rác thải từ hoạt động xây dựng', 'Liên hệ đơn vị thu gom chuyên biệt', EMPTY_BLOB());
/
commit;
/

//FUNCTION//////////////////////////////////////////////////////////////
//Tinh tong hoa don theo hop dong
CREATE OR REPLACE FUNCTION FUNC_TongTienTheoHopDong (
    p_MaHopDong IN NUMBER
) RETURN NUMBER IS
    v_tong NUMBER;
BEGIN
    SELECT NVL(SUM(SoTien), 0) INTO v_tong
    FROM HoaDon
    WHERE MaHopDong = p_MaHopDong;

    RETURN v_tong;
END;
/

SELECT FUNC_TongTienTheoHopDong(1) AS TongTien FROM DUAL;
/

//Lay lich thu gom theo chu thai
CREATE OR REPLACE FUNCTION FUNC_LayLichTheoChuThai (
    p_MaChuThai IN NUMBER
) RETURN SYS_REFCURSOR IS
    rc SYS_REFCURSOR;
BEGIN
    OPEN rc FOR
    SELECT ltg.MaLich, ltg.MaNvdp, ltg.NgThu, ltg.GioThu, ltg.TrangThai
    FROM YeuCauDatLich yc
    JOIN LichThuGom ltg ON yc.MaLich = ltg.MaLich
    WHERE yc.MaChuThai = p_MaChuThai;

    RETURN rc;
END;

/
SET SERVEROUTPUT ON;

DECLARE
    v_cursor SYS_REFCURSOR;
    v_MaLich NUMBER;
    v_MaNvdp NUMBER;
    v_NgThu VARCHAR2(2);
    v_GioThu VARCHAR2(5);
    v_TrangThai VARCHAR2(50);
BEGIN
    v_cursor := FUNC_LayLichTheoChuThai(1);

    LOOP
        FETCH v_cursor INTO v_MaLich, v_MaNvdp, v_NgThu, v_GioThu, v_TrangThai;
        EXIT WHEN v_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE(
            'MaLich: ' || v_MaLich || 
            ', MaNvdp: ' || v_MaNvdp || 
            ', NgThu: ' || v_NgThu || 
            ', GioThu: ' || v_GioThu || 
            ', TrangThai: ' || v_TrangThai
        );
    END LOOP;

    CLOSE v_cursor;
END;
/


//tinh so cong cua nhan vien thu gom
CREATE OR REPLACE FUNCTION FUNC_TinhSoCongNVTG (
    p_MaNvtg IN NUMBER
) RETURN NUMBER IS
    v_cong NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_cong
    FROM ChamCong
    WHERE MaNvtg = p_MaNvtg;

    RETURN v_cong;
END;
/
SELECT FUNC_TinhSoCongNVTG(1) as SOCONG FROM DUAL;
/

//Dem so luong don phan anh
CREATE OR REPLACE FUNCTION FUNC_DemPhanAnhChuThai (
    p_MaChuThai IN NUMBER
) RETURN NUMBER IS
    v_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM PhanAnh
    WHERE MaChuThai = p_MaChuThai;

    RETURN v_count;
END;
/
SELECT FUNC_DemPhanAnhChuThai(1) as SoLuongPA FROM DUAL;
/
//Kiem tra nhan vien thu gom da co lich trong ngay
CREATE OR REPLACE FUNCTION FUNC_KiemTraNVTG_LichTrongNgay (
    p_MaNvtg IN NUMBER,
    p_Ngay   IN DATE
) RETURN NUMBER IS
    v_count NUMBER;
    v_thu   VARCHAR2(2);
BEGIN
    v_thu := TO_CHAR(p_Ngay, 'D');

    SELECT COUNT(*) INTO v_count
    FROM PHANCONG pc
    JOIN LichThuGom ltg ON pc.MaLich = ltg.MaLich
    WHERE pc.MaNvtg = p_MaNvtg AND ltg.NgThu = v_thu;

    RETURN v_count;
END;
/
SELECT FUNC_KiemTraNVTG_LichTrongNgay(1, TO_DATE('2025-05-08', 'YYYY-MM-DD')) AS SoLich
FROM DUAL;

/

//Kiem tra su ton tai cua lich theo ma
CREATE OR REPLACE FUNCTION FUNC_KiemTraLichTonTai(p_MaLich NUMBER) RETURN NUMBER IS
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM LichThuGom
    WHERE MaLich = p_MaLich;

    RETURN v_count; 
END;
/
SELECT FUNC_KiemTraLichTonTai(1) AS TrangThai FROM DUAL;
/
//Kiem tra trang thai hoat dong cua lich thu gom
CREATE OR REPLACE FUNCTION FUNC_DaHoanThanh(p_MaLich NUMBER) RETURN NUMBER IS
    v_trangThai VARCHAR2(50);
BEGIN
    SELECT TrangThai INTO v_trangThai
    FROM LichThuGom
    WHERE MaLich = p_MaLich;

    IF UPPER(v_trangThai) = 'HOAT DONG' THEN
        RETURN 1;
    ELSIF (v_trangThai) = 'TAM DUNG' THEN
        RETURN 0;
    ELSE 
        RETURN -1;
    END IF;
END;
/
SELECT FUNC_DaHoanThanh(1) AS TrangThai FROM DUAL;
/
//Tra ve so luong lich theo trang thai
CREATE OR REPLACE FUNCTION FUNC_DemLichTheoTrangThai(p_TrangThai VARCHAR2) RETURN NUMBER IS
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM LichThuGom
    WHERE UPPER(TrangThai) = UPPER(p_TrangThai);

    RETURN v_count;
END;
/
SELECT FUNC_DemLichTheoTrangThai('Hoạt động') AS SOLUONG FROM DUAL;
/
//Kiem tra lich co trung ngay gio khong
CREATE OR REPLACE FUNCTION FUNC_KiemTraTrungLich(
    p_Ngay DATE,
    p_GioThu VARCHAR2
) RETURN NUMBER IS
    v_count NUMBER;
    v_ThuTrongTuan VARCHAR2(2);
BEGIN
    v_ThuTrongTuan := TO_CHAR(p_Ngay, 'D');  

    SELECT COUNT(*) INTO v_count
    FROM LichThuGom
    WHERE NgThu = v_ThuTrongTuan AND GioThu = p_GioThu;

    RETURN v_count;
END;

/
SELECT FUNC_KiemTraTrungLich(TO_DATE('2025-05-09', 'YYYY-MM-DD'), '08:00') AS TrangThaiTrung FROM DUAL;
/
//Lay gio thu gom theo ma lich
CREATE OR REPLACE FUNCTION FUNC_LayGioThu(p_MaLich NUMBER) RETURN VARCHAR2 IS
    v_gio VARCHAR2(5);
BEGIN
    SELECT GioThu INTO v_gio
    FROM LichThuGom
    WHERE MaLich = p_MaLich;

    RETURN v_gio;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN NULL;
END;
/
SELECT FUNC_LayGioThu(1) AS THOIGIAN FROM DUAL;
/
-- Kiểm tra lịch có tồn tại hay không
CREATE OR REPLACE FUNCTION FUNC_KiemTraLichTonTai (
    p_MaLich IN NUMBER
) RETURN NUMBER IS
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM LichThuGom
    WHERE MaLich = p_MaLich;

    RETURN v_count; -- Nếu = 0: không tồn tại, > 0: tồn tại
END;
/


--============================================PROCEDURE===============================================

--Thêm mới chủ thải
CREATE OR REPLACE PROCEDURE AddChuThai(
    p_HoTen IN VARCHAR2,
    p_DiaChi IN VARCHAR2,
    p_Sdt IN VARCHAR2,
    p_Email IN VARCHAR2,
    p_LoaiChuThai IN VARCHAR2
) AS
BEGIN
    INSERT INTO ChuThai (HoTen, DiaChi, Sdt, Email, LoaiChuThai)
    VALUES (p_HoTen, p_DiaChi, p_Sdt, p_Email, p_LoaiChuThai);
END AddChuThai;
/
--Thêm mới đơn vị thu gom
CREATE OR REPLACE PROCEDURE AddDonViThuGom(
    p_TenDv IN VARCHAR2,
    p_KhuVucPhuTrach IN VARCHAR2
) AS
BEGIN
    INSERT INTO DonViThuGom (TenDv, KhuVucPhuTrach)
    VALUES (p_TenDv, p_KhuVucPhuTrach);
END AddDonViThuGom;
/
--Thêm mới nhân viên thu gom
CREATE OR REPLACE PROCEDURE AddNhanVienThuGom(
    p_MaDv IN NUMBER,
    p_TenNvtg IN VARCHAR2,
    p_GioiTinh IN VARCHAR2,
    p_Sdt IN VARCHAR2,
    p_MaTruongNhom IN NUMBER
) AS
BEGIN
    INSERT INTO NhanVienThuGom (MaDv, TenNvtg, GioiTinh, Sdt, MaTruongNhom)
    VALUES (p_MaDv, p_TenNvtg, p_GioiTinh, p_Sdt, p_MaTruongNhom);
END AddNhanVienThuGom;
/
--Thêm mới hợp đồng
CREATE OR REPLACE PROCEDURE AddHopDong(
    p_MaChuThai IN NUMBER,
    p_LoaiHopDong IN VARCHAR2,
    p_NgBatDau IN DATE,
    p_NgKetThuc IN DATE,
    p_GiaTri IN NUMBER,
    p_MoTa IN CLOB,
    p_TrangThai IN VARCHAR2
) AS
BEGIN
    INSERT INTO HopDong (MaChuThai, LoaiHopDong, NgBatDau, NgKetThuc, GiaTri, MoTa, TrangThai)
    VALUES (p_MaChuThai, p_LoaiHopDong, p_NgBatDau, p_NgKetThuc, p_GiaTri, p_MoTa, p_TrangThai);
END AddHopDong;
/
--Thêm mới hóa đơn
CREATE OR REPLACE PROCEDURE AddHoaDon(
    p_MaHopDong IN NUMBER,
    p_MaNvdp IN NUMBER,
    p_NgLap IN DATE,
    p_SoTien IN NUMBER,
    p_TinhTrang IN VARCHAR2
) AS
BEGIN
    INSERT INTO HoaDon (MaHopDong, MaNvdp, NgLap, SoTien, TinhTrang)
    VALUES (p_MaHopDong, p_MaNvdp, p_NgLap, p_SoTien, p_TinhTrang);
END AddHoaDon;
/
--Thêm mới yêu cầu đặt lịch
CREATE OR REPLACE PROCEDURE AddYeuCauDatLich(
    p_MaChuThai IN NUMBER,
    p_MaLich IN NUMBER,
    p_ThoiGianYc IN DATE,
    p_GhiChu IN CLOB,
    p_TrangThai IN VARCHAR2
) AS
BEGIN
    INSERT INTO YeuCauDatLich (MaChuThai, MaLich, ThoiGianYc, GhiChu, TrangThai)
    VALUES (p_MaChuThai, p_MaLich, p_ThoiGianYc, p_GhiChu, p_TrangThai);
END AddYeuCauDatLich;
/
--Thêm mới phân công
CREATE OR REPLACE PROCEDURE AddPhanCong(
    p_MaNvdp IN NUMBER,
    p_MaLich IN NUMBER,
    p_MaNvtg IN NUMBER
) AS
BEGIN
    INSERT INTO PhanCong (MaNvdp, MaLich, MaNvtg)
    VALUES (p_MaNvdp, p_MaLich, p_MaNvtg);
END AddPhanCong;
/
--Thêm mới tuyến đường thu gom
CREATE OR REPLACE PROCEDURE AddTuyenDuongThuGom(
    p_MaDv IN NUMBER,
    p_TenTuyen IN VARCHAR2,
    p_KhuVuc IN VARCHAR2
) AS
BEGIN
    INSERT INTO TuyenDuongThuGom (MaDv, TenTuyen, KhuVuc)
    VALUES (p_MaDv, p_TenTuyen, p_KhuVuc);
END AddTuyenDuongThuGom;
/
--Thêm mới chấm công
CREATE OR REPLACE PROCEDURE AddChamCong(
    p_MaNvdp IN NUMBER,
    p_MaNvtg IN NUMBER,
    p_NgayCong IN DATE,
    p_GhiChu IN VARCHAR2,
    p_TrangThai IN VARCHAR2
) AS
BEGIN
    INSERT INTO ChamCong (MaNvdp, MaNvtg, NgayCong, GhiChu, TrangThai)
    VALUES (p_MaNvdp, p_MaNvtg, p_NgayCong, p_GhiChu, p_TrangThai);
END AddChamCong;
/
--Thêm mới phản ánh
CREATE OR REPLACE PROCEDURE AddPhanAnh(
    p_MaChuThai IN NUMBER,
    p_NoiDung IN CLOB,
    p_ThoiGianGui IN DATE,
    p_TrangThai IN VARCHAR2
) AS
BEGIN
    INSERT INTO PhanAnh (MaChuThai, NoiDung, ThoiGianGui, TrangThai)
    VALUES (p_MaChuThai, p_NoiDung, p_ThoiGianGui, p_TrangThai);
END AddPhanAnh;
/
--Cập nhật trạng thái hợp đồng
CREATE OR REPLACE PROCEDURE UpdateTrangThaiHopDong(
    p_MaHopDong IN NUMBER,
    p_TrangThai IN VARCHAR2
) AS
BEGIN
    UPDATE HopDong SET TrangThai = p_TrangThai WHERE MaHopDong = p_MaHopDong;
END UpdateTrangThaiHopDong;
/
--Xóa phản ánh
CREATE OR REPLACE PROCEDURE DeletePhanAnh(
    p_MaPa IN NUMBER
) AS
BEGIN
    DELETE FROM PhanAnh WHERE MaPA = p_MaPa;
END DeletePhanAnh;
/
--Cập nhật thông tin chủ thải
CREATE OR REPLACE PROCEDURE UpdateChuThai(
    p_MaChuThai IN NUMBER,
    p_HoTen IN VARCHAR2,
    p_DiaChi IN VARCHAR2,
    p_Sdt IN VARCHAR2,
    p_Email IN VARCHAR2,
    p_LoaiChuThai IN VARCHAR2
) AS
BEGIN
    UPDATE ChuThai 
    SET HoTen = p_HoTen, DiaChi = p_DiaChi, Sdt = p_Sdt, Email = p_Email, LoaiChuThai = p_LoaiChuThai
    WHERE MaChuThai = p_MaChuThai;
END UpdateChuThai;
/
--Cập nhật thông tin nhân viên thu gom
CREATE OR REPLACE PROCEDURE UpdateNhanVienThuGom(
    p_MaNvtg IN NUMBER,
    p_MaDv IN NUMBER,
    p_TenNvtg IN VARCHAR2,
    p_GioiTinh IN VARCHAR2,
    p_Sdt IN VARCHAR2,
    p_MaTruongNhom IN NUMBER
) AS
BEGIN
    UPDATE NhanVienThuGom 
    SET MaDv = p_MaDv, TenNvtg = p_TenNvtg, GioiTinh = p_GioiTinh, Sdt = p_Sdt, MaTruongNhom = p_MaTruongNhom
    WHERE MaNvtg = p_MaNvtg;
END UpdateNhanVienThuGom;
/
--Xóa yêu cầu đặt lịch
CREATE OR REPLACE PROCEDURE DeleteYeuCauDatLich(
    p_MaYc IN NUMBER
) AS
BEGIN
    DELETE FROM YeuCauDatLich WHERE MaYc = p_MaYc;
END DeleteYeuCauDatLich;
/
-- Phân công nhân viên thu gom
CREATE OR REPLACE PROCEDURE PhanCongNhanVien (
    p_MaLich   IN NUMBER,
    p_MaNvdp   IN NUMBER,
    p_MaNvtg   IN NUMBER
) AS
    v_MaPc       PhanCong.MaPc%TYPE;
    v_MaNvtgOld  PhanCong.MaNvtg%TYPE;
BEGIN
    -- Kiểm tra lịch tồn tại
    IF FUNC_KiemTraLichTonTai(p_MaLich) = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Lịch thu gom không tồn tại.');
    END IF;

    -- Truy vấn phân công hiện có và khóa dòng (chống truy xuất đồng thời)
    SELECT MaPc, MaNvtg
    INTO v_MaPc, v_MaNvtgOld
    FROM PhanCong
    WHERE MaLich = p_MaLich
    FOR UPDATE;

    -- Nếu đã phân công đúng, không cần cập nhật
    IF v_MaNvtgOld = p_MaNvtg THEN
        DBMS_OUTPUT.PUT_LINE('Nhân viên đã được phân công.');
        RETURN;
    END IF;

    -- Cập nhật nếu nhân viên vẫn như cũ
    UPDATE PhanCong
    SET MaNvtg = p_MaNvtg, MaNvdp = p_MaNvdp
    WHERE MaPc = v_MaPc;

    DBMS_OUTPUT.PUT_LINE('Cập nhật phân công thành công.');

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Nếu chưa có phân công, thêm mới
        INSERT INTO PhanCong(MaNvdp, MaLich, MaNvtg)
        VALUES (p_MaNvdp, p_MaLich, p_MaNvtg);
        DBMS_OUTPUT.PUT_LINE('Thêm phân công mới thành công.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Lỗi: ' || SQLERRM);
        RAISE;
END;
/

COMMIT;

