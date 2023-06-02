package com.ziqi.mlplatform.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String modelName;
    private String modelClass;
    private String modelPath;
    private String modelDescription;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {}, optional = true)
    @JoinColumn(name = "upload_file_id", nullable = true)
//    @JsonBackReference
    private TrainData trainData;
}
