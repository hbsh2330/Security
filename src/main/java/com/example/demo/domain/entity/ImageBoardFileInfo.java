package com.example.demo.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class ImageBoardFileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne //외래키
    @JoinColumn(name = "imageboard_id",foreignKey = @ForeignKey(name="FK_imagefileInfo_imageBoard",
            foreignKeyDefinition ="FOREIGN KEY(imageboard_id) REFERENCES image_board(id) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private ImageBoard imageBoard;
    private String dir;
    private String filename;
}
