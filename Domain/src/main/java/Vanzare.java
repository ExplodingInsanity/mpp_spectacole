import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="vanzari")
public class Vanzare implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer id_spectacol;
    private LocalDateTime data_vanzare;
    private Integer nr_bilete_vandute;
    private Float pret_bilet;
    private String lista_locuri_vandute;
    private Float suma;
}
