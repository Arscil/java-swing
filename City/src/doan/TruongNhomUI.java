package doan;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TruongNhomUI extends JFrame {
    private String maNvtg;
    private String tenNvtg;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public TruongNhomUI(String maNvtg, String tenNvtg) {
        this.maNvtg = maNvtg;
        this.tenNvtg = tenNvtg;
        setTitle("Trưởng nhóm thu gom");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Verify if user is a team leader
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String checkSql = "SELECT COUNT(*) as Count FROM NhanVienThuGom WHERE MaTruongNhom = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, maNvtg);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt("Count") == 0) {
                JOptionPane.showMessageDialog(this,
                    "Bạn không phải là trưởng nhóm!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                dispose();
                new GiaoDienDangNhap().setVisible(true);
                return;
            }
            
            rs.close();
            checkStmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi kiểm tra quyền trưởng nhóm: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            new GiaoDienDangNhap().setVisible(true);
            return;
        }

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(1200, 50));

        JLabel titleLabel = new JLabel("Trưởng Nhóm Thu Gom", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        JLabel staffInfoLabel = new JLabel(tenNvtg + " - Mã: " + maNvtg);
        staffInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        staffInfoLabel.setForeground(Color.WHITE);
        staffInfoLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new GiaoDienDangNhap().setVisible(true);
            }
        });
        rightPanel.add(staffInfoLabel);
        rightPanel.add(logoutButton);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Sidebar menu
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(44, 62, 80));
        menuPanel.setPreferredSize(new Dimension(220, 0));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Create menu buttons
        JButton lichButton = createMenuButton("Xem lịch thu gom");
        JButton chamCongButton = createMenuButton("Chấm công");
        JButton phanCongButton = createMenuButton("Phân công");
        JButton nhanVienButton = createMenuButton("Quản lý nhân viên");
        JButton settingsButton = createMenuButton("Cài đặt");

        menuPanel.add(lichButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(chamCongButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(phanCongButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(nhanVienButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(settingsButton);
        menuPanel.add(Box.createVerticalGlue());

        // Content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add content panels
        contentPanel.add(createLichThuGomPanel(), "Xem lịch thu gom");
        contentPanel.add(createChamCongPanel(), "Chấm công");
        contentPanel.add(createPhanCongPanel(), "Phân công");
        contentPanel.add(createQuanLyNhanVienPanel(), "Quản lý nhân viên");
        contentPanel.add(createSettingsPanel(), "Cài đặt");

        // Add action listeners for menu buttons
        lichButton.addActionListener(e -> cardLayout.show(contentPanel, "Xem lịch thu gom"));
        chamCongButton.addActionListener(e -> cardLayout.show(contentPanel, "Chấm công"));
        phanCongButton.addActionListener(e -> cardLayout.show(contentPanel, "Phân công"));
        nhanVienButton.addActionListener(e -> cardLayout.show(contentPanel, "Quản lý nhân viên"));
        settingsButton.addActionListener(e -> cardLayout.show(contentPanel, "Cài đặt"));

        // Layout
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        cardLayout.show(contentPanel, "Xem lịch thu gom");
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(44, 62, 80));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 73, 94));
            }
        });
        return button;
    }

    private JPanel createLichThuGomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Lịch Thu Gom", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlPanel.setBackground(Color.WHITE);

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);

        // Refresh Button
        JButton refreshButton = new JButton("Làm mới");
        refreshButton.setBackground(new Color(40, 167, 69));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);

        // Filter by Status
        JLabel statusLabel = new JLabel("Trạng thái:");
        String[] statusOptions = {"Tất cả", "Hoạt động", "Tạm dừng"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);

        controlPanel.add(new JLabel("Tìm kiếm:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(refreshButton);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(statusLabel);
        controlPanel.add(statusCombo);

        // Table
        String[] columnNames = {"Mã Lịch", "Tuyến Thu Gom", "Khu Vực", "Thứ", "Giờ Thu", "Trạng Thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);

        // Load initial data
        loadLichThuGomData(model, null, (String)statusCombo.getSelectedItem());

        // Search Button Action
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            String status = (String)statusCombo.getSelectedItem();
            loadLichThuGomData(model, searchTerm, status);
        });

        // Status Filter Action
        statusCombo.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            String status = (String)statusCombo.getSelectedItem();
            loadLichThuGomData(model, searchTerm, status);
        });

        // Refresh Button Action
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            statusCombo.setSelectedItem("Tất cả");
            loadLichThuGomData(model, null, "Tất cả");
        });

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadLichThuGomData(DefaultTableModel model, String searchTerm, String status) {
        model.setRowCount(0);
        try {
            Connection conn = ConnectionJDBC.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT ltg.MaLich, tuyen.TenTuyen, quan.TenQuan, ltg.NgThu, ltg.GioThu, " +
                "CASE TO_CHAR(ltg.TrangThai) " +
                "   WHEN '1' THEN 'Hoạt động' " +
                "   WHEN '0' THEN 'Tạm dừng' " +
                "   ELSE 'Không xác định' " +
                "END as TrangThai " +
                "FROM LichThuGom ltg " +
                "JOIN PhanCong pc ON ltg.MaLich = pc.MaLich " +
                "JOIN TuyenDuongThuGom tuyen ON ltg.MaTuyen = tuyen.MaTuyen " +
                "JOIN Quan quan ON tuyen.KhuVuc = quan.MaQuan " +
                "WHERE pc.MaNvtg = ? "
            );

            if (searchTerm != null && !searchTerm.isEmpty()) {
                sql.append(" AND (LOWER(tuyen.TenTuyen) LIKE LOWER(?) OR LOWER(quan.TenQuan) LIKE LOWER(?))");
            }

            if (status != null && !status.equals("Tất cả")) {
                if (status.equals("Hoạt động")) {
                    sql.append(" AND ltg.TrangThai = 1");
                } else if (status.equals("Tạm dừng")) {
                    sql.append(" AND ltg.TrangThai = 0");
                }
            }

            sql.append(" ORDER BY ltg.NgThu, ltg.GioThu");
            
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            pstmt.setString(paramIndex++, maNvtg);

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                pstmt.setString(paramIndex++, searchPattern);
                pstmt.setString(paramIndex++, searchPattern);
            }

            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaLich"),
                    rs.getString("TenTuyen"),
                    rs.getString("TenQuan"),
                    getDayOfWeek(rs.getInt("NgThu")),
                    rs.getString("GioThu"),
                    rs.getString("TrangThai")
                };
                model.addRow(row);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải dữ liệu lịch thu gom: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getDayOfWeek(int day) {
        switch (day) {
            case 2: return "Thứ 2";
            case 3: return "Thứ 3";
            case 4: return "Thứ 4";
            case 5: return "Thứ 5";
            case 6: return "Thứ 6";
            case 7: return "Thứ 7";
            case 8: return "Chủ nhật";
            default: return "Không xác định";
        }
    }

    private JPanel createChamCongPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Chấm Công Nhân Viên", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlPanel.setBackground(Color.WHITE);

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);

        // Date Picker
        JLabel dateLabel = new JLabel("Ngày:");
        JSpinner datePicker = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datePicker, "dd/MM/yyyy");
        datePicker.setEditor(dateEditor);
        datePicker.setValue(new Date());

        // Refresh Button
        JButton refreshButton = new JButton("Làm mới");
        refreshButton.setBackground(new Color(108, 117, 125));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);

        // Chấm công Button
        JButton chamCongButton = new JButton("Chấm công");
        chamCongButton.setBackground(new Color(40, 167, 69));
        chamCongButton.setForeground(Color.WHITE);
        chamCongButton.setEnabled(false); // Initially disabled

        controlPanel.add(new JLabel("Tìm kiếm:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(dateLabel);
        controlPanel.add(datePicker);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(refreshButton);
        controlPanel.add(chamCongButton);

        // Table
        String[] columnNames = {"Mã NV", "Tên nhân viên", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add selection listener for table
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            chamCongButton.setEnabled(selectedRow != -1);
            if (selectedRow != -1) {
                String status = (String)table.getValueAt(selectedRow, 2);
                chamCongButton.setText(status.equals("Đã chấm công") ? "Hủy chấm công" : "Chấm công");
                chamCongButton.setBackground(status.equals("Đã chấm công") ? 
                    new Color(220, 53, 69) : new Color(40, 167, 69));
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        // Load initial data
        loadDanhSachNhanVien(model, null, (Date)datePicker.getValue());

        // Chấm công Button Action
        chamCongButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String maNv = (String)table.getValueAt(selectedRow, 0);
                String tenNv = (String)table.getValueAt(selectedRow, 1);
                String currentStatus = (String)table.getValueAt(selectedRow, 2);
                
                if (currentStatus.equals("Đã chấm công")) {
                    // Show confirmation dialog for canceling attendance
                    int confirm = JOptionPane.showConfirmDialog(
                        panel,
                        "Bạn có muốn hủy chấm công cho nhân viên này?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        updateChamCong(maNv, (Date)datePicker.getValue(), 0, model);
                    }
                } else {
                    // Show note dialog for new attendance
                    showGhiChuDialog(maNv, tenNv, (Date)datePicker.getValue(), model);
                }
            }
        });

        // Search Button Action
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            loadDanhSachNhanVien(model, searchTerm, (Date)datePicker.getValue());
        });

        // Date Picker Action
        datePicker.addChangeListener(e -> {
            String searchTerm = searchField.getText().trim();
            loadDanhSachNhanVien(model, searchTerm, (Date)datePicker.getValue());
            table.clearSelection();
            chamCongButton.setEnabled(false);
            chamCongButton.setText("Chấm công");
            chamCongButton.setBackground(new Color(40, 167, 69));
        });

        // Refresh Button Action
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            datePicker.setValue(new Date());
            loadDanhSachNhanVien(model, null, (Date)datePicker.getValue());
            table.clearSelection();
            chamCongButton.setEnabled(false);
            chamCongButton.setText("Chấm công");
            chamCongButton.setBackground(new Color(40, 167, 69));
        });

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void updateChamCong(String maNvtg, Date ngayCong, int trangThai, DefaultTableModel model) {
        try {
            Connection conn = ConnectionJDBC.getConnection();
            
            if (trangThai == 1) {
                // Insert new attendance record
                String insertSql = "INSERT INTO ChamCong (MaNvdp, MaNvtg, NgayCong, TrangThai, GhiChu) VALUES (?, ?, ?, 1, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertSql);
                pstmt.setString(1, this.maNvtg);
                pstmt.setString(2, maNvtg);
                pstmt.setDate(3, new java.sql.Date(ngayCong.getTime()));
                pstmt.setString(4, model.getValueAt(model.getRowCount()-1, 3).toString()); // Get note from model
                pstmt.executeUpdate();
                pstmt.close();
            } else {
                // Delete existing attendance record
                String deleteSql = "DELETE FROM ChamCong WHERE MaNvtg = ? AND TRUNC(NgayCong) = TRUNC(?)";
                PreparedStatement pstmt = conn.prepareStatement(deleteSql);
                pstmt.setString(1, maNvtg);
                pstmt.setDate(2, new java.sql.Date(ngayCong.getTime()));
                pstmt.executeUpdate();
                pstmt.close();
            }
            
            conn.close();
            
            // Reload the table
            loadDanhSachNhanVien(model, null, ngayCong);
            
            JOptionPane.showMessageDialog(this,
                trangThai == 1 ? "Chấm công thành công!" : "Đã hủy chấm công!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi cập nhật chấm công: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showGhiChuDialog(String maNv, String tenNv, Date ngayCong, DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Nhập ghi chú chấm công", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(new JLabel("Nhân viên: " + tenNv));
        infoPanel.add(new JLabel("Ngày: " + new SimpleDateFormat("dd/MM/yyyy").format(ngayCong)));

        // Note Panel
        JPanel notePanel = new JPanel(new BorderLayout(5, 5));
        notePanel.setBackground(Color.WHITE);
        notePanel.add(new JLabel("Ghi chú:"), BorderLayout.NORTH);
        
        JTextArea noteArea = new JTextArea(5, 30);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(noteArea);
        notePanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = new JButton("Lưu");
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        
        JButton cancelButton = new JButton("Hủy");
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);

        saveButton.addActionListener(e -> {
            String note = noteArea.getText().trim();
            // Add note to model temporarily
            model.addRow(new Object[]{"", "", "", note}); // Add temporary row for note
            updateChamCong(maNv, ngayCong, 1, model);
            model.removeRow(model.getRowCount()-1); // Remove temporary row
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(notePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void loadDanhSachNhanVien(DefaultTableModel model, String searchTerm, Date date) {
        model.setRowCount(0);
        try {
            Connection conn = ConnectionJDBC.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT nv.MaNvtg, nv.TenNvtg, " +
                "CASE " +
                "   WHEN cc.MaCc IS NULL THEN 'Chưa chấm công' " +
                "   ELSE 'Đã chấm công' " +
                "END as TrangThai, " +
                "cc.GhiChu " +
                "FROM NhanVienThuGom nv " +
                "JOIN PhanCong pc ON nv.MaNvtg = pc.MaNvtg " +
                "JOIN LichThuGom l ON pc.MaLich = l.MaLich " +
                "LEFT JOIN ChamCong cc ON nv.MaNvtg = cc.MaNvtg AND TRUNC(cc.NgayCong) = TRUNC(?) " +
                "WHERE nv.MaTruongNhom = ? " +
                "AND l.NgThu = TO_NUMBER(TO_CHAR(?, 'D')) "
            );

            if (searchTerm != null && !searchTerm.isEmpty()) {
                sql.append(" AND (LOWER(nv.TenNvtg) LIKE LOWER(?) OR nv.MaNvtg LIKE ?)");
            }

            sql.append(" ORDER BY nv.TenNvtg");
            
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            pstmt.setDate(paramIndex++, new java.sql.Date(date.getTime()));
            pstmt.setString(paramIndex++, maNvtg);
            pstmt.setDate(paramIndex++, new java.sql.Date(date.getTime()));

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                pstmt.setString(paramIndex++, searchPattern);
                pstmt.setString(paramIndex, searchPattern);
            }

            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaNvtg"),
                    rs.getString("TenNvtg"),
                    rs.getString("TrangThai"),
                    rs.getString("GhiChu")
                };
                model.addRow(row);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách nhân viên: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createPhanCongPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Phân Công Nhân Viên", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlPanel.setBackground(Color.WHITE);

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);

        // Add Button
        JButton addButton = new JButton("Thêm phân công");
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);

        // Refresh Button
        JButton refreshButton = new JButton("Làm mới");
        refreshButton.setBackground(new Color(108, 117, 125));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);

        // Filter by Day
        JLabel dayLabel = new JLabel("Thứ:");
        String[] days = {"Tất cả", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        JComboBox<String> dayCombo = new JComboBox<>(days);

        controlPanel.add(new JLabel("Tìm kiếm:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(dayLabel);
        controlPanel.add(dayCombo);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(addButton);
        controlPanel.add(refreshButton);

        // Table
        String[] columnNames = {"Mã PC", "Mã Lịch", "Nhân viên", "Tuyến thu gom", "Thời gian", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add selection listener for table
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Color.WHITE);
        
        JButton editButton = new JButton("Sửa");
        editButton.setBackground(new Color(255, 193, 7));
        editButton.setForeground(Color.WHITE);
        editButton.setEnabled(false);
        
        JButton deleteButton = new JButton("Xóa");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setEnabled(false);
        
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        
        // Enable/disable buttons based on selection
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = table.getSelectedRow() != -1;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection);
        });
        
        JScrollPane scrollPane = new JScrollPane(table);

        // Load initial data
        loadPhanCongData(model, null, (String)dayCombo.getSelectedItem());

        // Add Button Action
        addButton.addActionListener(e -> showPhanCongDialog(null, model, dayCombo));

        // Edit Button Action
        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String maPC = table.getValueAt(row, 0).toString();
                showPhanCongDialog(maPC, model, dayCombo);
            }
        });

        // Delete Button Action
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String maPC = table.getValueAt(row, 0).toString();
                String tenNv = table.getValueAt(row, 2).toString();
                String tuyenTG = table.getValueAt(row, 3).toString();
                
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn xóa phân công của nhân viên " + tenNv + " cho tuyến " + tuyenTG + "?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    deletePhanCong(maPC, model, (String)dayCombo.getSelectedItem());
                }
            }
        });

        // Search functionality
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                loadPhanCongData(model, null, (String)dayCombo.getSelectedItem());
                return;
            }
            searchPhanCong(searchTerm, model, (String)dayCombo.getSelectedItem());
        });

        // Day Filter Action
        dayCombo.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            String selectedDay = (String)dayCombo.getSelectedItem();
            loadPhanCongData(model, searchTerm, selectedDay);
        });

        // Refresh Button Action
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            dayCombo.setSelectedItem("Tất cả");
            loadPhanCongData(model, null, "Tất cả");
        });

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(actionPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadPhanCongData(DefaultTableModel model, String searchTerm, String selectedDay) {
        model.setRowCount(0);
        try {
            Connection conn = ConnectionJDBC.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT pc.MaPC, pc.MaLich, nv.TenNvtg, t.TenTuyen, l.NgThu, l.GioThu, " +
                "CASE TO_CHAR(l.TrangThai) " +
                "   WHEN '1' THEN 'Hoạt động' " +
                "   WHEN '0' THEN 'Tạm dừng' " +
                "   ELSE 'Không xác định' " +
                "END as TrangThai " +
                "FROM PhanCong pc " +
                "JOIN NhanVienThuGom nv ON pc.MaNvtg = nv.MaNvtg " +
                "JOIN LichThuGom l ON pc.MaLich = l.MaLich " +
                "JOIN TuyenDuongThuGom t ON l.MaTuyen = t.MaTuyen " +
                "WHERE nv.MaTruongNhom = ? "
            );

            if (searchTerm != null && !searchTerm.isEmpty()) {
                sql.append(" AND (LOWER(nv.TenNvtg) LIKE LOWER(?) OR LOWER(t.TenTuyen) LIKE LOWER(?))");
            }

            if (selectedDay != null && !selectedDay.equals("Tất cả")) {
                int dayNumber = getDayNumber(selectedDay);
                if (dayNumber > 0) {
                    sql.append(" AND l.NgThu = ?");
                }
            }

            sql.append(" ORDER BY l.NgThu ASC, l.GioThu ASC");
            
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            pstmt.setString(paramIndex++, maNvtg);

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                pstmt.setString(paramIndex++, searchPattern);
                pstmt.setString(paramIndex++, searchPattern);
            }

            if (selectedDay != null && !selectedDay.equals("Tất cả")) {
                int dayNumber = getDayNumber(selectedDay);
                if (dayNumber > 0) {
                    pstmt.setInt(paramIndex, dayNumber);
                }
            }

            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String thu = getDayOfWeek(rs.getInt("NgThu"));
                Object[] row = {
                    rs.getString("MaPC"),
                    rs.getString("MaLich"),
                    rs.getString("TenNvtg"),
                    rs.getString("TenTuyen"),
                    thu + " " + rs.getString("GioThu"),
                    rs.getString("TrangThai")
                };
                model.addRow(row);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải dữ liệu phân công: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getDayNumber(String dayName) {
        switch (dayName) {
            case "Thứ 2": return 2;
            case "Thứ 3": return 3;
            case "Thứ 4": return 4;
            case "Thứ 5": return 5;
            case "Thứ 6": return 6;
            case "Thứ 7": return 7;
            case "Chủ nhật": return 1;
            default: return -1;
        }
    }

    private void showPhanCongDialog(String maPC, DefaultTableModel model, JComboBox<String> dayCombo) {
        JDialog dialog = new JDialog(this, maPC == null ? "Thêm phân công" : "Sửa phân công", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Employee Selection
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nhân viên:"), gbc);

        JComboBox<String> employeeCombo = new JComboBox<>();
        gbc.gridx = 1;
        formPanel.add(employeeCombo, gbc);

        // Schedule Selection
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Lịch thu gom:"), gbc);

        JComboBox<String> scheduleCombo = new JComboBox<>();
        gbc.gridx = 1;
        formPanel.add(scheduleCombo, gbc);

        try {
            Connection conn = ConnectionJDBC.getConnection();
            
            // Load employees
            String empSql = "SELECT MaNvtg, TenNvtg FROM NhanVienThuGom WHERE MaTruongNhom = ?";
            PreparedStatement empStmt = conn.prepareStatement(empSql);
            empStmt.setString(1, maNvtg);
            ResultSet empRs = empStmt.executeQuery();

            while (empRs.next()) {
                employeeCombo.addItem(empRs.getString("MaNvtg") + " - " + empRs.getString("TenNvtg"));
            }

            // Load schedules
            String schSql = "SELECT l.MaLich, t.TenTuyen, l.NgThu, l.GioThu " +
                          "FROM LichThuGom l " +
                          "JOIN TuyenDuongThuGom t ON l.MaTuyen = t.MaTuyen " +
                          "WHERE l.TrangThai = 'Hoạt động'";
            PreparedStatement schStmt = conn.prepareStatement(schSql);
            ResultSet schRs = schStmt.executeQuery();

            while (schRs.next()) {
                String thu = "Thứ " + schRs.getString("NgThu");
                if (schRs.getString("NgThu").equals("1")) {
                    thu = "Chủ nhật";
                }
                scheduleCombo.addItem(schRs.getString("MaLich") + " - " + 
                                    schRs.getString("TenTuyen") + " (" + 
                                    thu + " " + schRs.getString("GioThu") + ")");
            }

            // If editing, load current values
            if (maPC != null) {
                String loadSql = "SELECT pc.MaNvtg, pc.MaLich " +
                               "FROM PhanCong pc " +
                               "WHERE pc.MaPC = ?";
                PreparedStatement loadStmt = conn.prepareStatement(loadSql);
                loadStmt.setString(1, maPC);
                ResultSet loadRs = loadStmt.executeQuery();

                if (loadRs.next()) {
                    String maNvtg = loadRs.getString("MaNvtg");
                    String maLich = loadRs.getString("MaLich");

                    // Set selected employee
                    for (int i = 0; i < employeeCombo.getItemCount(); i++) {
                        if (employeeCombo.getItemAt(i).startsWith(maNvtg)) {
                            employeeCombo.setSelectedIndex(i);
                            break;
                        }
                    }

                    // Set selected schedule
                    for (int i = 0; i < scheduleCombo.getItemCount(); i++) {
                        if (scheduleCombo.getItemAt(i).startsWith(maLich)) {
                            scheduleCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                loadRs.close();
                loadStmt.close();
            }

            empRs.close();
            schRs.close();
            empStmt.close();
            schStmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog,
                "Lỗi khi tải dữ liệu: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton(maPC == null ? "Thêm" : "Lưu");
        JButton cancelButton = new JButton("Hủy");

        saveButton.addActionListener(evt -> {
            try {
                Connection conn = ConnectionJDBC.getConnection();
                String selectedEmployee = (String)employeeCombo.getSelectedItem();
                String maNv = selectedEmployee.split(" - ")[0];
                String selectedSchedule = (String)scheduleCombo.getSelectedItem();
                String maLich = selectedSchedule.split(" - ")[0];

                // Check if assignment already exists
                String checkSql = "SELECT COUNT(*) as Count FROM PhanCong " +
                                "WHERE MaNvtg = ? AND MaLich = ? AND MaPC != ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, maNv);
                checkStmt.setString(2, maLich);
                checkStmt.setString(3, maPC != null ? maPC : "");
                ResultSet checkRs = checkStmt.executeQuery();

                if (checkRs.next() && checkRs.getInt("Count") > 0) {
                    JOptionPane.showMessageDialog(dialog,
                        "Nhân viên đã được phân công cho lịch thu gom này!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (maPC == null) {
                    // Insert new assignment
                    String insertSql = "INSERT INTO PhanCong (MaNvdp, MaLich, MaNvtg) VALUES (?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(insertSql);
                    pstmt.setString(1, maNvtg);
                    pstmt.setString(2, maLich);
                    pstmt.setString(3, maNv);
                    pstmt.executeUpdate();
                    pstmt.close();
                } else {
                    // Update existing assignment
                    String updateSql = "UPDATE PhanCong SET MaLich = ?, MaNvtg = ? WHERE MaPC = ?";
                    PreparedStatement pstmt = conn.prepareStatement(updateSql);
                    pstmt.setString(1, maLich);
                    pstmt.setString(2, maNv);
                    pstmt.setString(3, maPC);
                    pstmt.executeUpdate();
                    pstmt.close();
                }

                loadPhanCongData(model, null, (String)dayCombo.getSelectedItem());
                dialog.dispose();
                JOptionPane.showMessageDialog(dialog,
                    maPC == null ? "Thêm phân công thành công!" : "Cập nhật phân công thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);

                checkRs.close();
                checkStmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog,
                    "Lỗi khi " + (maPC == null ? "thêm" : "cập nhật") + " phân công: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(evt -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deletePhanCong(String maPC, DefaultTableModel model, String selectedDay) {
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "DELETE FROM PhanCong WHERE MaPC = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maPC);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                loadPhanCongData(model, null, selectedDay);
                JOptionPane.showMessageDialog(this,
                    "Xóa phân công thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xóa phân công: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchPhanCong(String searchTerm, DefaultTableModel model, String selectedDay) {
        try {
            Connection conn = ConnectionJDBC.getConnection();
            StringBuilder sql = new StringBuilder(
                "SELECT pc.MaPC, pc.MaLich, nv.TenNvtg, t.TenTuyen, l.NgThu, l.GioThu, " +
                "CASE TO_CHAR(l.TrangThai) " +
                "   WHEN '1' THEN 'Hoạt động' " +
                "   WHEN '0' THEN 'Tạm dừng' " +
                "   ELSE 'Không xác định' " +
                "END as TrangThai " +
                "FROM PhanCong pc " +
                "JOIN NhanVienThuGom nv ON pc.MaNvtg = nv.MaNvtg " +
                "JOIN LichThuGom l ON pc.MaLich = l.MaLich " +
                "JOIN TuyenDuongThuGom t ON l.MaTuyen = t.MaTuyen " +
                "WHERE nv.MaTruongNhom = ? "
            );

            if (searchTerm != null && !searchTerm.isEmpty()) {
                sql.append(" AND (LOWER(nv.TenNvtg) LIKE LOWER(?) OR LOWER(t.TenTuyen) LIKE LOWER(?))");
            }

            if (selectedDay != null && !selectedDay.equals("Tất cả")) {
                int dayNumber = getDayNumber(selectedDay);
                if (dayNumber > 0) {
                    sql.append(" AND l.NgThu = ?");
                }
            }

            sql.append(" ORDER BY l.NgThu ASC, l.GioThu ASC");
            
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            pstmt.setString(paramIndex++, maNvtg);

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                pstmt.setString(paramIndex++, searchPattern);
                pstmt.setString(paramIndex++, searchPattern);
            }

            if (selectedDay != null && !selectedDay.equals("Tất cả")) {
                int dayNumber = getDayNumber(selectedDay);
                if (dayNumber > 0) {
                    pstmt.setInt(paramIndex, dayNumber);
                }
            }

            ResultSet rs = pstmt.executeQuery();
            
            model.setRowCount(0);
            while (rs.next()) {
                String thu = getDayOfWeek(rs.getInt("NgThu"));
                Object[] row = {
                    rs.getString("MaPC"),
                    rs.getString("MaLich"),
                    rs.getString("TenNvtg"),
                    rs.getString("TenTuyen"),
                    thu + " " + rs.getString("GioThu"),
                    rs.getString("TrangThai")
                };
                model.addRow(row);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tìm kiếm phân công: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createQuanLyNhanVienPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Quản Lý Nhân Viên", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlPanel.setBackground(Color.WHITE);

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);

        // Refresh Button
        JButton refreshButton = new JButton("Làm mới");
        refreshButton.setBackground(new Color(108, 117, 125));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);

        controlPanel.add(new JLabel("Tìm kiếm:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(refreshButton);

        // Table
        String[] columnNames = {"Mã NV", "Họ tên", "Giới tính", "Số điện thoại", "Đơn vị", "Số công tháng này"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);

        // Load initial data
        loadEmployeeList(model, new Date());

        // Search functionality
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                loadEmployeeList(model, new Date());
                return;
            }
            searchNhanVien(searchTerm, model);
        });

        // Refresh Button Action
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadEmployeeList(model, new Date());
        });

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadEmployeeList(DefaultTableModel model, Date date) {
        model.setRowCount(0);
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT nv.MaNvtg, nv.TenNvtg, nv.GioiTinh, nv.Sdt, dv.TenDv, " +
                        "COUNT(cc.MaCc) as SoCong " +
                        "FROM NhanVienThuGom nv " +
                        "LEFT JOIN DonViThuGom dv ON nv.MaDv = dv.MaDv " +
                        "LEFT JOIN ChamCong cc ON nv.MaNvtg = cc.MaNvtg " +
                        "AND EXTRACT(MONTH FROM cc.NgayCong) = EXTRACT(MONTH FROM SYSDATE) " +
                        "AND EXTRACT(YEAR FROM cc.NgayCong) = EXTRACT(YEAR FROM SYSDATE) " +
                        "WHERE nv.MaTruongNhom = ? " +
                        "GROUP BY nv.MaNvtg, nv.TenNvtg, nv.GioiTinh, nv.Sdt, dv.TenDv " +
                        "ORDER BY nv.TenNvtg";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNvtg);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaNvtg"),
                    rs.getString("TenNvtg"),
                    rs.getString("GioiTinh"),
                    rs.getString("Sdt"),
                    rs.getString("TenDv"),
                    rs.getInt("SoCong")
                };
                model.addRow(row);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách nhân viên: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchNhanVien(String searchTerm, DefaultTableModel model) {
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT nv.MaNvtg, nv.TenNvtg, nv.GioiTinh, nv.Sdt, dv.TenDv, " +
                        "COUNT(cc.MaCc) as SoCong " +
                        "FROM NhanVienThuGom nv " +
                        "LEFT JOIN DonViThuGom dv ON nv.MaDv = dv.MaDv " +
                        "LEFT JOIN ChamCong cc ON nv.MaNvtg = cc.MaNvtg " +
                        "AND EXTRACT(MONTH FROM cc.NgayCong) = EXTRACT(MONTH FROM SYSDATE) " +
                        "AND EXTRACT(YEAR FROM cc.NgayCong) = EXTRACT(YEAR FROM SYSDATE) " +
                        "WHERE nv.MaTruongNhom = ? " +
                        "AND (LOWER(nv.TenNvtg) LIKE LOWER(?) OR nv.Sdt LIKE ?) " +
                        "GROUP BY nv.MaNvtg, nv.TenNvtg, nv.GioiTinh, nv.Sdt, dv.TenDv " +
                        "ORDER BY nv.TenNvtg";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNvtg);
            pstmt.setString(2, "%" + searchTerm + "%");
            pstmt.setString(3, "%" + searchTerm + "%");
            
            ResultSet rs = pstmt.executeQuery();
            model.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaNvtg"),
                    rs.getString("TenNvtg"),
                    rs.getString("GioiTinh"),
                    rs.getString("Sdt"),
                    rs.getString("TenDv"),
                    rs.getInt("SoCong")
                };
                model.addRow(row);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tìm kiếm nhân viên: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Cài đặt", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Main content panel with GridBagLayout for better control
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Personal Information Panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Thông tin cá nhân",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));

        // Create text fields for personal information
        JTextField tenNvtgField = new JTextField(20);
        JTextField sdtField = new JTextField(20);
        JComboBox<String> gioiTinhCombo = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField maDvField = new JTextField(20);

        // Make some fields uneditable
        maDvField.setEditable(false);

        // Load current information
        try {
            Connection conn = ConnectionJDBC.getConnection();
            String sql = "SELECT nvtg.*, dv.TenDv " +
                        "FROM NhanVienThuGom nvtg " +
                        "LEFT JOIN DonViThuGom dv ON nvtg.MaDv = dv.MaDv " +
                        "WHERE nvtg.MaNvtg = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maNvtg);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    tenNvtgField.setText(rs.getString("TenNvtg"));
                    sdtField.setText(rs.getString("Sdt"));
                    gioiTinhCombo.setSelectedItem(rs.getString("GioiTinh"));
                    maDvField.setText(rs.getString("MaDv") + " - " + rs.getString("TenDv"));
                }
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải thông tin nhân viên: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }

        // Add components to info panel
        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(5, 5, 5, 5);
        gbcInfo.fill = GridBagConstraints.HORIZONTAL;

        // Row 1
        gbcInfo.gridx = 0; gbcInfo.gridy = 0;
        infoPanel.add(new JLabel("Họ tên:"), gbcInfo);
        gbcInfo.gridx = 1;
        infoPanel.add(tenNvtgField, gbcInfo);

        // Row 2
        gbcInfo.gridx = 0; gbcInfo.gridy = 1;
        infoPanel.add(new JLabel("Số điện thoại:"), gbcInfo);
        gbcInfo.gridx = 1;
        infoPanel.add(sdtField, gbcInfo);

        // Row 3
        gbcInfo.gridx = 0; gbcInfo.gridy = 2;
        infoPanel.add(new JLabel("Giới tính:"), gbcInfo);
        gbcInfo.gridx = 1;
        infoPanel.add(gioiTinhCombo, gbcInfo);

        // Row 4
        gbcInfo.gridx = 0; gbcInfo.gridy = 3;
        infoPanel.add(new JLabel("Đơn vị:"), gbcInfo);
        gbcInfo.gridx = 1;
        infoPanel.add(maDvField, gbcInfo);

        // Save button
        JButton saveInfoButton = new JButton("Lưu thay đổi");
        saveInfoButton.setBackground(new Color(40, 167, 69));
        saveInfoButton.setForeground(Color.WHITE);
        saveInfoButton.setFocusPainted(false);
        gbcInfo.gridx = 1;
        gbcInfo.gridy = 4;
        gbcInfo.anchor = GridBagConstraints.EAST;
        infoPanel.add(saveInfoButton, gbcInfo);

        saveInfoButton.addActionListener(e -> {
            try {
                Connection conn = ConnectionJDBC.getConnection();
                String updateSql = "UPDATE NhanVienThuGom SET TenNvtg = ?, Sdt = ?, GioiTinh = ? WHERE MaNvtg = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setString(1, tenNvtgField.getText().trim());
                    pstmt.setString(2, sdtField.getText().trim());
                    pstmt.setString(3, (String)gioiTinhCombo.getSelectedItem());
                    pstmt.setString(4, maNvtg);
                    
                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this,
                            "Cập nhật thông tin thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi cập nhật thông tin: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Password Change Panel
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Đổi mật khẩu",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));

        JPasswordField oldPassField = new JPasswordField(20);
        JPasswordField newPassField = new JPasswordField(20);
        JPasswordField confirmPassField = new JPasswordField(20);

        GridBagConstraints gbcPass = new GridBagConstraints();
        gbcPass.insets = new Insets(5, 5, 5, 5);
        gbcPass.fill = GridBagConstraints.HORIZONTAL;

        // Row 1
        gbcPass.gridx = 0; gbcPass.gridy = 0;
        passwordPanel.add(new JLabel("Mật khẩu cũ:"), gbcPass);
        gbcPass.gridx = 1;
        passwordPanel.add(oldPassField, gbcPass);

        // Row 2
        gbcPass.gridx = 0; gbcPass.gridy = 1;
        passwordPanel.add(new JLabel("Mật khẩu mới:"), gbcPass);
        gbcPass.gridx = 1;
        passwordPanel.add(newPassField, gbcPass);

        // Row 3
        gbcPass.gridx = 0; gbcPass.gridy = 2;
        passwordPanel.add(new JLabel("Xác nhận mật khẩu:"), gbcPass);
        gbcPass.gridx = 1;
        passwordPanel.add(confirmPassField, gbcPass);

        // Change password button
        JButton changePassButton = new JButton("Đổi mật khẩu");
        changePassButton.setBackground(new Color(0, 123, 255));
        changePassButton.setForeground(Color.WHITE);
        changePassButton.setFocusPainted(false);
        gbcPass.gridx = 1;
        gbcPass.gridy = 3;
        gbcPass.anchor = GridBagConstraints.EAST;
        passwordPanel.add(changePassButton, gbcPass);

        changePassButton.addActionListener(e -> {
            String oldPass = new String(oldPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ thông tin!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this,
                    "Mật khẩu mới không khớp!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = ConnectionJDBC.getConnection();
                // First verify old password
                String checkSql = "SELECT Password FROM NhanVienThuGom WHERE MaNvtg = ? AND Password = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, maNvtg);
                    checkStmt.setString(2, oldPass);
                    ResultSet rs = checkStmt.executeQuery();
                    
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this,
                            "Mật khẩu cũ không đúng!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Update new password
                String updateSql = "UPDATE NhanVienThuGom SET Password = ? WHERE MaNvtg = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, newPass);
                    updateStmt.setString(2, maNvtg);
                    
                    int result = updateStmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this,
                            "Đổi mật khẩu thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                        oldPassField.setText("");
                        newPassField.setText("");
                        confirmPassField.setText("");
                    }
                }
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi đổi mật khẩu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add panels to main content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        contentPanel.add(infoPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.5;
        contentPanel.add(passwordPanel, gbc);

        // Add components to main panel
        panel.add(title, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TruongNhomUI tnUI = new TruongNhomUI("TN001", "Nguyễn Văn Trưởng");
            tnUI.setVisible(true);
        });
    }
}