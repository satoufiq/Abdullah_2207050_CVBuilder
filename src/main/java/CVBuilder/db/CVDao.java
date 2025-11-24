package CVBuilder.db;

import CVBuilder.models.CV;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CVDao {
    private static final String SEP = "|||";

    public CVDao() {
        DBUtil.getInstance();
    }

    private String join(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join(SEP, list);
    }

    private List<String> splitToList(String s) {
        if (s == null || s.isBlank()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(s.split(java.util.regex.Pattern.quote(SEP), -1)));
    }

    public int insert(CV cv) throws SQLException {
        String sql = "INSERT INTO cv(fullName,email,phone,address,profileImageURI,educations,skills,experiences,projects) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cv.getFullName());
            ps.setString(2, cv.getEmail());
            ps.setString(3, cv.getPhone());
            ps.setString(4, cv.getAddress());
            ps.setString(5, cv.getProfileImageURI());
            ps.setString(6, join(cv.getEducations()));
            ps.setString(7, join(cv.getSkills()));
            ps.setString(8, join(cv.getExperiences()));
            ps.setString(9, join(cv.getProjects()));

            int affected = ps.executeUpdate();
            if (affected == 0) throw new SQLException("Insert failed, no rows affected.");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    cv.setId(id);
                    return id;
                } else {
                    throw new SQLException("Insert failed, no ID obtained.");
                }
            }
        }
    }

    public boolean update(CV cv) throws SQLException {
        String sql = "UPDATE cv SET fullName=?,email=?,phone=?,address=?,profileImageURI=?,educations=?,skills=?,experiences=?,projects=? WHERE id=?";
        try (Connection c = DBUtil.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cv.getFullName());
            ps.setString(2, cv.getEmail());
            ps.setString(3, cv.getPhone());
            ps.setString(4, cv.getAddress());
            ps.setString(5, cv.getProfileImageURI());
            ps.setString(6, join(cv.getEducations()));
            ps.setString(7, join(cv.getSkills()));
            ps.setString(8, join(cv.getExperiences()));
            ps.setString(9, join(cv.getProjects()));
            ps.setInt(10, cv.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM cv WHERE id=?";
        try (Connection c = DBUtil.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<CV> findAll() throws SQLException {
        List<CV> list = new ArrayList<>();
        String sql = "SELECT * FROM cv ORDER BY created_at DESC";
        try (Connection c = DBUtil.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public CV findById(int id) throws SQLException {
        String sql = "SELECT * FROM cv WHERE id=?";
        try (Connection c = DBUtil.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    private CV mapRow(ResultSet rs) throws SQLException {
        CV cv = new CV();
        cv.setId(rs.getInt("id"));
        cv.setFullName(rs.getString("fullName"));
        cv.setEmail(rs.getString("email"));
        cv.setPhone(rs.getString("phone"));
        cv.setAddress(rs.getString("address"));
        cv.setProfileImageURI(rs.getString("profileImageURI"));
        cv.getEducations().addAll(splitToList(rs.getString("educations")));
        cv.getSkills().addAll(splitToList(rs.getString("skills")));
        cv.getExperiences().addAll(splitToList(rs.getString("experiences")));
        cv.getProjects().addAll(splitToList(rs.getString("projects")));
        return cv;
    }
}

