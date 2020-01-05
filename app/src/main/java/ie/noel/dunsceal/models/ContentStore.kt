package ie.noel.dunsceal.models

interface ContentStore {
  fun findAll(): List<Content?>?
  fun create(content: Content)
  fun update(content: Content)
  fun delete(content: Content)
  fun findById(id: Int) : Content?
  fun clear()
}