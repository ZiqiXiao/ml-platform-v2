package com.ziqi.mlplatform.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String fileName;
    private String filePath;
    private String templateName;
    private String templatePath;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "upload_file_id", referencedColumnName = "id")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ToString.Exclude
    private List<Model> models;
}
