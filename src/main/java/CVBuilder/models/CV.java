package CVBuilder.models;

import java.util.ArrayList;
import java.util.List;

public class CV {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String profileImageURI;
    private final List<String> educations = new ArrayList<>();
    private final List<String> skills = new ArrayList<>();
    private final List<String> experiences = new ArrayList<>();
    private final List<String> projects = new ArrayList<>();

    public CV() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<String> getEducations() { return educations; }
    public List<String> getSkills() { return skills; }
    public List<String> getExperiences() { return experiences; }
    public List<String> getProjects() { return projects; }
    public String getProfileImageURI() { return profileImageURI; }
    public void setProfileImageURI(String profileImageURI) { this.profileImageURI = profileImageURI; }
}
