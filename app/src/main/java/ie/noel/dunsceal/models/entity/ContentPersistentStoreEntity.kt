package ie.noel.dunsceal.models.entity

interface ContentPersistentStoreEntity {
  fun findAll(): List<ContentEntity?>
  fun createContent(dun: ContentEntity)
  fun updateContent(dun: ContentEntity)
  fun deleteContent(dun: ContentEntity)
  fun findById(id: Int) : ContentEntity?
  fun clear()
}