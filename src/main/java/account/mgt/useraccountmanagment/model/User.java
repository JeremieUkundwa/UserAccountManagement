package account.mgt.useraccountmanagment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date dateOfBirth;
    private EMaritalStatus status;
    private String nationality;
    private String password;
    private boolean active;
    private boolean verified;

    public User(Long id) {
        this.id = id;
    }
}
