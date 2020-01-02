package ie.noel.dunsceal.persistence.db;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ie.noel.dunsceal.models.entity.DunEntity;
import ie.noel.dunsceal.models.entity.EntranceEntity;
import ie.noel.dunsceal.models.entity.InvestigationEntity;

/**
 * Utility class that holds values to be used for testing.
 */
public class TestData {

    static final DunEntity DUN_ENTITY = new DunEntity(1, "name", "desc", 3);
    static final DunEntity DUN_ENTITY2 = new DunEntity(2, "name2", "desc2", 20);

    static final List<DunEntity> DUNS = Arrays.asList(DUN_ENTITY, DUN_ENTITY2);

    static final InvestigationEntity INVESTIGATION_ENTITY = new InvestigationEntity(1, DUN_ENTITY.getId(),
            "desc", new Date());
    static final InvestigationEntity INVESTIGATION_ENTITY2 = new InvestigationEntity(2,
            DUN_ENTITY2.getId(), "desc2", new Date());

    static final List<InvestigationEntity> INVESTIGATIONS = Arrays.asList(INVESTIGATION_ENTITY, INVESTIGATION_ENTITY2);

    static final EntranceEntity ENTRANCE_ENTITY = new EntranceEntity(1, DUN_ENTITY.getId(),
        "desc", new Date());
    static final EntranceEntity ENTRANCE_ENTITY2 = new EntranceEntity(2,
        DUN_ENTITY2.getId(), "desc2", new Date());

    static final List<EntranceEntity> ENTRANCES = Arrays.asList(ENTRANCE_ENTITY, ENTRANCE_ENTITY2);
}
