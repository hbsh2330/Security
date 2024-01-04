package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cart_id;

    @ManyToOne //외래키
    @JoinColumn(name = "imageboard_id",foreignKey = @ForeignKey(name="FK_cart_imageBoard",
            foreignKeyDefinition ="FOREIGN KEY(imageboard_id) REFERENCES image_board(id) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private ImageBoard imageBoard;

    @ManyToOne //외래키
    @JoinColumn(name = "username",foreignKey = @ForeignKey(name="FK_cart_user_01",
            foreignKeyDefinition ="FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE ON UPDATE CASCADE" ))
    private User user;

    private LocalDateTime regdate;
}
