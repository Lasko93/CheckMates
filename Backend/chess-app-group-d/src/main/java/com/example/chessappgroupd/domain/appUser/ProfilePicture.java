package com.example.chessappgroupd.domain.appUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "PROFILE_PICTURE")
@Data
@AllArgsConstructor
@NoArgsConstructor
//ProfilePicture
//One User may have an ProfilePicture
//It can be set at registration
public class ProfilePicture {
    @Id
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "TYPE")
    private String type;
    @Lob
    @Column(name = "IMAGES")
    private byte[] images;
}