package account.mgt.useraccountmanagment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_verification")
public class AccountVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nid;
    @Column(unique = true)
    private String passportNumber;
    @Column(name = "nid_document_name")
    private String nidDocumentName;
    @Column(name = "passport_document_name")
    private String passportDocumentName;
    private EAccountStates states;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

}
