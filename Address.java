import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class Address {
    private int objectId;
    private String name;
    private String typeName;
    private Date startDate;
    private Date endDate;
    private boolean isActual;
    private boolean isActive;

    public boolean isActualOnDate(Date date) {
        return date.before(endDate) && date.after(startDate);
    }

}
