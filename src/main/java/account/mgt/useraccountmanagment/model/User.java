package account.mgt.useraccountmanagment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private Integer age;
    @Column(name = "dob")
//    @DateTimeFormat(pattern = "mm-dd-yyyy")
    private Date dateOfBirth;
    private EMaritalStatus status;
    private String nationality;
    private String password;
    private boolean active=true;
    private boolean verified;
    @Column(name = "profile_picture")
    private byte[] profilePicture;
    @OneToOne(mappedBy = "user")
    private AccountVerification verification;
    @Column(name = "phone_number",unique = true)
    private String phoneNumber;
    private boolean validated;
    private Integer otp;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    public User(Long id) {
        this.id = id;
    }

    public boolean hasRole(String roleName) {
        if(role.getRoleName().equals(roleName))
            return true;
        return false;
    }
}
