package ie.noel.dunsceal.persistence.db

import ie.noel.dunsceal.models.entity.Dun
import ie.noel.dunsceal.models.entity.InvestigationEntity
import java.util.*

/**
 * Utility class that holds values to be used for testing.
 */
object TestData {
  val DUN_ENTITY = Dun(1, "name", "desc", 3)
  val DUN_ENTITY2 = Dun(2, "name2", "desc2", 20)
  val DUNS = Arrays.asList(DUN_ENTITY, DUN_ENTITY2)
  val INVESTIGATION_ENTITY = InvestigationEntity(1, DUN_ENTITY.id,
      "desc", Date())
  val INVESTIGATION_ENTITY2 = InvestigationEntity(2,
      DUN_ENTITY2.id, "desc2", Date())
  val INVESTIGATIONS = Arrays.asList(INVESTIGATION_ENTITY, INVESTIGATION_ENTITY2)
}