import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "spectacole")
public class Spectacol implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date data_spectacol;
    private String titlu;
    private Float pret_bilet;
    private Integer capacitate;
    String lista_locuri_vandute;

    public void setLista_locuri_vandute(String lista_locuri_vandute) {
        this.lista_locuri_vandute = lista_locuri_vandute;
    }
}
