import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class AddressHierarchy {
    private int objectId;
    private int parentObjId;
    private int prevId;
    private int nextId;
    private Date startDate;
    private Date endDate;
    private boolean isActive;
}